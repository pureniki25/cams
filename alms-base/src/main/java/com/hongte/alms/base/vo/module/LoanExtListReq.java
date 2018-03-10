package com.hongte.alms.base.vo.module;

import java.util.Date;

import com.hongte.alms.common.vo.PageRequest;

import io.swagger.annotations.ApiModel;

@ApiModel(value="展期信息列表页查询请求对象",description="展期信息列表页查询请求对象")
public class LoanExtListReq extends PageRequest{
	private Date dateStart ;
	private Date dateEnd ;
	private String deptId ;
	private String businessId ;
	private String salesman ;
	private String customer ;
	private String businessType ;
	private String extensionId ;
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getExtensionId() {
		return extensionId;
	}
	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}
}
