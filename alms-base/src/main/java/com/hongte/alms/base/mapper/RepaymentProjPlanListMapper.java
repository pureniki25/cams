package com.hongte.alms.base.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
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
	
	/**
	 * 计算某个proj_plan_list的总实还
	 * @author 王继光
	 * 2018年5月26日 下午2:40:25
	 * @param projPlanListId
	 * @return
	 */
	BigDecimal caluProjPlanListFactAmount(String projPlanListId);
	
	/**计算某个proj_plan_list的总应还(已减去减免)
	 * @author 王继光
	 * 2018年5月26日 下午2:41:10
	 * @param projPlanListId
	 * @return
	 */
	BigDecimal caluProjPlanListPlanAmount(String projPlanListId);
	
	/**
	 * 找出符合条件的记录进行滞纳金计算
	 * @return
	 */
	List<RepaymentProjPlanList> getProListForCalLateFee(@Param("projListId") String projListId);

	/**
	 * 结清期 前面和后面的详情金额查询
	 * @param flag
	 * @param businessId
	 * @param period
	 * @return
	 */
    List<RepaymentSettleMoneyDto> selectProjPlanMoney(@Param("flag") int flag,@Param("businessId")  String businessId,@Param("period")  Integer period,@Param("planId") String planId);

}
