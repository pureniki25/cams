package com.hongte.alms.base.vo.billing;

public class PreviousFeesVO {
	private int period;
	private double previousPlanAccrual;
	private double previousPlanServiceCharge;
	private double previousLateFees;
	private String currentStatus;
	private String businessId;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
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

	@Override
	public String toString() {
		return "PreviousFeesVO [period=" + period + ", previousPlanAccrual=" + previousPlanAccrual
				+ ", previousPlanServiceCharge=" + previousPlanServiceCharge + ", previousLateFees=" + previousLateFees
				+ ", currentStatus=" + currentStatus + ", businessId=" + businessId + "]";
	}

}
