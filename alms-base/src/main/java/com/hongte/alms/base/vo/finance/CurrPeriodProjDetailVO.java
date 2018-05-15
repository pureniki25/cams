/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;

/**
 * @author 王继光
 * 2018年5月14日 下午8:42:42
 */
public class CurrPeriodProjDetailVO {
	private boolean master ;
	private String userName;
	private BigDecimal projAmount ;
	private BigDecimal item10 ;
	private BigDecimal item20 ;
	private BigDecimal item30 ;
	private BigDecimal item50 ;
	private BigDecimal offlineOverDue ;
	private BigDecimal onlineOverDue ;
	private BigDecimal subTotal ;
	private BigDecimal total ;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getProjAmount() {
		return projAmount;
	}
	public void setProjAmount(BigDecimal projAmount) {
		this.projAmount = projAmount;
	}
	public BigDecimal getItem10() {
		return item10;
	}
	public void setItem10(BigDecimal item10) {
		this.item10 = item10;
	}
	public BigDecimal getItem20() {
		return item20;
	}
	public void setItem20(BigDecimal item20) {
		this.item20 = item20;
	}
	public BigDecimal getItem30() {
		return item30;
	}
	public void setItem30(BigDecimal item30) {
		this.item30 = item30;
	}
	public BigDecimal getItem50() {
		return item50;
	}
	public void setItem50(BigDecimal item50) {
		this.item50 = item50;
	}
	public BigDecimal getOfflineOverDue() {
		return offlineOverDue;
	}
	public void setOfflineOverDue(BigDecimal offlineOverDue) {
		this.offlineOverDue = offlineOverDue;
	}
	public BigDecimal getOnlineOverDue() {
		return onlineOverDue;
	}
	public void setOnlineOverDue(BigDecimal onlineOverDue) {
		this.onlineOverDue = onlineOverDue;
	}
	public BigDecimal getSubTotal() {
		subTotal = new BigDecimal(0);
		subTotal=subTotal.add(getItem10()).add(getItem20()).add(getItem30()).add(getItem50()) ;
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getTotal() {
		total = new BigDecimal(0);
		total=total.add(getItem10()).add(getItem20()).add(getItem30()).add(getItem50())
				.add(getOfflineOverDue()).add(getOnlineOverDue());
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
}
