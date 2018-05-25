package com.hongte.alms.base.service;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 标的还款计划列表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanListService extends BaseService<RepaymentProjPlanList> {
	Double getOnLinePlanAmountSum(String projListId);
	
	Double getOnLineFactAmountSum(String projListId);
	Double getFactAmountSum(String projPlanId);
	Double getPrincipalAndinterestPeriod(@Param("projListId") String projListId);
	
	

}
