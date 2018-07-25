/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王继光 2018年5月14日 下午8:42:42
 */
public class CurrPeriodProjDetailVO {
	private boolean master;
	private String userName;
	private BigDecimal projAmount = new BigDecimal("0");
	//本金
	private BigDecimal item10 = new BigDecimal("0");
	//利息
	private BigDecimal item20 = new BigDecimal("0");
	//分公司服务费
	private BigDecimal item30 = new BigDecimal("0");
	//月收平台费
	private BigDecimal item50 = new BigDecimal("0");
	//线下滞纳金
	private BigDecimal offlineOverDue = new BigDecimal("0");
	//线上滞纳金
	private BigDecimal onlineOverDue = new BigDecimal("0");

	//违约金
	private BigDecimal item70 = new BigDecimal("0");

	private BigDecimal subTotal = new BigDecimal("0");
	private BigDecimal total = new BigDecimal("0");
	//结余
	private BigDecimal surplus = new BigDecimal("0");
	private String project ;
	private Date queryFullSuccessDate ;

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

	public BigDecimal getItem70() {
		return item70;
	}

	public void setItem70(BigDecimal item70) {
		this.item70 = item70;
	}

	public BigDecimal getSubTotal() {
		subTotal = new BigDecimal("0");
		subTotal = subTotal.add(getItem10() == null ? new BigDecimal("0") : getItem10())
				.add(getItem20() == null ? new BigDecimal("0") : getItem20())
				.add(getItem30() == null ? new BigDecimal("0") : getItem30())
				.add(getItem50() == null ? new BigDecimal("0") : getItem50());
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTotal() {
		total = new BigDecimal("0");
		total = total.add(getItem10() == null ? new BigDecimal("0") : getItem10())
				.add(getItem20() == null ? new BigDecimal("0") : getItem20())
				.add(getItem30() == null ? new BigDecimal("0") : getItem30())
				.add(getItem50() == null ? new BigDecimal("0") : getItem50())
				.add(getOfflineOverDue() == null ? new BigDecimal("0") : getOfflineOverDue())
				.add(getOnlineOverDue() == null ? new BigDecimal("0") : getOnlineOverDue())
				.add(getSurplus() == null ? new BigDecimal("0") : getSurplus());
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

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the surplus
	 */
	public BigDecimal getSurplus() {
		return surplus;
	}

	/**
	 * @param surplus the surplus to set
	 */
	public void setSurplus(BigDecimal surplus) {
		this.surplus = surplus;
	}

	/**
	 * @return the queryFullSuccessDate
	 */
	public Date getQueryFullSuccessDate() {
		return queryFullSuccessDate;
	}

	/**
	 * @param queryFullSuccessDate the queryFullSuccessDate to set
	 */
	public void setQueryFullSuccessDate(Date queryFullSuccessDate) {
		this.queryFullSuccessDate = queryFullSuccessDate;
	}
}
