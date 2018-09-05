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
	private EipRemote eipRemote;

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
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		try {
			/*
			 * 查找资金分发表中platStatus字段不为 1：已还款 的数据
			 */
			LOG.info("更新platStatus字段 ---- 开始");
			long start = System.currentTimeMillis();
			List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
					.selectList(new EntityWrapper<TdrepayRechargeLog>().ne("plat_status", 1).eq("is_valid", 1));
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				Map<String, Object> paramMap = new HashMap<>();
				List<TdrepayRechargeLog> list = new ArrayList<>();
				
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					paramMap.put("projectId", tdrepayRechargeLog.getProjectId());
					Result result = eipRemote.queryProjectPayment(paramMap);
					
					updatePlatStatus(tdrepayRechargeLog, result, list);
				}
				
				if (!list.isEmpty()) {
					tdrepayRechargeLogService.updateBatchById(list);
				}
			}
			long end = System.currentTimeMillis();
			LOG.info("更新platStatus字段 ---- 结束，耗时：{}", (end - start));
			return SUCCESS;
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return FAIL;
		}

	}

	private void updatePlatStatus(TdrepayRechargeLog tdrepayRechargeLog, Result result, List<TdrepayRechargeLog> list) {
		
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
							break;
						}
					}
				}
			}
		}
			
	}

}
