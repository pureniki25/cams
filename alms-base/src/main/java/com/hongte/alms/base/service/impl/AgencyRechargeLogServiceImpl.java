package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.AgencyRechargeLogMapper;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.core.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 代充值操作记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-04
 */
@Service("AgencyRechargeLogService")
public class AgencyRechargeLogServiceImpl extends BaseServiceImpl<AgencyRechargeLogMapper, AgencyRechargeLog> implements AgencyRechargeLogService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AgencyRechargeLogServiceImpl.class);
	
	@Autowired
	private EipRemote eipRemote;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void callBackAgencyRecharge(String cmOrderNo, String orderStatus) {
		try {
			if (StringUtil.isEmpty(cmOrderNo) || StringUtil.isEmpty(orderStatus)) {
				throw new ServiceRuntimeException("订单号或处理状态不能为空");
			}
			
			AgencyRechargeLog log = this.selectOne(new EntityWrapper<AgencyRechargeLog>().eq("unique_id", cmOrderNo));
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
			if (result == null) {
				throw new ServiceRuntimeException("调用外联平台接口失败！");
			}
			if ("0000".equals(result.getReturnCode())) {
				String dataJson = JSONObject.toJSONString(result.getData());
				Map<String, Object> resultMap =  JSONObject.parseObject(dataJson, Map.class);
				String status = (String) resultMap.get("status");
				String orderId = (String) resultMap.get("orderId");
				String message = (String) resultMap.get("message");
				LOG.info("查询订单充值，状态：{}，订单号：{}，消息：{}", status, orderId, message);
				AgencyRechargeLog log = new AgencyRechargeLog();
				log.setHandleStatus(status);
				log.setUpdateTime(new Date());
				log.setUpdateUser(updateUser);
				this.update(log, new EntityWrapper<AgencyRechargeLog>().eq("unique_id", cmOrderNo));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}
}
