package com.hongte.alms.base.vo.compliance;

import java.io.Serializable;

import com.hongte.alms.base.entity.AgencyRechargeLog;

public class AgencyRechargeLogVO extends AgencyRechargeLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 充值银行名称
	 */
	private String bankName;

	/**
	 * 充值来源银行卡（后4位）
	 */
	private String subBankAccount;

	/**
	 * 处理状态 1处理中，2成功，3失败
	 */
	private String handleStatusStr;

	/**
	 * 创建人姓名
	 */
	private String createUsername;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSubBankAccount() {
		return subBankAccount;
	}

	public void setSubBankAccount(String subBankAccount) {
		this.subBankAccount = subBankAccount;
	}

	public String getHandleStatusStr() {
		return handleStatusStr;
	}

	public void setHandleStatusStr(String handleStatusStr) {
		this.handleStatusStr = handleStatusStr;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	@Override
	public String toString() {
		return "AgencyRechargeLogVO [bankName=" + bankName + ", subBankAccount=" + subBankAccount + ", handleStatusStr="
				+ handleStatusStr + ", createUsername=" + createUsername + "]";
	}

}
