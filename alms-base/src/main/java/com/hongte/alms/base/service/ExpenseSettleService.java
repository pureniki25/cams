/**
 * 
 */
package com.hongte.alms.base.service;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;

/**
 * @author 王继光
 * 2018年3月12日 下午5:09:59
 */
public interface ExpenseSettleService {
	
	public ExpenseSettleVO cal(String preSettleDate,String businessId);
	public ExpenseSettleVO sum(String businessId);
}
