package com.hongte.alms.base.vo.billing;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PreviousFeesVO {
	private String afterId;
	private double previousPlanAccrual;
	private double previousPlanServiceCharge;
	private double previousLateFees;
	private String currentStatus;
	private String businessId;
	private Date dueDate;

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public double getPreviousPlanAccrual() {
		return previousPlanAccrual;
	}

	public void setPreviousPlanAccrual(double previousPlanAccrual) {
		this.previousPlanAccrual = previousPlanAccrual;
	}

	public double getPreviousPlanServiceCharge() {
		return previousPlanServiceCharge;
	}

	public void setPreviousPlanServiceCharge(double previousPlanServiceCharge) {
		this.previousPlanServiceCharge = previousPlanServiceCharge;
	}

	public double getPreviousLateFees() {
		return previousLateFees;
	}

	public void setPreviousLateFees(double previousLateFees) {
		this.previousLateFees = previousLateFees;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@JsonFormat(timezone = "GMT + 8")
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "PreviousFeesVO [afterId=" + afterId + ", previousPlanAccrual=" + previousPlanAccrual
				+ ", previousPlanServiceCharge=" + previousPlanServiceCharge + ", previousLateFees=" + previousLateFees
				+ ", currentStatus=" + currentStatus + ", businessId=" + businessId + ", dueDate=" + dueDate + "]";
	}

}
