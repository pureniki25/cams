/**
 * 
 */
package com.hongte.alms.base.service;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleRepaymentPlanVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;

/**
 * @author 王继光
 * 2018年3月12日 下午5:09:59
 */
public interface ExpenseSettleService {
	
	public ExpenseSettleVO cal(String preSettleDate,String businessId);
	public ExpenseSettleVO cal(String businessId,Date preSettleDate) throws Exception;
	public ExpenseSettleVO sum(String businessId);
	public List<ExpenseSettleLackFeeVO> listLackFee(String businessId) ;
	public void calLackFee(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan);
	public void calPrincipal(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan,List<BizOutputRecord> bizOutputRecords);
	public void calDemurrage(Date settleDate, ExpenseSettleVO expenseSettleVO, BasicBusiness basicBusiness, ExpenseSettleRepaymentPlanVO plan);
}
