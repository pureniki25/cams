/**
 * 
 */
package com.hongte.alms.scheduled.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

/**
 * @author 王继光
 * 2018年10月15日 下午7:55:15
 */
@FeignClient(value="alms-finance-service")
public interface RepaymentConfirmLogSynchClient {

	@RequestMapping(value = "/factRepay/synchForScheduled",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
	Result synch();
}
