package com.hongte.alms.scheduled.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.FiveLevelClassifyService;

@Component
public class FiveLevelClassifySchedule {
	
	private static final Logger LOG = LoggerFactory.getLogger(FiveLevelClassifySchedule.class);
	
	@Autowired
	@Qualifier("FiveLevelClassifyService")
	private FiveLevelClassifyService fiveLevelClassifyService;
	
	@Scheduled(cron = "0 0 2 * * ?")
	public void run() {
		try {
			fiveLevelClassifyService.fiveLevelClassifySchedule();
		} catch (Exception e) {
			LOG.error("五级分类设置调度执行失败", e);
		}
	}
	
}
