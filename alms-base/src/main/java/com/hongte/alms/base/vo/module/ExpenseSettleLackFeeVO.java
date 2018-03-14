/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;

/**
 * @author 王继光
 * 2018年3月12日 下午5:28:14
 */
public class ExpenseSettleLackFeeVO{
	/**
	 * 
	 */
	public ExpenseSettleLackFeeVO(String period,BigDecimal servicecharge,BigDecimal lateFee) {
		super();
		this.lateFee = lateFee ;
		this.period = period ;
		this.servicecharge = servicecharge ;
	}
	/**
	 * 
	 */
	public ExpenseSettleLackFeeVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 期数
	 */
	private String period ;
	/**
	 * 少交的服务费
	 */
	private BigDecimal servicecharge ;
	/**
	 * 少交的滞纳金
	 */
	private BigDecimal lateFee ;
	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}
	/**
	 * @return the servicecharge
	 */
	public BigDecimal getServicecharge() {
		return servicecharge;
	}
	/**
	 * @param servicecharge the servicecharge to set
	 */
	public void setServicecharge(BigDecimal servicecharge) {
		this.servicecharge = servicecharge;
	}
	/**
	 * @return the lateFee
	 */
	public BigDecimal getLateFee() {
		return lateFee;
	}
	/**
	 * @param lateFee the lateFee to set
	 */
	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee;
	}
}
