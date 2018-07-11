package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hongte.alms.scheduled.client.WithholdingClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * 
 * @author czs
 * 同步代扣结果定时任务，每天每隔10分钟执行
 */
@JobHandler(value = "syncRechargeResultJobHandler")
@Component
public class SyncRechargeResultJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(SyncRechargeResultJob.class);
	
	@Autowired
	WithholdingClient withholdingClient;
	
	


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@AutoRecharge@同步代扣结果--开始[{},{}]");
		withholdingClient.searchRepayResult();
		LOG.info("@AutoRecharge@同步代扣结果--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("同步代扣结果失败", e);
		return FAIL;
	}
		
	}
	
}
