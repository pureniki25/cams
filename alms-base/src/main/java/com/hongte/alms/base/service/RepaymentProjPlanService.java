package com.hongte.alms.base.service;

import java.util.Date;
import java.util.List;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.common.service.BaseService;


/**
 * <p>
 * 标的还款计划信息 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanService extends BaseService<RepaymentProjPlan> {
	
	
	RepaymentProjPlanList findCurrentPeriod(Date settleDate, List<RepaymentProjPlanList> projPlanLists);

	List<RepaymentSettleMoneyDto> selectProjPlanMoney(String businessId, String planId);
	
}
