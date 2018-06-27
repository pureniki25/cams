package com.hongte.alms.scheduled.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.scheduled.client.TdrepayRechargeFeignClient;

@Component
public class TdrepayRechargeSchedule {

	private static final Logger LOG = LoggerFactory.getLogger(TdrepayRechargeSchedule.class);

	@Autowired
	private TdrepayRechargeFeignClient tdrepayRechargeFeignClient;

	@Autowired
	@Qualifier("SysApiCallFailureRecordService")
	private SysApiCallFailureRecordService sysApiCallFailureRecordService;

	@Autowired
	private PlatformRepaymentFeignClient platformRepaymentFeignClient;

	@Scheduled(cron = "0 0/60 * * * ?")
	public void repayComplianceWithRequirements() {
		try {
			LOG.info("资产端对团贷网通用合规化还款流程");
			long start = System.currentTimeMillis();
			tdrepayRechargeFeignClient.repayComplianceWithRequirements();
			long end = System.currentTimeMillis();
			LOG.info("资产端对团贷网通用合规化还款流程，耗时：{}", (end - start));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 平台还款调度任务
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(cron = "0 0/60 * * * ?")
	public void platformRepayment() {
		LOG.info("平台还款调度任务");
		long start = System.currentTimeMillis();

		// 根据ref_id分组，查找调用失败，且次数小于5次的，且次数最大的一条数据
		List<SysApiCallFailureRecord> records = sysApiCallFailureRecordService
				.queryCallFailedDataByApiCode(Constant.INTERFACE_CODE_PLATREPAY_REPAYMENT);
		if (CollectionUtils.isNotEmpty(records)) {
			for (SysApiCallFailureRecord record : records) {
				if (record.getRetrySuccess() != null && record.getRetrySuccess().intValue() == 1) {
					continue;
				}
				Integer retryCount = record.getRetryCount();
				String apiParamPlaintext = record.getApiParamPlaintext();
				if (StringUtil.notEmpty(apiParamPlaintext)) {
					Map paramMap = JSONObject.parseObject(apiParamPlaintext, Map.class);
					Result result = null;
					try {
						result = platformRepaymentFeignClient.repayment(paramMap);
					} catch (Exception e) {
						record.setApiReturnInfo(e.getMessage());
						LOG.error("调用平台合规化还款接口失败，refId：{}", record.getRefId());
					}
					if (result != null && "1".equals(result.getCode())) {
						record.setRetrySuccess(1);
						record.setApiReturnInfo(JSONObject.toJSONString(result));
					} else {
						record.setRetrySuccess(0);
					}
					record.setRetryCount(++retryCount);
					record.setCreateUser("定时任务");
					record.setCraeteTime(new Date());
					sysApiCallFailureRecordService.insert(record);
				}
			}
		}

		long end = System.currentTimeMillis();
		LOG.info("平台还款调度任务，耗时：{}", (end - start));
	}
}
