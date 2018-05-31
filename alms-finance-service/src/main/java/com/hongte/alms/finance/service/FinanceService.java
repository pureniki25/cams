/**
 * 
 */
package com.hongte.alms.finance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.dto.ActualPaymentSingleLogDTO;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.vo.finance.CurrPeriodDerateInfoVO;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;

/**
 * @author 王继光 2018年5月7日 下午6:03:32
 */
public interface FinanceService {
	public Result appointBankStatement(RepaymentRegisterInfoDTO dto);

	public Result matchBankStatement(List<String> moneyPoolIds, String businessId, String afterId, String mprid);

	public Result disMatchedBankStatement(MoneyPool moneyPool, MoneyPoolRepayment moneyPoolRepayment);

	public Result thisPeroidRepayment(String businessId, String afterId);

	/**
	 * 获取本次应还信息
	 * 
	 * @author 王继光 2018年5月14日 下午6:52:53
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public CurrPeriodRepaymentInfoVO getCurrPeriodRepaymentInfoVO(String businessId, String afterId);

	/**
	 * 获取当前期减免明细
	 * 
	 * @author 王继光 2018年5月14日 下午7:39:31
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public CurrPeriodDerateInfoVO getCurrPeriodDerate(String businessId, String afterId);

	/**
	 * 计算结余金额
	 * 
	 * @author 王继光 2018年5月14日 下午7:58:02
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	@Deprecated
	public BigDecimal getSurplusFund(String businessId, String afterId);

	public List<CurrPeriodProjDetailVO> getCurrPeriodProjDetailVOs(String businessId, String afterId);

	/**
	 * 计算某期某项已还金额
	 * 
	 * @author 王继光 2018年5月15日 下午2:01:02
	 * @param itemType
	 * @param feeId
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public BigDecimal calFactRepay(Integer itemType, String feeId, String businessId, String afterId);

	/**
	 * 查询已完成的还款信息
	 * 
	 * @author 王继光 2018年5月15日 下午2:13:33
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public List<MatchedMoneyPoolVO> selectConfirmedBankStatement(String businessId, String afterId);

	
	/**
     * 根据源业务编号获取还款计划信息
     * @param businessId
     * @return
     */
    Map<String, Object> queryRepaymentPlanInfoByBusinessId(String businessId);
    
    /**
     * 根据业务还款计划列表ID获取所有对应的标的应还还款计划信息
     * @param planListId
     * @return
     */
    Map<String, Object> queryPlanRepaymentProjInfoByPlanListId(String planListId);
    
    /**
     * 根据业务还款计划列表ID获取所有对应的标的实还还款计划信息
     * @param planListId
     * @return
     */
    Map<String, Object> queryActualRepaymentProjInfoByPlanListId(String planListId);
    
    /**
     * 获取标还款计划差额
     * @param planListId
     * @return
     */
    Map<String, Object> queryDifferenceRepaymentProjInfo(String planListId);
    
    /**
     * 获取业务维度的其他费用
     * @param planListId
     * @return
     */
    List<String> queryBizOtherFee(String planListId);
    
    /**
     * 获取标维度的其他费用
     * @param projPlanListId
     * @return
     */
    List<String> queryProjOtherFee(String projPlanListId);
    
    /**
     * 获取业务维度贷款余额（非源业务编号）
     * @param businessId
     * @return
     */
    void sendLoanBalanceToDataPlatform(String businessId);
    
    /**
	 * 根据业务编号查找实还流水
	 */
    Map<String, Object> queryActualPaymentByBusinessId(String businessId);
    
}
