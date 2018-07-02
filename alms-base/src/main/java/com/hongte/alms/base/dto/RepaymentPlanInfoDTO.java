package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RepaymentPlanInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 还款计划列表ID
	 */
	private String planListId;
	/**
	 * 类型: 计划还款、实际还款、差额
	 */
	private String repayment;
	/**
	 * 期数：还款计划的期数
	 */
	private String afterId;
	/**
	 * 还款日期：应还日期/实际还款日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date repaymentDate;
	/**
	 * 本金：本期应还本金
	 */
	private double principal;
	/**
	 * 利息：本期应还利息
	 */
	private double accrual;
	/**
	 * 月收分公司服务费：本期月收分公司服务费
	 */
	private double serviceCharge;
	/**
	 * 月收平台费：本期月收平台费
	 */
	private double platformCharge;
	/**
	 * 其他费用：本期应收其他费用，该费用在应还时应为0，结清时还才有值
	 */
	private double otherFee;
	/**
	 * 小计：本金+利息+月收分公司服务费+月收平台费
	 */
	private double subtotal;
	/**
	 * 逾期天数
	 */
	private double overdueDays;
	/**
	 * 线上滞纳金：应还线上滞纳金 逾期天数×线上滞纳金费率（系统配置）
	 */
	private double onlineLateFee;
	/**
	 * 线下滞纳金 ：应还线下滞纳金 逾期天数× 线下滞纳金费率
	 */
	private double offlineLateFee;
	/**
	 * 线上减免金额
	 */
	private double onlineDerateAmount;
	/**
	 * 线下减免金额
	 */
	private double offlineDerateAmount;
	/**
	 * 结余：应还为0，实际实收结余费用项
	 */
	private double surplus;
	/**
	 * 还款合计（含滞纳金）：小计+线上滞纳金+线下滞纳金+结余
	 */
	private double total;
	/**
	 * 总计
	 */
	private double amount;
	/**
	 * 财务确认状态
	 */
	private String confirmFlag;

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getRepayment() {
		return repayment;
	}

	public void setRepayment(String repayment) {
		this.repayment = repayment;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Date getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getAccrual() {
		return accrual;
	}

	public void setAccrual(double accrual) {
		this.accrual = accrual;
	}

	public double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public double getPlatformCharge() {
		return platformCharge;
	}

	public void setPlatformCharge(double platformCharge) {
		this.platformCharge = platformCharge;
	}

	public double getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(double overdueDays) {
		this.overdueDays = overdueDays;
	}

	public double getOnlineLateFee() {
		return onlineLateFee;
	}

	public void setOnlineLateFee(double onlineLateFee) {
		this.onlineLateFee = onlineLateFee;
	}

	public double getOfflineLateFee() {
		return offlineLateFee;
	}

	public void setOfflineLateFee(double offlineLateFee) {
		this.offlineLateFee = offlineLateFee;
	}

	public double getOnlineDerateAmount() {
		return onlineDerateAmount;
	}

	public void setOnlineDerateAmount(double onlineDerateAmount) {
		this.onlineDerateAmount = onlineDerateAmount;
	}

	public double getOfflineDerateAmount() {
		return offlineDerateAmount;
	}

	public void setOfflineDerateAmount(double offlineDerateAmount) {
		this.offlineDerateAmount = offlineDerateAmount;
	}

	public double getSurplus() {
		return surplus;
	}

	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	@Override
	public String toString() {
		return "RepaymentPlanInfoDTO [planListId=" + planListId + ", repayment=" + repayment + ", afterId=" + afterId
				+ ", repaymentDate=" + repaymentDate + ", principal=" + principal + ", accrual=" + accrual
				+ ", serviceCharge=" + serviceCharge + ", platformCharge=" + platformCharge + ", otherFee=" + otherFee
				+ ", subtotal=" + subtotal + ", overdueDays=" + overdueDays + ", onlineLateFee=" + onlineLateFee
				+ ", offlineLateFee=" + offlineLateFee + ", onlineDerateAmount=" + onlineDerateAmount
				+ ", offlineDerateAmount=" + offlineDerateAmount + ", surplus=" + surplus + ", total=" + total
				+ ", amount=" + amount + ", confirmFlag=" + confirmFlag + "]";
	}

}
