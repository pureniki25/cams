package com.hongte.alms.finance.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.enums.PaymentPlatformEnums;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.service.WithholdingFlowRecordService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowyYbRecordVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.storage.StorageService;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/customer")
@Api(tags = "BfWithholdFlowController", description = "宝付代扣流水", hidden = true)
public class BfWithholdFlowController {
    private  final Logger logger = LoggerFactory.getLogger(CustomerRepayFlowController.class);

    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService;

    @Autowired
    @Qualifier("WithholdingFlowRecordService")
    private WithholdingFlowRecordService withholdingFlowRecordService;
    @Autowired
//  @Qualifier("storageService")
    private StorageService storageService;

    /**
     * 宝付代扣流水列表
     *
     * @param withholdFlowReq
     * @return
     */
    @ApiOperation(value = "宝付代扣流水列表")
    @GetMapping("/getBfWithholdFlowPageList")
    @ResponseBody
    // public PageResult<List<BfWithholdFlowVo>>
    // getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
    public PageResult<List<WithholdingFlowRecordVo>> getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
    	logger.info("====>>>>>宝付代扣流水列表[{}]", JSON.toJSONString(withholdFlowReq));
        try {
            // Page<BfWithholdFlowVo> pages =
            // withholdingRepaymentLogService.getBfWithholdFlowPageList(withholdFlowReq);

//            EntityWrapper<WithholdingFlowRecord> ew = new EntityWrapper<>();
//            ew.eq("withholding_platform", PlatformEnum.BF_FORM.getValue());
//            if (StringUtils.isNotBlank(withholdFlowReq.getStartTime()))
//                ew.ge("liquidation_date", withholdFlowReq.getStartTime());
//            if (StringUtils.isNotBlank(withholdFlowReq.getEndTime()))
//                ew.le("liquidation_date", withholdFlowReq.getEndTime());
//
//            ew.orderBy("liquidation_date", false);
        	withholdFlowReq.setWithholdingPlatform(PlatformEnum.BF_FORM.getPlatformId());
            // 查分页数据
            Page<WithholdingFlowRecordVo> pages=withholdingFlowRecordService.selectFlowBfRecordPage(withholdFlowReq);

            return PageResult.success(pages.getRecords(), pages.getTotal());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }
    
    @ApiOperation(value = "宝付流水 存储Excel  ")
    @PostMapping("/saveExcel")
    public Result saveExcel(HttpServletRequest request, HttpServletResponse response,@RequestBody WithholdFlowReq req) throws Exception {

        logger.info("@宝付流水--存储Excel--开始[{}]" , req);
    	req.setWithholdingPlatform(req.getWithholdingPlatform());
    	req.setLimit(10000);
        // 查分页数据
    	 Page pages=null;
    	   Workbook workbook =null;
    	if(req.getWithholdingPlatform()==PlatformEnum.BF_FORM.getPlatformId()) {
    		pages=withholdingFlowRecordService.selectFlowBfRecordPage(req);
    		workbook=ExcelExportUtil.exportExcel(new ExportParams(), WithholdingFlowRecordVo.class, pages.getRecords());

    	}
    	if(req.getWithholdingPlatform()==PlatformEnum.YB_FORM.getPlatformId()) {
    		pages=withholdingFlowRecordService.selectFlowYbRecordPage(req);
    		workbook=ExcelExportUtil.exportExcel(new ExportParams(), WithholdingFlowyYbRecordVo.class, pages.getRecords());
    	}
 
        String fileName =  UUID.randomUUID().toString()+".xls";
        System.out.println(fileName);


        Map<String,String> retMap = storageService.storageExcelWorkBook(workbook,fileName);
        String docUrl=retMap.get("docUrl");

        retMap.put("errorInfo","");
        retMap.put("sucFlage","true");

        if(retMap.get("sucFlage").equals("true")){
            logger.info("@宝付流水---结束[{}]" , fileName);
            return  Result.success(docUrl);
        }else{
            logger.info("@宝付流水--存储Excel---失败[{}]" ,  retMap.get("errorInfo"));
            return Result.error("500", retMap.get("errorInfo"));
        }
//        workbook.write(response.getOutputStream());


    }

    /**
     * 查询宝付汇总数据
     */
    @ApiOperation("查询宝付汇总数据")
    @RequestMapping("/queryBfWithholdFlowSummary")
    @ResponseBody
    public Result<WithholdingFlowRecordSummaryVo> querySummary(WithholdFlowReq withholdFlowReq) {
        try {
            // 查汇总数据
            withholdFlowReq.setWithholdingPlatform((Integer) PlatformEnum.BF_FORM.getValue());
            WithholdingFlowRecordSummaryVo summaryVo = withholdingFlowRecordService.querySummary(withholdFlowReq);
            return Result.success(summaryVo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
