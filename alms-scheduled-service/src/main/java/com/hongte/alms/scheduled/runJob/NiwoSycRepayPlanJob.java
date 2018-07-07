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
import com.hongte.alms.scheduled.client.FinanceClient;
import com.hongte.alms.scheduled.client.WithholdingClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * 
 * @author czs
 * 每天每隔半小时同步你我金融还款计划
 */
@JobHandler(value = "NiwoSycRepayPlanJobHandler")
@Component
public class NiwoSycRepayPlanJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(NiwoSycRepayPlanJob.class);
	
	@Autowired
	FinanceClient financeClient;
	
	


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@NiwoSycRepayPlan@你我金融同步还款计划--开始[{},{}]");
		financeClient.sycRepayPlanBySearch();
		LOG.info("@NiwoSycRepayPlan@你我金融同步还款计划--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("你我金融同步还款计划", e);
		return FAIL;
	}
		
	}
	
}
