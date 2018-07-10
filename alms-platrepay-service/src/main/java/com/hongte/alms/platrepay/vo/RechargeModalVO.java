package com.hongte.alms.platrepay.vo;

import java.io.Serializable;

/**
 * 充值
 * 
 * @author 胡伟骞
 *
 */
public class RechargeModalVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 充值账户类型（1、车贷；2、房贷；3、扶贫贷；4、闪贷业务；5、车全业务；6、二手车业务；7、一点车贷；8、信用贷）
	 */
	private String rechargeAccountType;
	/**
	 * 转账类型(1：对公；2：对私)
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
	 * 银行编码
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

	/**
	 * 原业务编号
	 */
	private String origBusinessId;

	/**
	 * 资产端期数
	 */
	private String afterId;

	/**
	 * 操作人
	 */
	private String operator;

	/**
	 * 客户端IP
	 */
	private String clientIp;

	/**
	 * 充值类型 （1：网关；2：快捷；3：代充值）
	 */
	private String chargeType;

	/**
	 * 充值账户userID
	 */
	private String rechargeUserId;

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

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getRechargeUserId() {
		return rechargeUserId;
	}

	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}

	@Override
	public String toString() {
		return "RechargeModalVO [rechargeAccountType=" + rechargeAccountType + ", transferType=" + transferType
				+ ", rechargeAmount=" + rechargeAmount + ", rechargeSourseAccount=" + rechargeSourseAccount
				+ ", bankCode=" + bankCode + ", rechargeAccountBalance=" + rechargeAccountBalance + ", bankAccount="
				+ bankAccount + ", origBusinessId=" + origBusinessId + ", afterId=" + afterId + ", operator=" + operator
				+ ", clientIp=" + clientIp + ", chargeType=" + chargeType + ", rechargeUserId=" + rechargeUserId + "]";
	}

}
