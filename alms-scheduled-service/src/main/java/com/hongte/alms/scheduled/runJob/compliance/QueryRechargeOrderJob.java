package com.hongte.alms.scheduled.runJob.compliance;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "queryRechargeOrder")
@Component
public class QueryRechargeOrderJob extends IJobHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryRechargeOrderJob.class);

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		try {
			LOG.info("更新代充值处理状态开始");
			long start = System.currentTimeMillis();
			List<AgencyRechargeLog> logs = agencyRechargeLogService
					.selectList(new EntityWrapper<AgencyRechargeLog>().eq("handle_status", "1").eq("charge_type", "3"));
			if (CollectionUtils.isNotEmpty(logs)) {
				for (AgencyRechargeLog agencyRechargeLog : logs) {
					agencyRechargeLogService.queryRechargeOrder(agencyRechargeLog.getoIdPartner(),
							agencyRechargeLog.getCmOrderNo(), "贷后定时任务");
				}
			}
			long end = System.currentTimeMillis();
			LOG.info("更新代充值处理状态结束，耗时：{}", (end - start));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return FAIL;
		}
	}

}
