package com.hongte.alms.scheduled.runJob.compliance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.TdProjectPaymentDTO;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.scheduled.client.TdrepayRechargeFeignClient;
import com.ht.ussp.core.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "repayComplianceWithRequirements")
@Component
public class RepayComplianceWithRequirementsJob extends IJobHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RepayComplianceWithRequirementsJob.class);

	@Autowired
	private TdrepayRechargeFeignClient tdrepayRechargeFeignClient;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

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
