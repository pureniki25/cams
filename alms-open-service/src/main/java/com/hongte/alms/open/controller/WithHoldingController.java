package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.base.vo.module.api.RepayLogResp;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.open.service.WithHoldingXinDaiService;
import com.hongte.alms.open.vo.RequestData;
import com.hongte.alms.open.vo.ResponseData;
import com.hongte.alms.open.vo.ResponseEncryptData;
import com.hongte.alms.open.vo.SearchRepayRecordReq;
import com.hongte.alms.open.vo.WithHoldingInfo;
import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WithHoldingController")
@Api(tags = "WithHoldingController", description = "执行代扣", hidden = true)

public class WithHoldingController {
	private Logger logger = LoggerFactory.getLogger(WithHoldingController.class);
	@Autowired
	@Qualifier("WithholdingRecordLogService")
	WithholdingRecordLogService WithholdingRecordLogService;

	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	
	@Autowired	
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanDetailListService;
	
	
	@Autowired
	@Qualifier("CollectionStatusService")
	CollectionStatusService collectionStatusService;
	
	  @Value(value="${bmApi.apiUrl}")
	private String apiUrl;
	
	@ApiOperation(value = "执行代扣")
	@GetMapping("/withholding")
	public Result<String> withholding(@RequestParam("originalBusinessId") String originalBusinessId,
			@RequestParam("afterId") String afterId, @RequestParam("total") String total,
			@RequestParam("planOverDueMoney") String planOverDueMoney, @RequestParam("platformId") String platformId,
			@RequestParam("type") String type, @RequestParam("nowdate") String nowdate

	) {
		try {
          
			WithHoldingInfo info = new WithHoldingInfo();
			RequestData requestData = new RequestData();
			info.setBusinessId(originalBusinessId);
			info.setAfterId(afterId);
			info.setOverduemoney(planOverDueMoney);
			info.setFactDate(nowdate);
			info.setRepayplatform(Integer.valueOf(platformId));
			info.setType(Integer.valueOf(type));

			requestData.setData(JSON.toJSONString(info));
			requestData.setMethodName("AfterLoanRepayment_SubmitAutoRepay");
			String encryptStr = JSON.toJSONString(requestData);
			// 请求数据加密
			encryptStr = encryptPostData(encryptStr);
			/* http://172.16.200.104:8084/apites是信贷接口域名，这里本机配置的，需要配置成自己的 */
			logger.info(getUrl());
			WithHoldingXinDaiService withholdingxindaiService = Feign.builder().target(WithHoldingXinDaiService.class,
					"http://10.110.1.21:8018");
			String respStr = withholdingxindaiService.withholding(encryptStr);
			// 返回数据解密
			ResponseData respData = getRespData(respStr);
		
			
			if ("1".equals(respData.getReturnCode())) {// 处理中
				
				Boolean flag=withHoldingInsertRecord(WithholdingRecordLogService, afterId, originalBusinessId, planOverDueMoney);
				if(flag) {
					return Result.success("代扣正在处理中,请稍后查看代扣结果");
				}else {
					return Result.success("代扣正在处理中,请稍后查看代扣结果");
					
				}
		
			} else {
				return Result.success(respData.getReturnMessage());
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Result.error("代扣出现异常", ex.getMessage());
		}

	}
	
	
	
	
	@ApiOperation(value = "查询代扣记录")
	@GetMapping("/searchRepayLog")
	public PageResult<List<RepayLogResp>>searchRepayLog(@RequestParam("originalBusinessId") String originalBusinessId) {
		try {

			RequestData requestData = new RequestData();
			SearchRepayRecordReq req=new SearchRepayRecordReq();
			req.setBusinessId(originalBusinessId);
			requestData.setData(JSON.toJSONString(req));
			requestData.setMethodName("AfterLoanRepayment_SearRepayLog");
			String encryptStr = JSON.toJSONString(requestData);
			// 请求数据加密
			encryptStr = encryptPostData(encryptStr);
			WithHoldingXinDaiService withholdingxindaiService = Feign.builder().target(WithHoldingXinDaiService.class,
					"http://10.110.1.21:8018");
			
			String respStr = withholdingxindaiService.searchRepayRecord(encryptStr);
			// 返回数据解密
			ResponseData respData = getRespData(respStr);
			List<RepayLogResp> list=JSON.parseArray(respData.getData(), RepayLogResp.class);
			
		      return PageResult.success(list, list.size());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return PageResult.error(9999, "查询代扣记录出错");
		}

	}

//	@ApiOperation(value = "代扣结果回调", response = Result.class)
//	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input", response = Result.class) })
//	@PostMapping("/withHoldingResult/notice")
//	@ResponseBody
//	public Result withHoldingResult(@ApiParam(value="jsonData",required=true)@RequestBody String  jsonData) {
//		try {
//			
//			repaymentBizPlanService.repayResultUpdateRecord(data);
//			
//			return Result.error("0000", "回调成功");
//		} catch (Exception ex) {
//			return Result.error("9999", "回调出错");
//		}
//
//	}
	// 返回数据解密
	private ResponseData getRespData(String str) throws Exception {
		ResponseEncryptData resp = JSON.parseObject(str, ResponseEncryptData.class);
		String decryptStr = decryptRespData(resp);
		EncryptionResult result = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(result.getParam(), ResponseData.class);
		
		return respData;
		
	}
	

	// 代扣记录日志入库
	private Boolean withHoldingInsertRecord(WithholdingRecordLogService service, String afterId, String originalBusinessId,
			String planOverDueMoney) {
		List<WithholdingRecordLog> loglist=service.selectWithholdingRecordLog(originalBusinessId, afterId);
		//已经存在记录
		if(loglist.size()>0) {
			return false;
		}else {
			WithholdingRecordLog log = new WithholdingRecordLog();
			log.setOriginalBusinessId(originalBusinessId);
			log.setAfterId(afterId);
			log.setCreateTime(new Date());
			log.setCurrentAmount(BigDecimal.valueOf(Double.valueOf(planOverDueMoney)));
			log.setRepayStatus(2);
			log.setUpdateTime(new Date());
			service.insert(log);
			return true;
		}
	}


	
	

	
	// 加密
	private String encryptPostData(String str) throws Exception {

		DESC desc = new DESC();
		str = desc.Encryption(str);

		return str;
	}

	// 解密
	private String decryptRespData(ResponseEncryptData data) throws Exception {

		DESC desc = new DESC();
		String str = desc.Decode(data.getA(), data.getUUId());
		return str;
	}
	
	private String getUrl() {
		String apiUrl2="http://10.110.1.21:8085/api/ltgproject/dod/";
		int i=apiUrl2.indexOf("api/");
		String url=apiUrl2.substring(0,i-1);
		return url;
		
	}

}
