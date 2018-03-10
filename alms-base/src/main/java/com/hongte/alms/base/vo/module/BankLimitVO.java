package com.hongte.alms.base.vo.module;

import java.io.Serializable;

import com.hongte.alms.base.entity.DepartmentBank;

public class BankLimitVO implements Serializable{
	/**银行额度
	 * @author chenzs
	 * @since 2018-03-010
	 */
	private static final long serialVersionUID = -2274331770167647125L;
	private String bankName;
	private String platformName;
	private String onceLimit;
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getOnceLimit() {
		return onceLimit;
	}
	public void setOnceLimit(String onceLimit) {
		this.onceLimit = onceLimit;
	}
	public String getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}
	private String dayLimit;

	
	
}
