package com.hongte.alms.scheduled.runJob.compliance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "handleRunningData")
@Component
public class HandleRunningDataJob extends IJobHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HandleRunningDataJob.class);
	
	@Autowired
	private PlatformRepaymentFeignClient platformRepaymentFeignClient;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			LOG.info("处理合规化还款处理中的数据------开始");
			long start = System.currentTimeMillis();

			platformRepaymentFeignClient.handleRunningData();

			long end = System.currentTimeMillis();
			LOG.info("处理合规化还款处理中的数据------结束，耗时：{}", (end - start));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return FAIL;
		}
		
	}

}
