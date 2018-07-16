package com.hongte.alms.scheduled.runJob.compliance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongte.alms.scheduled.client.TdrepayRechargeFeignClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "repayComplianceWithRequirements")
@Component
public class RepayComplianceWithRequirementsJob extends IJobHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RepayComplianceWithRequirementsJob.class);

	@Autowired
	private TdrepayRechargeFeignClient tdrepayRechargeFeignClient;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			LOG.info("资产端对团贷网通用合规化还款流程");
			long start = System.currentTimeMillis();
			tdrepayRechargeFeignClient.repayComplianceWithRequirements();
			long end = System.currentTimeMillis();
			LOG.info("资产端对团贷网通用合规化还款流程，耗时：{}", (end - start));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return FAIL;
		}
	}

}
