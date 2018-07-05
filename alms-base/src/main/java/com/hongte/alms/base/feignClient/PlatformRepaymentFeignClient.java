package com.hongte.alms.base.feignClient;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.dto.PlatformRepaymentDto;
import com.hongte.alms.base.dto.PlatformRepaymentReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

@FeignClient(value = "alms-platrepay-service")
public interface PlatformRepaymentFeignClient {

	/**
	 * 平台合规化还款接口
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/platformRepayment/repayment", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result repayment(@RequestBody Map<String, Object> paramMap);

	/**
	 * 查询资金分发状态接口
	 *
	 * @return
	 */
	@RequestMapping(value = "/tdrepayRecharge/queryTdrepayRechargeRecord", headers = { "app=ALMS","content-type=application/json" })
	public Result<List<PlatformRepaymentDto>> queryTdrepayRechargeRecord(@RequestBody PlatformRepaymentReq platformRepaymentReq);

}
