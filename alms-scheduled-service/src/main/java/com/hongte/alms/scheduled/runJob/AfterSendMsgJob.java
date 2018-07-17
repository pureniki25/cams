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
 * 贷后业务，发送还款提醒短信和消息推送，每天定时发送
 */
@JobHandler(value = "AfterSendMsgJobHandler")
@Component
public class AfterSendMsgJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AfterSendMsgJob.class);
	
	@Autowired
	WithholdingClient withholdingClient;


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@AfterSendMsg@贷后业务，发送还款提醒短信和消息推送--开始[{},{}]");
		withholdingClient.sendRemindMsg();
		LOG.info("@AfterSendMsg@贷后业务，发送还款提醒短信和消息推送-结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("贷后业务，发送还款提醒短信和消息推送出错", e);
		return FAIL;
	}
		
	}
	
}
