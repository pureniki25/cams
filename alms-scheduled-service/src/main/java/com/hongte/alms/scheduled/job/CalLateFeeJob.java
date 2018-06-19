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
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
@JobHandler(value = "calLateFeeJobHandler")
@Component
public class CalLateFeeJob extends IJobHandler {
	
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


	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
		
		LOG.info("@calLateFee@计算滞纳金--开始[{},{}]");
		repaymentProjPlanListService.calLateFee();
		LOG.info("@calLateFee@计算滞纳金--结束[{}]");
		return SUCCESS;
	} catch (Exception e) {
		LOG.error("计算逾期费用失败", e);
		return FAIL;
	}
		
	}
	
}
