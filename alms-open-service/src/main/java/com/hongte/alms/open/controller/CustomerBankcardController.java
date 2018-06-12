package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.base.vo.module.api.RepayLogResp;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.open.service.WithHoldingXinDaiService;
import com.hongte.alms.open.vo.*;
import com.ht.ussp.util.DateUtil;

import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/CustomerBankcardController")
@Api(tags = "CustomerBankcardController", description = "获取客户银行卡信息", hidden = true)
@RefreshScope
public class CustomerBankcardController {
	private Logger logger = LoggerFactory.getLogger(CustomerBankcardController.class);
	
	  @Value(value="${bmApi.apiUrl:http://127.0.0.1}")
	private String apiUrl;
	

	@ApiOperation(value = "获取客户银行卡信息")
	@GetMapping("/getBankcardInfo")
	public List<BankCardInfo> getBankcardInfo(@RequestParam("identityCard") String identityCard) {
		try {

			RequestData requestData = new RequestData();
			JSONObject data = new JSONObject() ;
			data.put("identityCard", identityCard);
			requestData.setData(JSON.toJSONString(data));
			requestData.setMethodName("BankCard_GetBankCardByIdCard");
			String encryptStr = JSON.toJSONString(requestData);
			// 请求数据加密
			encryptStr = encryptPostData(encryptStr);
			WithHoldingXinDaiService withholdingxindaiService = Feign.builder().target(WithHoldingXinDaiService.class,
					apiUrl);
			
			String respStr = withholdingxindaiService.getBankcardInfo(encryptStr);
			// 返回数据解密
			ResponseData respData = getRespData(respStr);
			logger.info("代扣查询接口返回数据:"+respData.getData()+","+respData.getReturnMessage());
			List<BankCardInfo> bankCardInfos=JSON.parseArray(respData.getData(), BankCardInfo.class);
			
		      return bankCardInfos;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;

	}

	
	// 返回数据解密
		private ResponseData getRespData(String str) throws Exception {
			ResponseEncryptData resp = JSON.parseObject(str, ResponseEncryptData.class);
			String decryptStr = decryptRespData(resp);
			EncryptionResult result = JSON.parseObject(decryptStr, EncryptionResult.class);
			ResponseData respData = JSON.parseObject(result.getParam(), ResponseData.class);
			
			return respData;
			
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
	


}
