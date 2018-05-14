/**
 * 
 */
package com.hongte.alms.finance.service;

import java.util.List;

import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.common.result.Result;

/**
 * @author 王继光
 * 2018年5月7日 下午6:03:32
 */
public interface FinanceService {
	public Result appointBankStatement(RepaymentRegisterInfoDTO dto);
	public Result matchBankStatement(List<String> moneyPoolIds,String businessId,String afterId,String mprid);
	public Result disMatchedBankStatement(MoneyPool moneyPool,MoneyPoolRepayment moneyPoolRepayment);
}
