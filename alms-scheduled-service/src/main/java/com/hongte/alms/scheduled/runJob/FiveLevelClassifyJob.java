package com.hongte.alms.scheduled.runJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.FiveLevelClassifyService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "fiveLevelClassify")
@Component
public class FiveLevelClassifyJob extends IJobHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FiveLevelClassifyJob.class);

	@Autowired
	@Qualifier("FiveLevelClassifyService")
	private FiveLevelClassifyService fiveLevelClassifyService;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			LOG.info("五级分类设置开始");
			long start = System.currentTimeMillis();
			fiveLevelClassifyService.fiveLevelClassifySchedule();
			long end = System.currentTimeMillis();
			LOG.info("五级分类设置结束，耗时：{}", (end - start));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error("五级分类设置调度执行失败", e);
			return FAIL;
		}
	}

}
