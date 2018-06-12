package com.hongte.alms.platrepay.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdReturnAdvanceShareProfitDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private int period;
	/**
	 * 还款日期
	 */
	private String refundDate;
	/**
	 * 还款总金额
	 */
	private BigDecimal totalAmount;
	/**
	 * 本金+利息
	 */
	private BigDecimal principalAndInterest;
	/**
	 * 平台服务费
	 */
	private BigDecimal tuandaiAmount;
	/**
	 * 资产端服务费
	 */
	private BigDecimal orgAmount;
	/**
	 * 担保公司服务费
	 */
	private BigDecimal guaranteeAmount;
	/**
	 * 仲裁服务费
	 */
	private BigDecimal arbitrationAmount;
	/**
	 * 逾期费用（罚息）
	 */
	private BigDecimal overDueAmount;
	/**
	 * 结清状态 1 已结清 0 未结清
	 */
	private int status;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
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

	public BigDecimal getOverDueAmount() {
		return overDueAmount;
	}

	public void setOverDueAmount(BigDecimal overDueAmount) {
		this.overDueAmount = overDueAmount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TdReturnAdvanceShareProfitDTO [period=" + period + ", refundDate=" + refundDate + ", totalAmount="
				+ totalAmount + ", principalAndInterest=" + principalAndInterest + ", tuandaiAmount=" + tuandaiAmount
				+ ", orgAmount=" + orgAmount + ", guaranteeAmount=" + guaranteeAmount + ", arbitrationAmount="
				+ arbitrationAmount + ", overDueAmount=" + overDueAmount + ", status=" + status + "]";
	}

}
