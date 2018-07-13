package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平台标的还款计划信息DTO
 * @author 胡伟骞
 *
 */
public class TdPlatformPlanRepaymentDTO implements Serializable {

	private static final long serialVersionUID = -7918825844438203785L;

	/**
	 * 期次
	 */
	private int period;

	/**
	 * 还款日期
	 */
	private String cycDate;

	/**
	 * 本金
	 */
	private BigDecimal amount;
	/**
	 * 利息
	 */
	private BigDecimal interestAmount;
	/**
	 * 保证金
	 */
	private BigDecimal depositAmount;
	/**
	 * 担保费
	 */
	private BigDecimal guaranteeAmount;
	/**
	 * 仲裁费
	 */
	private BigDecimal arbitrationAmount;
	/**
	 * 资产端服务费
	 */
	private BigDecimal orgAmount;
	/**
	 * 平台服务费
	 */
	private BigDecimal tuandaiAmount;
	/**
	 * 中介服务费
	 */
	private BigDecimal agencyAmount;
	/**
	 * 其他费用
	 */
	private BigDecimal otherAmount;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getCycDate() {
		return cycDate;
	}

	public void setCycDate(String cycDate) {
		this.cycDate = cycDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
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

	public BigDecimal getOrgAmount() {
		return orgAmount;
	}

	public void setOrgAmount(BigDecimal orgAmount) {
		this.orgAmount = orgAmount;
	}

	public BigDecimal getTuandaiAmount() {
		return tuandaiAmount;
	}

	public void setTuandaiAmount(BigDecimal tuandaiAmount) {
		this.tuandaiAmount = tuandaiAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}

	@Override
	public String toString() {
		return "TdPlatformPlanRepaymentDTO [period=" + period + ", cycDate=" + cycDate + ", amount=" + amount
				+ ", interestAmount=" + interestAmount + ", depositAmount=" + depositAmount + ", guaranteeAmount="
				+ guaranteeAmount + ", arbitrationAmount=" + arbitrationAmount + ", orgAmount=" + orgAmount
				+ ", tuandaiAmount=" + tuandaiAmount + ", agencyAmount=" + agencyAmount + ", otherAmount=" + otherAmount
				+ "]";
	}

}
