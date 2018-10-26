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
 * 检查没有核销的代扣记录,重新核销，每天每隔1小时执行
 */
@JobHandler(value = "AutoCheckCancleJobHandler")
@Component
public class AutoCheckCancleJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AutoCheckCancleJob.class);
	
	@Autowired
	WithholdingClient withholdingClient;
	
	


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@AutoCheckCancle@检查没有核销的代扣记录--开始[{},{}]");
		withholdingClient.autoCheckCancle();
		LOG.info("@AutoCheckCancle@检查没有核销的代扣记录--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("检查没有核销的代扣记录失败", e);
		return FAIL;
	}
		
	}
	
}
