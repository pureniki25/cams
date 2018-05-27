/**
 * 
 */
package com.hongte.alms.base.enums;

/**
 * @author 王继光
 * 2018年5月19日 下午3:46:13
 */
public enum RepayedFlag {
	REPAYING(0,""),
	/**
	 * 申请展期已还款
	 */
	APPLY_RENEW(6,"申请展期已还款"),
	/**
	 * 线下确认已还款
	 */
	CONFIRM_OFFLINE_REPAYED(10,"线下确认已还款"),
	/**
	 * 自动线下代扣已还款
	 */
	AUTO_WITHHOLD_OFFLINE_REPAYED(20,"自动线下代扣已还款"),
	/**
	 * 人工线下代扣已还款
	 */
	MANUAL_WITHHOLD_OFFLINE_REPAYED(21,"人工线下代扣已还款"),
	/**
	 * 自动银行代扣已还款
	 */
	AUTO_BANK_WITHHOLD_REPAYED(30,"自动银行代扣已还款"),
	/**
	 * 人工银行代扣已还款
	 */
	MANUA_BANKL_WITHHOLD_REPAYED(31,"人工银行代扣已还款"),
	;

	private int key ;
	private String name ;
	/**
	 * @param name
	 * @param ordinal
	 */
	RepayedFlag(int key, String name) {
		this.key = key ;
		this.name = name ;
	}
	/*0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款*/
	public String getName() {
		return this.name ;
	}
	public int getKey() {
		return this.key ;
	}
}
