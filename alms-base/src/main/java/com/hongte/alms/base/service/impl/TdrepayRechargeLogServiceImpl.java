package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.TdrepayRechargeLogMapper;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 团贷网合规化还款标的充值记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-06
 */
@Service("TdrepayRechargeLogService")
public class TdrepayRechargeLogServiceImpl extends BaseServiceImpl<TdrepayRechargeLogMapper, TdrepayRechargeLog>
		implements TdrepayRechargeLogService {

	private static final Logger LOG = LoggerFactory.getLogger(TdrepayRechargeLogServiceImpl.class);
	
	@Autowired
	private TdrepayRechargeLogMapper tdrepayRechargeLogMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void callBackDistributeFund(String oIdPartner, String batchId, String requestNo, String orderStatus) {
		try {
			LOG.info("资金分发回调接口开始：oIdPartner：{}，batchId：{}，requestNo：{}，orderStatus：{}", oIdPartner, batchId, requestNo,
					orderStatus);
			if (StringUtil.isEmpty(requestNo)) {
				List<TdrepayRechargeLog> tdrepayRechargeLogs = this.selectList(
						new EntityWrapper<TdrepayRechargeLog>().eq("batch_id", batchId).eq("oid_partner", oIdPartner));
				handleCallBackResult(orderStatus, tdrepayRechargeLogs);
			} else {
				List<TdrepayRechargeLog> tdrepayRechargeLogs = this.selectList(new EntityWrapper<TdrepayRechargeLog>()
						.eq("batch_id", batchId).eq("oid_partner", oIdPartner).eq("request_no", requestNo));
				handleCallBackResult(orderStatus, tdrepayRechargeLogs);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	private void handleCallBackResult(String orderStatus, List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
			throw new ServiceRuntimeException("没有找到相关数据");
		} else {
			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				handleCallBackResultOrderStatus(tdrepayRechargeLog, orderStatus);
			}
			this.updateBatchById(tdrepayRechargeLogs);
		}
	}

	/**
	 * 团贷网回调结果状态：0：未处理1：处理中2：处理完成3：处理失败4：确认请求处理失败 500：用户未开通存管
	 * 
	 * @param tdrepayRechargeLog
	 * @param orderStatus
	 * @return
	 */
	private TdrepayRechargeLog handleCallBackResultOrderStatus(TdrepayRechargeLog tdrepayRechargeLog,
			String orderStatus) {
		int status = 1;
		switch (orderStatus) {
		case "0":
			status = 1;
			break;
		case "1":
			status = 1;
			break;
		case "2":
			status = 2;
			break;
		case "3":
			status = 3;
			break;
		case "4":
			status = 3;
			tdrepayRechargeLog.setRemark("确认请求处理失败 500：用户未开通存管");
			break;
		default:
			status = 3;
			break;
		}
		tdrepayRechargeLog.setProcessStatus(status);
		return tdrepayRechargeLog;
	}
	
	@Override
	public List<Map<String, Object>> queryTdrepayRechargeRecord(String projectId, String confirmLogId) {
		if (StringUtil.isEmpty(projectId)) {
			return Collections.emptyList();
		}
		return tdrepayRechargeLogMapper.queryTdrepayRechargeRecord(projectId, confirmLogId);
	}

}
