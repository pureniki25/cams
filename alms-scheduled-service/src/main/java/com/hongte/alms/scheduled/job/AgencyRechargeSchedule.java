package com.hongte.alms.scheduled.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.service.AgencyRechargeLogService;

@Component
public class AgencyRechargeSchedule {
	
	private static final Logger LOG = LoggerFactory.getLogger(AgencyRechargeSchedule.class);

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;
	
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void run() {
		try {
			LOG.info("更新代充值处理状态开始");
			long start = System.currentTimeMillis();
			List<AgencyRechargeLog> logs = agencyRechargeLogService.selectList(new EntityWrapper<AgencyRechargeLog>().eq("handle_status", "1"));
			if (CollectionUtils.isNotEmpty(logs)) {
				for (AgencyRechargeLog agencyRechargeLog : logs) {
					agencyRechargeLogService.queryRechargeOrder(agencyRechargeLog.getoIdPartner(), agencyRechargeLog.getCmOrderNo(), "贷后定时任务");
				}
			}
			long end = System.currentTimeMillis();
			LOG.info("更新代充值处理状态结束，耗时：{}", (end - start));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
