package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.scheduled.client.WithholdingClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * 
 * @author czs
 *自动代扣任务，每天下午1点和晚上9点执行
 */

@JobHandler(value = "autoRechargeJobHandler")
@Component
public class AutoRechargeJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AutoRechargeJob.class);
	
	@Autowired
	WithholdingClient withholdingClient;
	
	


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@AutoRecharge@自动代扣--开始[{},{}]");
		withholdingClient.autoRepay();
		LOG.info("@AutoRecharge@自动代扣--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("自动代扣失败", e);
		return FAIL;
	}
		
	}
	
}
