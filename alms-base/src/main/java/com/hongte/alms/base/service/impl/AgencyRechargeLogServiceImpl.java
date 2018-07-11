package com.hongte.alms.base.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.FinanceFeignClient;
import com.hongte.alms.base.mapper.AgencyRechargeLogMapper;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.core.Result;

/**
 * <p>
 * 代充值操作记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-04
 */
@Service("AgencyRechargeLogService")
public class AgencyRechargeLogServiceImpl extends BaseServiceImpl<AgencyRechargeLogMapper, AgencyRechargeLog>
		implements AgencyRechargeLogService {

	private static final Logger LOG = LoggerFactory.getLogger(AgencyRechargeLogServiceImpl.class);

	@Autowired
	private EipRemote eipRemote;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("IssueSendOutsideLogService")
	private IssueSendOutsideLogService issueSendOutsideLogService;
	
	@Autowired
	private FinanceFeignClient financeFeignClient;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void callBackAgencyRecharge(String cmOrderNo, String orderStatus) {
		try {
			if (StringUtil.isEmpty(cmOrderNo) || StringUtil.isEmpty(orderStatus)) {
				throw new ServiceRuntimeException("订单号或处理状态不能为空");
			}

			AgencyRechargeLog log = this.selectOne(new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
			if (log == null) {
				throw new ServiceRuntimeException("没有找到对应的代充值记录");
			}
			log.setHandleStatus(orderStatus);
			log.setUpdateTime(new Date());
			log.setUpdateUser("eip回调");
			this.updateById(log);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void queryRechargeOrder(String oidPartner, String cmOrderNo, String updateUser) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("oidPartner", oidPartner);
			paramMap.put("cmOrderNo", cmOrderNo);
			Result result = eipRemote.queryRechargeOrder(paramMap);
			String resultJson = JSONObject.toJSONString(result);
			LOG.info("查询快捷代充值订单，cmOrderNo = {}；返回结果：{}", cmOrderNo, resultJson);
			if (result == null) {
				throw new ServiceRuntimeException("调用外联平台接口失败！");
			}

			String dataJson = JSONObject.toJSONString(result.getData());
			Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
			String status = (String) resultMap.get("status");
			String orderId = (String) resultMap.get("orderId");
			String message = (String) resultMap.get("message");

			LOG.info("查询订单充值，状态：{}，订单号：{}，消息：{}", status, orderId, message);

			if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
				AgencyRechargeLog log = new AgencyRechargeLog();
				log.setHandleStatus(status);
				log.setUpdateTime(new Date());
				log.setUpdateUser(updateUser);
				this.update(log, new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void queryDistributeFund() {
		List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
				.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("process_status", 1).eq("is_valid", 1));
		if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {

			Map<String, Object> paramMap = new HashMap<>();

			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				paramMap.put("oidPartner", tdrepayRechargeLog.getOidPartner());
				paramMap.put("batchId", tdrepayRechargeLog.getBatchId());
				paramMap.put("requestNo", tdrepayRechargeLog.getRequestNo());

				IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(loginUserInfoHelper.getUserId(), paramMap,
						Constant.INTERFACE_CODE_QUERY_DISTRIBUTE_FUND, Constant.INTERFACE_NAME_QUERY_DISTRIBUTE_FUND,
						Constant.SYSTEM_CODE_EIP, tdrepayRechargeLog.getProjectId());

				Result result = null;
				try {
					// 资金分发订单查询
					result = eipRemote.queryDistributeFund(paramMap);
				} catch (Exception e) {
					issueSendOutsideLog.setReturnJson(e.getMessage());
					LOG.error(e.getMessage(), e);
				}

				if (result != null) {
					issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
				}
				issueSendOutsideLogService.insert(issueSendOutsideLog);

				if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
						&& result.getData() != null) {
					String jsonString = JSONObject.toJSONString(result.getData());
					Map<String, Object> resultMap = JSONObject.parseObject(jsonString, Map.class);

					String handlerStatus = (String) resultMap.get("handlerStatus");

					if (StringUtil.notEmpty(handlerStatus)) {
						switch (handlerStatus) {
						case "0":
							tdrepayRechargeLog.setProcessStatus(1);
							break;
						case "1":
							tdrepayRechargeLog.setProcessStatus(1);
							break;
						case "2":
							tdrepayRechargeLog.setProcessStatus(2);
							break;
						case "3":
							tdrepayRechargeLog.setProcessStatus(3);
							break;
						case "4":
							tdrepayRechargeLog.setProcessStatus(3);
							break;
						case "500":
							tdrepayRechargeLog.setProcessStatus(3);
							break;

						default:
							break;
						}
						tdrepayRechargeLog.setUpdateTime(new Date());
						tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
					}
				}
			}
		}
	}

	/**
	 * 记录第三方日志
	 * 
	 * @param userId
	 * @param sendObject
	 * @param interfaceCode
	 * @param interfaceName
	 * @param systemCode
	 * @param sendKey
	 * @return
	 */
	private IssueSendOutsideLog issueSendOutsideLog(String userId, Object sendObject, String interfaceCode,
			String interfaceName, String systemCode, String sendKey) {
		IssueSendOutsideLog issueSendOutsideLog = new IssueSendOutsideLog();
		issueSendOutsideLog.setCreateTime(new Date());
		issueSendOutsideLog.setCreateUserId(userId);
		issueSendOutsideLog.setSendJson(JSONObject.toJSONString(sendObject));
		issueSendOutsideLog.setInterfacecode(interfaceCode);
		issueSendOutsideLog.setInterfacename(interfaceName);
		issueSendOutsideLog.setSystem(systemCode);
		issueSendOutsideLog.setSendKey(sendKey);

		return issueSendOutsideLog;
	}

	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void queryQuickRechargeOrder(List<AgencyRechargeLog> logs, String updateUser) {
		List<AgencyRechargeLog> failLogs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(logs)) {
			for (AgencyRechargeLog agencyRechargeLog : logs) {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("oidPartner", agencyRechargeLog.getoIdPartner());
				paramMap.put("cmOrderNo", agencyRechargeLog.getCmOrderNo());

				Result result = null;
				try {
					result = eipRemote.queryRechargeOrder(paramMap);
					String resultJson = JSONObject.toJSONString(result);
					LOG.info("查询快捷代充值订单，cmOrderNo = {}；返回结果：{}", agencyRechargeLog.getCmOrderNo(), resultJson);
				} catch (Exception e) {
					LOG.error("调用外联平台接口失败！cmOrderNo = {}", agencyRechargeLog.getCmOrderNo());
					LOG.error(e.getMessage(), e);
				}

				if (result == null) {
					LOG.info("调用外联平台接口失败！cmOrderNo = {}", agencyRechargeLog.getCmOrderNo());
					failLogs.add(agencyRechargeLog);
					continue;
				}

				String dataJson = JSONObject.toJSONString(result.getData());
				Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
				String status = (String) resultMap.get("status");
				String orderId = (String) resultMap.get("orderId");
				String message = (String) resultMap.get("message");

				LOG.info("查询订单充值，状态：{}，订单号：{}，消息：{}", status, orderId, message);

				if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
					agencyRechargeLog.setHandleStatus(status);
					agencyRechargeLog.setUpdateTime(new Date());
					agencyRechargeLog.setUpdateUser(updateUser);
				} else {
					failLogs.add(agencyRechargeLog);
				}
			}
			logs.removeAll(failLogs);
			
			if (CollectionUtils.isNotEmpty(logs)) {
				this.updateBatchById(logs);
				
				// 调用核销接口
				for (AgencyRechargeLog agencyRechargeLog : logs) {
					if (!"2".equals(agencyRechargeLog.getHandleStatus())) {
						continue;
					}
					ConfirmRepaymentReq req = new ConfirmRepaymentReq();
					req.setBusinessId(agencyRechargeLog.getOrigBusinessId());
					req.setAfterId(agencyRechargeLog.getAfterId());
					List<String> rechargeIds = new ArrayList<>();
					rechargeIds.add(agencyRechargeLog.getCmOrderNo());
					req.setRechargeIds(rechargeIds);
					req.setCallFlage(50);
					com.hongte.alms.common.result.Result result = financeFeignClient.recharge(req);
					if (result == null || !"1".equals(result.getCode())) {
						agencyRechargeLog.setHandleStatus("1");
						this.updateById(agencyRechargeLog);
					}
				}
			}
		}
	}
}
