package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongte.alms.scheduled.client.SyncDaihouClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
@JobHandler(value = "syncDaihouJob")
@Component
public class SyncDaihouJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(SyncDaihouJob.class);

	@Autowired
	SyncDaihouClient syncDaihouClient;
	
	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		XxlJobLogger.log("@SyncDaihouJob@同步贷后数据--开始[{},{}]");
		LOG.info("@SyncDaihouJob@同步贷后数据--开始[{},{}]");
		syncDaihouClient.SynchDaihouData();
		LOG.info("@SyncDaihouJob@同步贷后数据--结束[{}]");
		XxlJobLogger.log("@SyncDaihouJob@同步贷后数据--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("同步贷后数据失败", e);
		return FAIL;
	}
		
	}
	
}
