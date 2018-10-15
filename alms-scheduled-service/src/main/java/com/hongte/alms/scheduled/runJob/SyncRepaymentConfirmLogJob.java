/**
 * 
 */
package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongte.alms.scheduled.client.RepaymentConfirmLogSynchClient;
import com.sun.jersey.api.client.Client;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * @author 王继光
 * 2018年10月15日 下午8:03:37
 */
@JobHandler(value = "syncRepaymentConfirmLogJob")
@Component
public class SyncRepaymentConfirmLogJob extends IJobHandler{

	private static final Logger LOG = LoggerFactory.getLogger(SyncRepaymentConfirmLogJob.class);
	
	@Autowired
	private RepaymentConfirmLogSynchClient client ;
	
	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			XxlJobLogger.log("@syncRepaymentConfirmLogJob@同步RepaymentConfirmLogSynch数据--开始[{},{}]");
			LOG.info("@syncRepaymentConfirmLogJob@同步RepaymentConfirmLogSynch数据--开始[{},{}]");
			client.synch();
			LOG.info("@syncRepaymentConfirmLogJob@同步RepaymentConfirmLogSynch数据--结束[{}]");
			XxlJobLogger.log("@SyncDaihouJob@同步RepaymentConfirmLogSynch数据--结束[{}]");
			return SUCCESS;
		} catch (Exception e) {
			LOG.error("同步RepaymentConfirmLogSynch数据失败", e);
			return FAIL;
		}
	}

}
