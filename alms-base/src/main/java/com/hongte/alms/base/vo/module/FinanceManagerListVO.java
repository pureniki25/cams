/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 王继光
 * 2018年5月3日 下午3:10:11
 */
public class FinanceManagerListVO {

//	业务编号	期数	部门	主办	客户姓名	业务类型	还款日期	还款登记日期	还款金额	会计确认状态	状态	是否支持代扣
	
	/**
	 * 还款计划id
	 */
	private String planListId ;
	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 期数
	 */
	private String afterId;
	/**
	 * 部门
	 */
	private String dept;
	/**
	 * 主办
	 */
	private String operator;
	/**
	 * 客户姓名
	 */
	private String customer;
	/**
	 * 业务类型
	 */
	private String businessType;
	
	/**
	 * 还款日期
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date planRepayDate;
	/**
	 * 还款登记日期
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date factRepayDate;
	
	/**
	 * 还款金额
	 */
	private BigDecimal planRepayAmount;
	/**
	 * 会计确认状态
	 */
	private String financeConfirmStatus;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 是否支持代扣
	 */
	private boolean canWithhold;
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public Date getPlanRepayDate() {
		return planRepayDate;
	}
	public void setPlanRepayDate(Date planRepayDate) {
		this.planRepayDate = planRepayDate;
	}
	public Date getFactRepayDate() {
		return factRepayDate;
	}
	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}
	public BigDecimal getPlanRepayAmount() {
		return planRepayAmount;
	}
	public void setPlanRepayAmount(BigDecimal planRepayAmount) {
		this.planRepayAmount = planRepayAmount;
	}
	public String getFinanceConfirmStatus() {
		return financeConfirmStatus;
	}
	public void setFinanceConfirmStatus(String financeConfirmStatus) {
		this.financeConfirmStatus = financeConfirmStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isCanWithhold() {
		return canWithhold;
	}
	public void setCanWithhold(boolean canWithhold) {
		this.canWithhold = canWithhold;
	}
	/**
	 * @return the planListId
	 */
	public String getPlanListId() {
		return planListId;
	}
	/**
	 * @param planListId the planListId to set
	 */
	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}
}
