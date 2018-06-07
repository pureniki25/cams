package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 代充值操作记录表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-04
 */
public interface AgencyRechargeLogService extends BaseService<AgencyRechargeLog> {
	/**
	 * 代充值回调接口
	 * 
	 * @param cmOrderNo
	 *            唯一订单号
	 * @param orderStatus
	 *            订单状态
	 */
	void callBackAgencyRecharge(String cmOrderNo, String orderStatus);

	/**
	 * 查询充值订单
	 * @param oidPartner资产端账户唯一编号
	 * @param cmOrderNo机构订单编号
	 * @param updateUser更新人
	 * @return
	 */
	void queryRechargeOrder(String oidPartner, String cmOrderNo, String updateUser);
}
