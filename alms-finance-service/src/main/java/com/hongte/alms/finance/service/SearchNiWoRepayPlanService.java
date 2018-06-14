package com.hongte.alms.finance.service;

import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;

/**
 * @author 陈泽圣2018年6月13日 
 */
public interface SearchNiWoRepayPlanService {


	/**
	 * 根据标ID获取标的信息
	 * 
	 * @author 陈泽圣 2018年6月13日
	 * @param afterId
	 * @return
	 */
	public NiWoProjPlanDto getNiWoRepayPlan(String projId);


    
}
