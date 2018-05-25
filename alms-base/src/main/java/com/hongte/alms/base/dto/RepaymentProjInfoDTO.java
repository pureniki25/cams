package com.hongte.alms.base.dto;

public class RepaymentProjInfoDTO extends RepaymentPlanInfoDTO {

	private static final long serialVersionUID = 1L;

	private String realName;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "RepaymentProjInfoDTO [realName=" + realName + "]";
	}

}
