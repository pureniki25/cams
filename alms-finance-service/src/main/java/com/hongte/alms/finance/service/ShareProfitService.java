/**
 * 
 */
package com.hongte.alms.finance.service;

import java.util.List;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;

/**
 * @author 王继光
 * 2018年5月24日 下午2:41:31
 */
public interface ShareProfitService {
//	public void privew(ConfirmRepaymentReq req);
	/**
	 * 分润核心算法
	 * @author 王继光
	 * 2018年5月24日 下午3:00:53
	 * @param req
	 * @param save,true=保存分润后数据,false=不保存分润后数据
	 */
	public  List<CurrPeriodProjDetailVO>  execute(ConfirmRepaymentReq req,boolean save);


	/**
	 * 更新还款计划信息到信贷系统
	 * @param businessId
	 */
	public void updateRepayPlanToLMS(String businessId);
}
