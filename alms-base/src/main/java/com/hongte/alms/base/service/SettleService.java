/**
 * 
 */
package com.hongte.alms.base.service;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.vo.finance.SettleInfoVO;

/**
 * @author 王继光
 * 2018年6月14日 下午3:03:49
 */
public interface SettleService {
	/**
	 * 获取结清应还信息
	 * @author 王继光
	 * 2018年6月14日 下午3:51:01
	 * @return
	 */
	public JSONObject settleInfo(String businessId,String afterId,String planId) ;
	
	/**
	 * 获取结清应还信息
	 * @author 王继光
	 * 2018年7月5日 下午2:43:59
	 * @param businessId
	 * @param afterId
	 * @param planId
	 * @return
	 */
	public SettleInfoVO settleInfoVO (String businessId ,String afterId,String planId);
}
