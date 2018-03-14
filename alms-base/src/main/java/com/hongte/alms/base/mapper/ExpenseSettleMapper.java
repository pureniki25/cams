/**
 * 
 */
package com.hongte.alms.base.mapper;

import java.util.List;

import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;

/**
 * @author 王继光
 * 2018年3月13日 下午1:56:05
 */
public interface ExpenseSettleMapper {

	public List<ExpenseSettleLackFeeVO > listLackFee(String businessId);
}
