package com.hongte.alms.scheduled.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;


@Component
public class CalLateFeeJob {
	
	private static final Logger LOG = LoggerFactory.getLogger(CalLateFeeJob.class);
	
	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService  repaymentProjPlanService;
	
	
	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;
	
	
	@Autowired
	@Qualifier("RepaymentProjPlanListDetailService")
	RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

	@Scheduled(cron = "0 0 2 * * ?")
	public void run() {
		try {
		} catch (Exception e) {
			LOG.error("计算逾期费用失败", e);
		}
	}
	
}
