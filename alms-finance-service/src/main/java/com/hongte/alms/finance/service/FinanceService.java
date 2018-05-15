/**
 * 
 */
package com.hongte.alms.finance.service;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.vo.finance.CurrPeriodDerateInfoVO;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.common.result.Result;

/**
 * @author 王继光
 * 2018年5月7日 下午6:03:32
 */
public interface FinanceService {
	public Result appointBankStatement(RepaymentRegisterInfoDTO dto);
	public Result matchBankStatement(List<String> moneyPoolIds,String businessId,String afterId,String mprid);
	public Result disMatchedBankStatement(MoneyPool moneyPool,MoneyPoolRepayment moneyPoolRepayment);
	public Result thisPeroidRepayment(String businessId,String afterId);
	/**
	 * 获取本次应还信息
	 * @author 王继光
	 * 2018年5月14日 下午6:52:53
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public CurrPeriodRepaymentInfoVO getCurrPeriodRepaymentInfoVO(String businessId,String afterId) ;
	/**
	 * 获取当前期减免明细
	 * @author 王继光
	 * 2018年5月14日 下午7:39:31
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public CurrPeriodDerateInfoVO getCurrPeriodDerate(String businessId,String afterId) ;
	
	/**
	 * 计算结余金额
	 * @author 王继光
	 * 2018年5月14日 下午7:58:02
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public BigDecimal getSurplusFund(String businessId,String afterId) ;
	
	public List<CurrPeriodProjDetailVO> getCurrPeriodProjDetailVOs(String businessId,String afterId) ;
	
	public BigDecimal calFactRepay(Integer itemType,String feeId,String businessId,String afterId) ;
}
