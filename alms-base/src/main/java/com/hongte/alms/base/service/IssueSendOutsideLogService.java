package com.hongte.alms.base.service;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-27
 */
public interface IssueSendOutsideLogService extends BaseService<IssueSendOutsideLog> {
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
	IssueSendOutsideLog createIssueSendOutsideLog(String userId, Object sendObject, String interfaceCode,
			String interfaceName, String systemCode) ;
	
	/**
	 * 保存日志,新建独立事务
	 * @author 王继光
	 * 2018年8月21日 上午11:31:22
	 * @param issueSendOutsideLog
	 */
	void save(IssueSendOutsideLog issueSendOutsideLog) ;
	
}
