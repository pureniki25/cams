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
@JobHandler(value = "NiwoSendFailMsgJobHandler")
@Component
public class NiwoSendFailMsgJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(NiwoSendFailMsgJob.class);
	
	@Autowired
	FinanceClient financeClient;
	
	


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@NiwoSendFailMsg@你我金融发送扣款失败短信--开始[{},{}]");
		financeClient.sendFailMsg();
		LOG.info("@NiwoSendFailMsg@你我金融发送扣款失败短信--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("你我金融同步还款计划", e);
		return FAIL;
	}
		
	}
	
}
