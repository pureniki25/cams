package com.hongte.alms.finance.service;

import java.util.HashMap;


/**
 * @author 陈泽圣2018年6月13日 
 */
public interface NiWoRepayPlanService {


	/**
	 * 根据请求编号获取标的信息
	 * 
	 * @author 陈泽圣 2018年6月13日
	 * @return
	 */
	public void sycNiWoRepayPlan(String orderNo,HashMap<String,Object> niwoMap);


	/**
	 * 定时查询你我金融的还款计划
	 * 
	 * @author 陈泽圣 2018年6月19日
	 * @return
	 */
	public void SearchNiWoRepayPlan();
}
