package com.hongte.alms.finance.dto.repayPlan;

import java.util.List;

import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划Detail表DTO
 */
public class RepaymentProjPlanListDetailDto {
	private RepaymentProjPlanListDetail repaymentProjPlanListDetail ;
	private List<RepaymentProjFactRepay> repaymentProjFactRepays ;

	/**
	 * @return the repaymentProjFactRepays
	 */
	public List<RepaymentProjFactRepay> getRepaymentProjFactRepays() {
		return repaymentProjFactRepays;
	}

	/**
	 * @param repaymentProjFactRepays the repaymentProjFactRepays to set
	 */
	public void setRepaymentProjFactRepays(List<RepaymentProjFactRepay> repaymentProjFactRepays) {
		this.repaymentProjFactRepays = repaymentProjFactRepays;
	}

	/**
	 * @return the repaymentProjPlanListDetail
	 */
	public RepaymentProjPlanListDetail getRepaymentProjPlanListDetail() {
		return repaymentProjPlanListDetail;
	}

	/**
	 * @param repaymentProjPlanListDetail the repaymentProjPlanListDetail to set
	 */
	public void setRepaymentProjPlanListDetail(RepaymentProjPlanListDetail repaymentProjPlanListDetail) {
		this.repaymentProjPlanListDetail = repaymentProjPlanListDetail;
	}
}
