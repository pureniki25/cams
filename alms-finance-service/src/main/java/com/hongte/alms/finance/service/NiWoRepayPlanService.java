package com.hongte.alms.finance.service;

import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;

/**
 * @author 陈泽圣2018年6月13日 
 */
public interface NiWoRepayPlanService {


	/**
	 * 根据标ID获取标的信息
	 * 
	 * @author 陈泽圣 2018年6月13日
	 * @param afterId
	 * @return
	 */
	public void sycNiWoRepayPlan(String projId);


	/**
	 * 定时查询你我金融的还款计划
	 * 
	 * @author 陈泽圣 2018年6月19日
	 * @return
	 */
	public void SearchNiWoRepayPlan();
}
