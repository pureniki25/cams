package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.mapper.IssueSendOutsideLogMapper;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-27
 */
@Service("IssueSendOutsideLogService")
public class IssueSendOutsideLogServiceImpl extends BaseServiceImpl<IssueSendOutsideLogMapper, IssueSendOutsideLog> implements IssueSendOutsideLogService {

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public IssueSendOutsideLog createIssueSendOutsideLog(String userId, Object sendObject, String interfaceCode,
			String interfaceName, String systemCode) {
		IssueSendOutsideLog issueSendOutsideLog = new IssueSendOutsideLog();
		issueSendOutsideLog.setCreateTime(new Date());
		issueSendOutsideLog.setCreateUserId(userId);
		issueSendOutsideLog.setSendJson(JSONObject.toJSONString(sendObject));
		issueSendOutsideLog.setInterfacecode(interfaceCode);
		issueSendOutsideLog.setInterfacename(interfaceName);
		issueSendOutsideLog.setSystem(systemCode);
		return issueSendOutsideLog;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void save(IssueSendOutsideLog issueSendOutsideLog) {
		issueSendOutsideLog.insert();
	}

}
