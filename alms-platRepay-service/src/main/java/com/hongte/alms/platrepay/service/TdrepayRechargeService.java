package com.hongte.alms.platrepay.service;

import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.platrepay.vo.TdrepayRechargeInfoVO;

/**
 * 资金分发接口
 * 
 * @author 胡伟骞
 *
 */
public interface TdrepayRechargeService {
	/**
	 * 代充值资金分发参数接入接口
	 * 
	 * @param vo
	 */
	void saveTdrepayRechargeInfo(TdrepayRechargeInfoVO vo);

	/**
	 * 查询合规化还款主页面列表
	 * 
	 * @param paramMap
	 * @return
	 */
	List<TdrepayRechargeLog> queryComplianceRepaymentData(ComplianceRepaymentVO vo);

	int countComplianceRepaymentData(ComplianceRepaymentVO vo);

	/**
	 * 执行资金分发
	 */
	void tdrepayRecharge(List<TdrepayRechargeInfoVO> vos);

	/**
	 * 根据代充值账户类型获取 出款方账号
	 * 
	 * @param rechargeAccountType
	 *            代充值账户类型
	 * @return
	 */
	String handleAccountType(String rechargeAccountType);

	/**
	 * 根据代充值账户类型获取 资产端账户唯一编号
	 * 
	 * @param rechargeAccountType
	 *            代充值账户类型
	 * @return
	 */
	String handleOIdPartner(String rechargeAccountType);
	
	/**
	 * 资产端对团贷网通用合规化还款流程
	 */
	void rePayComplianceWithRequirements(String logId);

}
