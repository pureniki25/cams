package com.hongte.alms.base.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hongte.alms.common.result.Result;

@FeignClient("alms-finance-service")
public interface AlmsFinanceServiceFeignClient {
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/finance/queryActualPaymentByBusinessId", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.GET)
    Result queryActualPaymentByBusinessId(@RequestParam("businessId") String businessId);
}
