package com.hongte.alms.platrepay.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.platrepay.vo.TdrepayRechargeInfoVO;
import com.ht.ussp.core.Result;

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
	 * 根据代充值账户类型获取 资产端机构用户名(资产端在团贷网的账户对应的用户名)
	 * 
	 * @param businessType
	 *            业务类型
	 * @return
	 */
	int handleTdUserName(int businessType);
	
	/**
	 * 资产端对团贷网通用合规化还款流程
	 */
	void rePayComplianceWithRequirements();
	
	/**
	 * 从平台获取标的还款信息、还垫付信息
	 * @param projectId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map<String, Result> getAdvanceShareProfitAndProjectPayment(String projectId);
	
	/**
	 * 标的还款信息查询接口 assetside/getProjectPayment
	 * @param projectId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result remoteGetProjectPayment(String projectId);
	
}
