package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongte.alms.scheduled.client.UserInfoAndPermissionSynchClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
@JobHandler(value = "syncUserInfoAndPermissionJob")
@Component
public class SyncUserInfoAndPermissionJob extends IJobHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(SyncUserInfoAndPermissionJob.class);

	@Autowired
	UserInfoAndPermissionSynchClient userInfoAndPermissionSynchClient;
	
	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
	try {
		XxlJobLogger.log("@SyncDaihouJob@同步贷后用户数据--开始[{},{}]");
		LOG.info("@SyncDaihouJob@同步贷后用户数据--开始[{},{}]");
		userInfoAndPermissionSynchClient.getUserInfoForApp();
		LOG.info("@SyncDaihouJob@同步贷后用户数据--结束[{}]");
		XxlJobLogger.log("@SyncDaihouJob@同步贷后用户数据--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("同步贷后用户数据", e);
		return FAIL;
	}
		
	}
	
}
