package com.hongte.alms.base.mapper;

import java.util.List;

import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.vo.compliance.RechargeRecordReq;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 代充值操作记录表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-04
 */
public interface AgencyRechargeLogMapper extends SuperMapper<AgencyRechargeLog> {

	/**
	 * 查询充值记录
	 * @param req
	 * @return
	 */
	List<AgencyRechargeLog> queryRechargeRecord(RechargeRecordReq req);
	
	/**
	 * 查询充值记录
	 * @param req
	 * @return
	 */
	int countRechargeRecord(RechargeRecordReq req);
}
