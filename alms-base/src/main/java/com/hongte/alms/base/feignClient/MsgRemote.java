package com.hongte.alms.base.feignClient;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ht.ussp.core.Result;

/**
 * @author:陈泽圣
 * @date: 2018/3/12
 */
@FeignClient(value = "msg-send-app")
public interface MsgRemote {

	@RequestMapping(value = "/sendRequest/sendRequest", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result sendRequest(@RequestBody String jason);

	

}
