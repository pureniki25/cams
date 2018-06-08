/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import com.hongte.alms.base.entity.MoneyPool;

/**
 * @author 王继光
 * 2018年6月8日 上午11:37:57
 */
public class MoneyPoolManagerVO extends MoneyPool{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4925859164686479303L;

	/**
	 * 银行账号
	 */
	private String bankAccount ;

	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
}
