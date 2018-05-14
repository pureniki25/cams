package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.open.service.WithHoldingXinDaiService;
import com.hongte.alms.open.vo.RequestData;
import com.hongte.alms.open.vo.ResponseData;
import com.hongte.alms.open.vo.ResponseEncryptData;
import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author 陈泽圣
 *
 */
@RestController
@RequestMapping("/ContractDateController")
@Api(tags = "ContractDateController", description = "获取合同日期", hidden = true)
@RefreshScope
public class ContractDateController {
	private Logger logger = LoggerFactory.getLogger(ContractDateController.class);

	  @Value(value="${bmApi.apiUrl:http://127.0.0.1}")
	private String apiUrl;
	
	@ApiOperation(value = "获取合同日期")
	@GetMapping("/getContractDate")
	public Result<String> getContractDate(@RequestParam("originalBusinessId") String originalBusinessId
	) {
		try {
          
			RequestData requestData = new RequestData();
			JSONObject data = new JSONObject() ;
			data.put("BusinessId", originalBusinessId);
			requestData.setData(data.toJSONString());
			requestData.setMethodName("BusinessDerate_GetContractSignDate");
			String encryptStr = JSON.toJSONString(requestData);
			// 请求数据加密
			encryptStr = encryptPostData(encryptStr);
			WithHoldingXinDaiService withholdingxindaiService = Feign.builder().target(WithHoldingXinDaiService.class,
					apiUrl);
			String respStr = withholdingxindaiService.getContractDate(encryptStr);
			// 返回数据解密
			ResponseData respData = getRespData(respStr);
			logger.info("接口返回数据:"+respData.getData());
			
			

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Result.error("获取合同日期异常", ex.getMessage());
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
