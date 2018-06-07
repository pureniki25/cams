package com.hongte.alms.platRepay.service;

import com.hongte.alms.platRepay.vo.TdrepayRechargeInfoVO;

/**
 * 资金分发接口
 * @author 胡伟骞
 *
 */
public interface TdrepayRechargeService {
	/**
	 * 代充值资金分发参数接入接口
	 * @param vo
	 */
	void saveTdrepayRechargeInfo(TdrepayRechargeInfoVO vo);
}
