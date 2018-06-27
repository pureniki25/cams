package com.hongte.alms.scheduled.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

@FeignClient(value = "alms-platrepay-service")
public interface TdrepayRechargeFeignClient {

	/**
	 * 资产端对团贷网通用合规化还款流程
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/tdrepayRecharge/repayComplianceWithRequirements", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result repayComplianceWithRequirements();
}
