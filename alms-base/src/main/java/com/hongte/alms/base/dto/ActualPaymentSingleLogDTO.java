package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.Date;

public class ActualPaymentSingleLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private String afterId;

	/**
	 * 交易金额
	 */
	private double currentAmount;

	/**
	 * 收支类型
	 */
	private String incomeType;

	/**
	 * 交易时间
	 */
	private Date tradeTime;

	/**
	 * 交易方式
	 */
	private String tradeType;

	/**
	 * 账户名称
	 */
	private String accountName;

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public double getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
	public String toString() {
		return "ActualPaymentSingleLogDTO [afterId=" + afterId + ", currentAmount=" + currentAmount + ", incomeType="
				+ incomeType + ", tradeTime=" + tradeTime + ", tradeType=" + tradeType + ", accountName=" + accountName
				+ "]";
	}

}
