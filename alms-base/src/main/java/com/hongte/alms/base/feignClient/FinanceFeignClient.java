package com.hongte.alms.base.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.common.result.Result;

@FeignClient(value = "alms-finance-service")
public interface FinanceFeignClient {

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/finance/recharge", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result recharge(@RequestBody ConfirmRepaymentReq req);
}
