package com.hongte.alms.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 标的还款计划列表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanListMapper extends SuperMapper<RepaymentProjPlanList> {
	Double getOnLinePlanAmountSum(@Param("projListId") String projListId);
	
	Double getOnLineFactAmountSum(@Param("projListId") String projListId);
	
	Double getFactAmountSum(@Param("projPlanId") String projPlanId);
	Double getPrincipalAndInterestPeriod(@Param("projListId") String projListId);
	

}
