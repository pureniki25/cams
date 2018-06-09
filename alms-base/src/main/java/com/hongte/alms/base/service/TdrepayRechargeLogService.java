package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 团贷网合规化还款标的充值记录表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-06
 */
public interface TdrepayRechargeLogService extends BaseService<TdrepayRechargeLog> {

	/**
	 * 资金分发回调接口
	 * 
	 * @param oIdPartner
	 *            资产端账户唯一编号
	 * @param batchId
	 *            批次号
	 * @param requestNo
	 *            订单号
	 * @param orderStatus
	 *            订单状态
	 */
	void callBackDistributeFund(String oIdPartner, String batchId, String requestNo, String orderStatus);
}
