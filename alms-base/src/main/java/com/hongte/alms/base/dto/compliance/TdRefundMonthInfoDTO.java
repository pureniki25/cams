package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 团贷网标的还款信息还款计划列表信息
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
	private BigDecimal amount;
	/**
	 * 还款利息
	 */
	private BigDecimal interestAmout;
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
	private BigDecimal overdueAmount;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestAmout() {
		return interestAmout;
	}

	public void setInterestAmout(BigDecimal interestAmout) {
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

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	@Override
	public String toString() {
		return "TdRefundMonthInfoDTO [periods=" + periods + ", cycDate=" + cycDate + ", realityCycDate="
				+ realityCycDate + ", amount=" + amount + ", interestAmout=" + interestAmout + ", status=" + status
				+ ", overdueDay=" + overdueDay + ", overdueAmount=" + overdueAmount + "]";
	}

}
