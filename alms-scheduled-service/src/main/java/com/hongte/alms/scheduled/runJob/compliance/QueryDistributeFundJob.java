package com.hongte.alms.scheduled.runJob.compliance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "queryDistributeFund")
@Component
public class QueryDistributeFundJob extends IJobHandler {
	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributeFundJob.class);

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			LOG.info("资金分发订单查询开始");
			long start = System.currentTimeMillis();
			agencyRechargeLogService.queryDistributeFund();
			long end = System.currentTimeMillis();
			LOG.info("资金分发订单查询结束，耗时：{}", (end - start));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return FAIL;
		}
	}

}
