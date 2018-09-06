package com.hongte.alms.base.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.vo.cams.CamsMessage;
import com.ht.ussp.core.Result;

@FeignClient(value = "cams-messaging")
public interface AccountListHandlerMsgClient {

	/**
	 * 推送流水
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/message/", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result<Object> addMessageFlow(CamsMessage camsMessage);

}
