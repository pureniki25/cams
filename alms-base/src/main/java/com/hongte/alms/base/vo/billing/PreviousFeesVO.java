package com.hongte.alms.base.vo.billing;

public class PreviousFeesVO {
	private int period;
	private double previousPlanAccrual;
	private double previousPlanServiceCharge;
	private double previousLateFees;

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

	@Override
	public String toString() {
		return "PreviousFeesVO [period=" + period + ", previousPlanAccrual=" + previousPlanAccrual
				+ ", previousPlanServiceCharge=" + previousPlanServiceCharge + ", previousLateFees=" + previousLateFees
				+ "]";
	}

}
