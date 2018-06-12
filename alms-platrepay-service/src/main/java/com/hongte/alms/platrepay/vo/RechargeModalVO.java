package com.hongte.alms.platrepay.vo;

import java.io.Serializable;

/**
 * 代充值
 * @author 胡伟骞
 *
 */
public class RechargeModalVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 代充值账户
	 */
	private String rechargeAccountType;
	/**
	 * 转账类型
	 */
	private String transferType;
	/**
	 * 充值金额（元）
	 */
	private Double rechargeAmount;
	/**
	 * 充值来源账户
	 */
	private String rechargeSourseAccount;
	/**
	 * 选择银行
	 */
	private String bankCode;
	/**
	 * 代充值账户余额
	 */
	private Double rechargeAccountBalance;
	/**
	 * 银行账号
	 */
	private String bankAccount;

	public String getRechargeAccountType() {
		return rechargeAccountType;
	}

	public void setRechargeAccountType(String rechargeAccountType) {
		this.rechargeAccountType = rechargeAccountType;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getRechargeSourseAccount() {
		return rechargeSourseAccount;
	}

	public void setRechargeSourseAccount(String rechargeSourseAccount) {
		this.rechargeSourseAccount = rechargeSourseAccount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Double getRechargeAccountBalance() {
		return rechargeAccountBalance;
	}

	public void setRechargeAccountBalance(Double rechargeAccountBalance) {
		this.rechargeAccountBalance = rechargeAccountBalance;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public String toString() {
		return "RechargeModalVO [rechargeAccountType=" + rechargeAccountType + ", transferType=" + transferType
				+ ", rechargeAmount=" + rechargeAmount + ", rechargeSourseAccount=" + rechargeSourseAccount
				+ ", bankCode=" + bankCode + ", rechargeAccountBalance=" + rechargeAccountBalance + ", bankAccount="
				+ bankAccount + "]";
	}

}
