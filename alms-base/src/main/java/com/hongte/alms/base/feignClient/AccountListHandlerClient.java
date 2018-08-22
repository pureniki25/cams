package com.hongte.alms.base.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.vo.cams.CancelBizAccountListCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.ht.ussp.core.Result;

@FeignClient(value = "cams-account-service")
public interface AccountListHandlerClient {

	/**
	 * 批量新增账户流水
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/accountList/batchFlow", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result<Object> addBatchFlow(CreateBatchFlowCommand command);

	/**
	 * 撤销业务流水
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/accountList/cancelBiz", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result<Object> cancelFlow(CancelBizAccountListCommand command);

}
