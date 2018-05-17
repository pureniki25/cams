package com.hongte.alms.base.mapper;

import java.math.BigDecimal;
import java.util.List;

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
	
	/**
	 * 某个标的某一期的RepaymentProjPlanList
	 * @author 王继光
	 * 2018年5月17日 上午10:55:35
	 * @param projectId
	 * @param afterId
	 * @return
	 */
	RepaymentProjPlanList selectByProjectIDAndAfterId(String projectId,String afterId) ;
}
