package com.hongte.alms.base.dto;

public class RepaymentProjInfoDTO extends RepaymentPlanInfoDTO {

	private static final long serialVersionUID = 1L;

	private String projPlanListId;

	private String realName;

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "RepaymentProjInfoDTO [projPlanListId=" + projPlanListId + ", realName=" + realName + "]";
	}

}
