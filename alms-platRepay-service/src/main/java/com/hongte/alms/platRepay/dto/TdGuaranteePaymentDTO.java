package com.hongte.alms.platRepay.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdGuaranteePaymentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 本金利息
	 */
	private BigDecimal principalAndInterest;
	/**
	 * 滞纳金
	 */
	private BigDecimal penaltyAmount;
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

	public BigDecimal getPrincipalAndInterest() {
		return principalAndInterest;
	}

	public void setPrincipalAndInterest(BigDecimal principalAndInterest) {
		this.principalAndInterest = principalAndInterest;
	}

	public BigDecimal getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
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

	@Override
	public String toString() {
		return "TdGuaranteePaymentDTO [principalAndInterest=" + principalAndInterest + ", penaltyAmount="
				+ penaltyAmount + ", tuandaiAmount=" + tuandaiAmount + ", orgAmount=" + orgAmount + ", guaranteeAmount="
				+ guaranteeAmount + ", arbitrationAmount=" + arbitrationAmount + ", agencyAmount=" + agencyAmount + "]";
	}

}
