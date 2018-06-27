package com.hongte.alms.open.feignClient;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

@FeignClient(value = "alms-platrepay-service")
public interface RechargeRemoteApi {

	/**
	 * 代充值回调接口
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/recharge/callBackAgencyRecharge", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result callBackAgencyRecharge(Map<String, Object> paramMap);

	/**
	 * 资金分发回调接口
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/recharge/callBackDistributeFund", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result callBackDistributeFund(Map<String, Object> paramMap);

}
