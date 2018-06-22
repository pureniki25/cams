package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;

/**
 * 团贷网标的还款信息还款计划列表信息
 * 
 * @author 胡伟骞
 *
 */
public class TdRefundMonthInfoDTO implements Serializable {

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
	 * 实际还款日期 如未还，该字段为空，样式yyyy-MM-dd
	 */
	private String realityCycDate;
	/**
	 * 还款本金
	 */
	private Double amount;
	/**
	 * 还款利息
	 */
	private Double interestAmout;
	/**
	 * 还款状态
	 */
	private int status;
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

	public String getRealityCycDate() {
		return realityCycDate;
	}

	public void setRealityCycDate(String realityCycDate) {
		this.realityCycDate = realityCycDate;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "TdRefundMonthInfoDTO [periods=" + periods + ", cycDate=" + cycDate + ", realityCycDate="
				+ realityCycDate + ", amount=" + amount + ", interestAmout=" + interestAmout + ", status=" + status
				+ ", overdueDay=" + overdueDay + ", overdueAmount=" + overdueAmount + ", advanceAmount=" + advanceAmount
				+ ", total=" + total + ", statusStr=" + statusStr + "]";
	}

}
