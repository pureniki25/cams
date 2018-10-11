package com.hongte.alms.scheduled.runJob.compliance;

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
import com.ht.ussp.core.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "updatePlatStatusJob")
@Component
public class UpdatePlatStatusJob extends IJobHandler {
	private static final Logger LOG = LoggerFactory.getLogger(UpdatePlatStatusJob.class);

	@Autowired
	private EipRemote eipRemote;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

	@Override
	public ReturnT<String> execute(String arg0) throws Exception {
		/*
		 * 查找资金分发表中platStatus字段不为 1：已还款 的数据
		 */
		LOG.info("更新platStatus字段 ---- 开始");
		long start = System.currentTimeMillis();
		List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService.selectList(
				new EntityWrapper<TdrepayRechargeLog>().where("IFNULL(plat_status, 0) != 1").where("is_valid = 1"));
		try {
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					updatePlatStatus(tdrepayRechargeLog);
				}
			}
			long end = System.currentTimeMillis();
			LOG.info("更新platStatus字段 ---- 结束，耗时：{}", (end - start));
			return SUCCESS;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return FAIL;
		}
	}

	private void updatePlatStatus(TdrepayRechargeLog tdrepayRechargeLog) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", tdrepayRechargeLog.getProjectId());
			LOG.info("更新platStatus字段，projectId：{}", tdrepayRechargeLog.getProjectId());
			Result result = eipRemote.queryProjectPayment(paramMap);
			LOG.info("更新platStatus字段，返回信息：{}", JSONObject.toJSONString(result));

			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;

			if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
					&& result.getData() != null) {
				JSONObject parseObject = (JSONObject) JSONObject.toJSON(result.getData());
				if (parseObject.get("projectPayments") != null) {
					tdProjectPaymentDTOs = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
					if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
						for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
							/*
							 * 匹配当期
							 */
							if (tdProjectPaymentDTO.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()) {
								switch (tdProjectPaymentDTO.getStatus()) {
								case 1:
									tdrepayRechargeLog.setPlatStatus("1");
									break;
								case 0:
									tdrepayRechargeLog.setPlatStatus("2");
									break;

								default:
									break;
								}
								tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
