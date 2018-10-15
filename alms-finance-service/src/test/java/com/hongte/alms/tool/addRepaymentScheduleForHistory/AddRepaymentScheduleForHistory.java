/**
 * 
 */
package com.hongte.alms.tool.addRepaymentScheduleForHistory;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ht.ussp.core.Result;

/**
 * @author 王继光
 * 2018年9月25日 上午10:13:46
 */
@FeignClient(name="AddRepaymentScheduleForHistory", url="http://172.16.200.112:30406" ,qualifier="api")
public interface AddRepaymentScheduleForHistory {
	@RequestMapping(value = "/eip/td/repayment/addRepaymentScheduleForHistory",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
	public Result addRepaymentScheduleForHistory(Req req) ;
	
	
	@RequestMapping(value = "/eip/td/repayment/queryProjectPayment",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
	public Result queryProjectPayment(Map<String, String> param);
	
	
	@RequestMapping(value = "/eip/td/repayment/queryRepaymentSchedule",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
	public Result queryRepaymentSchedule(Map<String, String> param);
}
