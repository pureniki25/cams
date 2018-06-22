package com.hongte.alms.finance.req;

import java.io.Serializable;

public class RepayPlanReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String businessId;
	private String repaymentBatchId;
    private String afterId;

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

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

	@Override
	public String toString() {
		return "RepayPlanReq [businessId=" + businessId + ", repaymentBatchId=" + repaymentBatchId + "]";
	}

}
