package com.hongte.alms.scheduled.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.TdrepayRechargeService;

@Component
public class TdrepayRechargeSchedule {
	
	private static final Logger LOG = LoggerFactory.getLogger(TdrepayRechargeSchedule.class);

	@Autowired
	@Qualifier("TdrepayRechargeService")
	private TdrepayRechargeService tdrepayRechargeService;
	
	@Scheduled(cron = "0 0/60 * * * ?")
	public void repayComplianceWithRequirements() {
		try {
			LOG.info("资产端对团贷网通用合规化还款流程");
			long start = System.currentTimeMillis();
			tdrepayRechargeService.repayComplianceWithRequirements();
			long end = System.currentTimeMillis();
			LOG.info("资产端对团贷网通用合规化还款流程，耗时：{}", (end - start));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
