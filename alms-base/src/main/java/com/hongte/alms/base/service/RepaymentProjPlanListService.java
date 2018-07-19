package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
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
	void calLateFee();
	/**
	 * 找出符合条件的记录进行滞纳金计算
	 * @return
	 */
	List<RepaymentProjPlanList> getProListForCalLateFee(String projListId);


    List<RepaymentSettleMoneyDto> selectProjPlanMoney(int i, String businessId, Integer period,String planId);
}
