package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.base.vo.compliance.RechargeRecordReq;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
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
	 * 根据代充值账户类型获取参数配置
	 * @param rechargeAccountType
	 * @return
	 */
	SysParameter queryRechargeAccountSysParams(String rechargeAccountType);
	
	/**
	 * 资产端对团贷网通用合规化还款流程
	 */
	void repayComplianceWithRequirements();
	
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
	
	/**
	 * 查询资金分发记录
	 * @param projectId
	 * @return
	 */
	List<DistributeFundRecordVO> queryDistributeFundRecord(String projectId);
	
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
	
	/**
	 * 撤销资金分发
	 * @param tdrepayRechargeLogs
	 */
	void revokeTdrepayRecharge(List<TdrepayRechargeLog> tdrepayRechargeLogs);
	
	/**
	 * 处理合规化还款处理中的数据
	 */
	void handleRunningData();
}
