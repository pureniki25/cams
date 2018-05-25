package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划Detail表DTO
 */
public class RepaymentProjPlanListDetailDto implements Serializable {
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
