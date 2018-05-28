package com.hongte.alms.finance.req;

import java.io.Serializable;

public class RepayPlanReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return "RepayPlanReq [businessId=" + businessId + "]";
	}

}
