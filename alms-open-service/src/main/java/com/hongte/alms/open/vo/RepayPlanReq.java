package com.hongte.alms.open.vo;

import java.io.Serializable;

public class RepayPlanReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String businessId;
	private String repaymentBatchId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getRepaymentBatchId() {
		return repaymentBatchId;
	}

	public void setRepaymentBatchId(String repaymentBatchId) {
		this.repaymentBatchId = repaymentBatchId;
	}

	@Override
	public String toString() {
		return "RepayPlanReq [businessId=" + businessId + ", repaymentBatchId=" + repaymentBatchId + "]";
	}

}
