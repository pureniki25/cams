package com.hongte.alms.platRepay.service;

import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
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
	
	/**
	 * 查询合规化还款主页面列表
	 * @param paramMap
	 * @return
	 */
	List<TdrepayRechargeLog> queryComplianceRepaymentData(ComplianceRepaymentVO vo);
}
