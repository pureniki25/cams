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
import com.hongte.alms.scheduled.client.WithholdingClient;


@Component
public class SearchRepayResult {
	
	private static final Logger LOG = LoggerFactory.getLogger(SearchRepayResult.class);
	

	@Autowired
	WithholdingClient withholdingClient;

	@Scheduled(cron = "0 0 2 * * ?")
	public void run() {
		try {
			
			LOG.info("@searchRepayResult@查询代扣结果--开始[{},{}]");
			withholdingClient.searchRepayResult();
			LOG.info("@searchRepayResult@查询代扣结果--结束[{}]");
		} catch (Exception e) {
			LOG.error("查询代扣结果出错", e);
		}
	}
	
}
