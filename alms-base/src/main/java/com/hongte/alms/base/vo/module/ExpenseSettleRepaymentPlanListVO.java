/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.util.List;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;

/**
 * @author 王继光
 * 2018年3月30日 上午9:38:09
 */
public class ExpenseSettleRepaymentPlanListVO {
	private RepaymentBizPlanList repaymentBizPlanList ;
	private List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails ;

	/**
	 * @return the repaymentBizPlanListDetails
	 */
	public List<RepaymentBizPlanListDetail> getRepaymentBizPlanListDetails() {
		return repaymentBizPlanListDetails;
	}

	/**
	 * @param repaymentBizPlanListDetails the repaymentBizPlanListDetails to set
	 */
	public void setRepaymentBizPlanListDetails(List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails) {
		this.repaymentBizPlanListDetails = repaymentBizPlanListDetails;
	}

	/**
	 * @return the repaymentBizPlanList
	 */
	public RepaymentBizPlanList getRepaymentBizPlanList() {
		return repaymentBizPlanList;
	}

	/**
	 * @param repaymentBizPlanList the repaymentBizPlanList to set
	 */
	public void setRepaymentBizPlanList(RepaymentBizPlanList repaymentBizPlanList) {
		this.repaymentBizPlanList = repaymentBizPlanList;
	} 
	
}
