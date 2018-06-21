package com.hongte.alms.open.feignClient;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "alms-finance-service")
public interface NiwoRepayPlanRemoteApi {


	/**
	 * 你我金融同步还款计划回调
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/niwoController/sycRepayPlan", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	void sycRepayPlan(String orderNo);

}
