package com.hongte.alms.core.controller;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.TdrepayRechargeLogMapper;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.util.ExcelData;
import com.hongte.alms.base.util.ExcelUtils;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.util.AliyunHelper;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.core.enums.PlatformStatusTypeEnum;
import com.hongte.alms.core.enums.ProcessStatusTypeEnum;
import com.hongte.alms.core.enums.RepaySourceEnum;
import com.hongte.alms.core.storage.StorageService;
import com.ht.ussp.util.BeanUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * @author zengkun
 * @since 2018/2/25
 */
@RestController
@RequestMapping("downLoadController")
@Api(tags = "DownLoadController", description = "文件下载相关接口")
public class DownLoadController  implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(DownLoadController.class);

    private final StorageService storageService;
    
    @Autowired
	@Qualifier("DocService")
	private DocService docService;
    
    @Autowired
    private AliyunHelper ossClient;
    
    @Autowired
	private TdrepayRechargeLogMapper tdrepayRechargeLogMapper;
    

    public DownLoadController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @ApiOperation(value = "下载Excel文件接口")
    @RequestMapping("excelFiles")
    public void downloadExcel(@RequestParam("downloadFile") String downloadFile, @RequestParam("docUrl") String docUrl) {
		LOG.info("@文件下载@下载Excel文件--开始[{}]" , downloadFile);
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletResponse response = requestAttributes.getResponse();
		docService.download(downloadFile, docUrl, response);
        //删除文件
        ossClient.deleteObject(docUrl);
		LOG.info("@文件下载@下载Excel文件--结束[{}]","");
    }

	@ApiOperation(value = "下载Excel GBK编码文件接口")
	@RequestMapping("downLoadExcelGBK")
	public void downLoadExcelGBK(@RequestParam("downloadFile") String downloadFile, @RequestParam("docUrl") String docUrl) {
		LOG.info("@文件下载@下载Excel GBK文件--开始[{}]" , downloadFile);
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletResponse response = requestAttributes.getResponse();
		docService.downloadGbk(downloadFile, docUrl, response);
		//删除文件
		ossClient.deleteObject(docUrl);
		LOG.info("@文件下载@下载Excel GBK文件--结束[{}]","");
	}


    @RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam("downloadFile") String downloadFile, @RequestParam("docUrl") String docUrl) {
		LOG.info("@文件下载@下载附件--开始[{}]" , downloadFile);
		if (StringUtil.isEmpty(downloadFile) || StringUtil.isEmpty(docUrl)) {
			LOG.error("非法参数！downloadFile：" + downloadFile + "，docUrl：" + docUrl);
			return;
		}

		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletResponse response = requestAttributes.getResponse();
			docService.download(downloadFile, docUrl, response);
			LOG.info("附件下载成功！");
		} catch (Exception e) {
			LOG.error("文件下载失败：" + e.getMessage());
		}
		LOG.info("@文件下载@下载附件--结束[{}]" , downloadFile);
	}


    /**
	 * 导出资金分发数据
	 * 
	 * @param
	 */
	@ApiOperation(value = "导出资金分发数据")
	@PostMapping("/exportComplianceRepaymentData")
	@ResponseBody
	public void exportComplianceRepaymentData(@ModelAttribute ComplianceRepaymentVO vo) {

		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletResponse response = requestAttributes.getResponse();
			ExcelData data = new ExcelData();
			data.setName("资金分发数据");
			// 表头
			List<String> titles = new ArrayList<>();
			titles.add("业务编号");
			titles.add("业务类型");
			titles.add("还款方式");
			titles.add("借款人");
			titles.add("还款期数");
			titles.add("实还日期");
			titles.add("分公司");
			titles.add("状态");
			titles.add("流水合计");
			titles.add("实收总额");
			titles.add("充值金额");
			titles.add("平台状态");
			titles.add("分发状态");
			titles.add("备注");
			data.setTitles(titles);

			if (vo != null && vo.getConfirmTimeEnd() != null) {
				vo.setConfirmTimeEnd(DateUtil.addDay2Date(1, vo.getConfirmTimeEnd()));
			}
			vo.setIsExport(1);	// 导出
			int count = tdrepayRechargeLogMapper.countComplianceRepaymentData(vo);
			if (count == 0) {
				// 生成本地
				ExcelUtils.exportExcel(response, "资金分发数据.xls", data);
			}

			List<TdrepayRechargeLog> list = tdrepayRechargeLogMapper.queryComplianceRepaymentData(vo);

			List<TdrepayRechargeInfoVO> infoVOs = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(list)) {
				for (TdrepayRechargeLog tdrepayRechargeLog : list) {
					TdrepayRechargeInfoVO infoVO = BeanUtils.deepCopy(tdrepayRechargeLog, TdrepayRechargeInfoVO.class);
					if (infoVO != null) {
						infoVO.setBusinessTypeStr(BusinessTypeEnum.getName(infoVO.getBusinessType()));
						infoVO.setRepaymentTypeStr(RepaySourceEnum.getName(infoVO.getRepaySource()));
						switch (infoVO.getSettleType()) {
						case 0:
							infoVO.setPeriodTypeStr("正常还款");
							break;
						case 10:
							infoVO.setPeriodTypeStr("结清");
							break;
						case 11:
							infoVO.setPeriodTypeStr("结清");
							break;
						case 30:
							infoVO.setPeriodTypeStr("结清");
							break;
						case 25:
							infoVO.setPeriodTypeStr("展期确认");
							break;

						default:
							break;
						}
						if (StringUtil.notEmpty(infoVO.getPlatStatus())) {
							infoVO.setPlatformTypeStr(
									PlatformStatusTypeEnum.getName(Integer.valueOf(infoVO.getPlatStatus())));
						}
						infoVO.setProcessStatusStr(ProcessStatusTypeEnum.getName(infoVO.getProcessStatus()));
						infoVO.setFactRepayDateStr(DateUtil.formatDate(infoVO.getFactRepayDate()));
					}
					infoVOs.add(infoVO);
				}
			}

			List<List<Object>> rows = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(infoVOs)) {
				for (TdrepayRechargeInfoVO infoVO : infoVOs) {
					List<Object> row = new ArrayList<>();
					row.add(infoVO.getOrigBusinessId());
					row.add(infoVO.getBusinessTypeStr());
					row.add(infoVO.getRepaymentTypeStr());
					row.add(infoVO.getCustomerName());
					row.add(infoVO.getAfterId());
					row.add(infoVO.getFactRepayDateStr());
					row.add(infoVO.getCompanyName());
					row.add(infoVO.getPeriodTypeStr());
					row.add(infoVO.getResourceAmount() == null ? BigDecimal.valueOf(0) : infoVO.getResourceAmount());
					row.add(infoVO.getFactRepayAmount() == null ? BigDecimal.valueOf(0) : infoVO.getFactRepayAmount());
					row.add(infoVO.getRechargeAmount() == null ? BigDecimal.valueOf(0) : infoVO.getRechargeAmount());
					row.add(infoVO.getPlatformTypeStr());
					row.add(infoVO.getProcessStatusStr());
					row.add(infoVO.getRemark());
					rows.add(row);
				}
			}

			data.setRows(rows);

			// 生成本地
			ExcelUtils.exportExcel(response, "资金分发数据.xlsx", data);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}
}
