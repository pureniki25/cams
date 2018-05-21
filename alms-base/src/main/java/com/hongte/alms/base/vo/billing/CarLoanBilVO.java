package com.hongte.alms.base.vo.billing;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CarLoanBilVO {
	/**
	 * 预计结清日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT-16")
	private Date billDate;
	
	
	private Integer currentPriod;//减免申请的期数
	
	private double needPayInterest;//减免申请的当期应还利息 
	/**
	 * 期内滞纳金
	 */
	private double innerLateFees;
	/**
	 * 期外逾期利息
	 */
	private double outsideInterest;
	/**
	 * 提前还款违约金
	 */	
	private int preLateFees;
	/**
	 * 停车费
	 */
	private double parkingFees;
	/**
	 * GPS费
	 */
	private double gpsFees;
	/**
	 * 拖车费
	 */
	private double dragFees;
	/**
	 * 其他费用
	 */
	private double otherFees;
	/**
	 * 律师费
	 */
	private double attorneyFees;
	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 还款计划列表ID
	 */
	private String crpId;

	public double getNeedPayInterest() {
		return needPayInterest;
	}

	public void setNeedPayInterest(double needPayInterest) {
		this.needPayInterest = needPayInterest;
	}

	public Integer getCurrentPriod() {
		return currentPriod;
	}

	public void setCurrentPriod(Integer currentPriod) {
		this.currentPriod = currentPriod;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public double getInnerLateFees() {
		return innerLateFees;
	}

	public void setInnerLateFees(double innerLateFees) {
		this.innerLateFees = innerLateFees;
	}

	public double getOutsideInterest() {
		return outsideInterest;
	}

	public void setOutsideInterest(double outsideInterest) {
		this.outsideInterest = outsideInterest;
	}

	public int getPreLateFees() {
		return preLateFees;
	}

	public void setPreLateFees(int preLateFees) {
		this.preLateFees = preLateFees;
	}

	public double getParkingFees() {
		return parkingFees;
	}

	public void setParkingFees(double parkingFees) {
		this.parkingFees = parkingFees;
	}

	public double getGpsFees() {
		return gpsFees;
	}

	public void setGpsFees(double gpsFees) {
		this.gpsFees = gpsFees;
	}

	public double getDragFees() {
		return dragFees;
	}

	public void setDragFees(double dragFees) {
		this.dragFees = dragFees;
	}

	public double getOtherFees() {
		return otherFees;
	}

	public void setOtherFees(double otherFees) {
		this.otherFees = otherFees;
	}

	public double getAttorneyFees() {
		return attorneyFees;
	}

	public void setAttorneyFees(double attorneyFees) {
		this.attorneyFees = attorneyFees;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	@Override
	public String toString() {
		return "CarLoanBilVO [billDate=" + billDate + ", innerLateFees=" + innerLateFees + ", outsideInterest="
				+ outsideInterest + ", preLateFees=" + preLateFees + ", parkingFees=" + parkingFees + ", gpsFees="
				+ gpsFees + ", dragFees=" + dragFees + ", otherFees=" + otherFees + ", attorneyFees=" + attorneyFees
				+ ", businessId=" + businessId + ", crpId=" + crpId + "]";
	}

}
