package com.hongte.alms.platRepay.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdProjectPaymentDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 期数
	 */
	private int period;
	/**
	 * 还款状态 1 已结清 0逾期
	 */
	private int status;
	/**
	 * 还款日期 yyyy-MM-dd
	 */
	private String addDate;
	/**
	 * 实还总金额
	 */
	private BigDecimal totalAmount;
	/**
	 * 实还本息
	 */
	private BigDecimal principalAndInterest;
	/**
	 * 实还平台服务费
	 */
	private BigDecimal tuandaiAmount;
	/**
	 * 实还资产端服务费
	 */
	private BigDecimal orgAmount;
	/**
	 * 实还担保公司服务费
	 */
	private BigDecimal guaranteeAmount;
	/**
	 * 实还仲裁服务费
	 */
	private BigDecimal arbitrationAmount;
	/**
	 * 实还中介服务费
	 */
	private BigDecimal agencyAmount;
	/**
	 * 滞纳金
	 */
	private BigDecimal penaltyAmount;

	/**
	 * 借款人还款信息
	 */
	private TdBorrowerPaymentDTO borrowerPayment;
	/**
	 * 担保公司垫付信息
	 */
	private TdGuaranteePaymentDTO guaranteePayment;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPrincipalAndInterest() {
		return principalAndInterest;
	}

	public void setPrincipalAndInterest(BigDecimal principalAndInterest) {
		this.principalAndInterest = principalAndInterest;
	}

	public BigDecimal getTuandaiAmount() {
		return tuandaiAmount;
	}

	public void setTuandaiAmount(BigDecimal tuandaiAmount) {
		this.tuandaiAmount = tuandaiAmount;
	}

	public BigDecimal getOrgAmount() {
		return orgAmount;
	}

	public void setOrgAmount(BigDecimal orgAmount) {
		this.orgAmount = orgAmount;
	}

	public BigDecimal getGuaranteeAmount() {
		return guaranteeAmount;
	}

	public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
		this.guaranteeAmount = guaranteeAmount;
	}

	public BigDecimal getArbitrationAmount() {
		return arbitrationAmount;
	}

	public void setArbitrationAmount(BigDecimal arbitrationAmount) {
		this.arbitrationAmount = arbitrationAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public TdBorrowerPaymentDTO getBorrowerPayment() {
		return borrowerPayment;
	}

	public void setBorrowerPayment(TdBorrowerPaymentDTO borrowerPayment) {
		this.borrowerPayment = borrowerPayment;
	}

	public TdGuaranteePaymentDTO getGuaranteePayment() {
		return guaranteePayment;
	}

	public void setGuaranteePayment(TdGuaranteePaymentDTO guaranteePayment) {
		this.guaranteePayment = guaranteePayment;
	}

	@Override
	public String toString() {
		return "TdProjectPaymentDTO [period=" + period + ", status=" + status + ", addDate=" + addDate
				+ ", totalAmount=" + totalAmount + ", principalAndInterest=" + principalAndInterest + ", tuandaiAmount="
				+ tuandaiAmount + ", orgAmount=" + orgAmount + ", guaranteeAmount=" + guaranteeAmount
				+ ", arbitrationAmount=" + arbitrationAmount + ", agencyAmount=" + agencyAmount + ", penaltyAmount="
				+ penaltyAmount + ", borrowerPayment=" + borrowerPayment + ", guaranteePayment=" + guaranteePayment
				+ "]";
	}

}
