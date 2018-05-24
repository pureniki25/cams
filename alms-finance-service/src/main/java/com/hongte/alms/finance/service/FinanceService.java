/**
 * 
 */
package com.hongte.alms.finance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.RepaymentPlanInfoDTO;
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
	 * 预览确认还款后的信息
	 * @author 王继光
	 * 2018年5月15日 下午9:04:29
	 * @param req
	 * @return
	 */
	public Result previewConfirmRepayment(ConfirmRepaymentReq req) ;

	/**
	 * 确认还款
	 * @author 王继光
	 * 2018年5月15日 下午9:05:33
	 * @param req
	 * @return
	 */
	public Result confirmRepayment(ConfirmRepaymentReq req);
	
	/**
     * 根据源业务编号获取还款计划信息
     * @param businessId
     * @return
     */
    Map<String, Object> queryRepaymentPlanInfoByBusinessId(String businessId);
    
    /**
     * 根据源业务编号获取还款计划信息
     * @param planListId
     * @return
     */
    Map<String, Object> queryRepaymentProjInfoByPlanListId(String planListId);
}
