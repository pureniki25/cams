/**
 * 
 */
package com.hongte.alms.finance.service;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;

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
	public void execute(ConfirmRepaymentReq req,boolean save);
}
