package com.hongte.alms.platrepay.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlatFormRepaymentInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前还款期数
	 */
	private int periods;

	/**
	 * 还款日期 样式：yyyy-MM-dd
	 */
	private String cycDate;
	/**
	 * 还款本金
	 */
	private Double amount;
	/**
	 * 还款利息
	 */
	private Double interestAmout;
	/**
	 * 逾期天数
	 */
	private int overdueDay;
	/**
	 * 逾期费用
	 */
	private Double overdueAmount;
	/**
	 * 垫付费用 额外费用，不包含本金利息
	 */
	private Double advanceAmount;

	/**
	 * 应还总额
	 */
	private Double total;

	/**
	 * 还款状态
	 */
	private String statusStr;

	/**
	 * 实还期数
	 */
	private int period;

	/**
	 * 还款状态 1 已结清 0逾期
	 */
	private String statusStrActual;
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

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public String getCycDate() {
		return cycDate;
	}

	public void setCycDate(String cycDate) {
		this.cycDate = cycDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getInterestAmout() {
		return interestAmout;
	}

	public void setInterestAmout(Double interestAmout) {
		this.interestAmout = interestAmout;
	}

	public int getOverdueDay() {
		return overdueDay;
	}

	public void setOverdueDay(int overdueDay) {
		this.overdueDay = overdueDay;
	}

	public Double getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(Double overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getStatusStrActual() {
		return statusStrActual;
	}

	public void setStatusStrActual(String statusStrActual) {
		this.statusStrActual = statusStrActual;
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

	@Override
	public String toString() {
		return "PlatFormRepaymentInfoVO [periods=" + periods + ", cycDate=" + cycDate + ", amount=" + amount
				+ ", interestAmout=" + interestAmout + ", overdueDay=" + overdueDay + ", overdueAmount=" + overdueAmount
				+ ", advanceAmount=" + advanceAmount + ", total=" + total + ", statusStr=" + statusStr + ", period="
				+ period + ", statusStrActual=" + statusStrActual + ", addDate=" + addDate + ", totalAmount="
				+ totalAmount + ", principalAndInterest=" + principalAndInterest + ", tuandaiAmount=" + tuandaiAmount
				+ ", orgAmount=" + orgAmount + ", guaranteeAmount=" + guaranteeAmount + ", arbitrationAmount="
				+ arbitrationAmount + ", agencyAmount=" + agencyAmount + ", penaltyAmount=" + penaltyAmount + "]";
	}

}
