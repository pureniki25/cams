/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 王继光 2018年5月3日 下午3:10:11
 */
public class FinanceManagerListVO {

	// 业务编号 期数 部门 主办 客户姓名 业务类型 还款日期 还款登记日期 还款金额 会计确认状态 状态 是否支持代扣
	private String planId ;
	/**
	 * 还款计划id
	 */
	private String planListId;
	/**
	 * 业务编号
	 */
	private String businessId;
	private String orgBusinessId;
	/**
	 * 期数
	 */
	private String afterId;
	private Integer period ;
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
	 * 借款金额
	 */
	private String borrowMoney;
	/**
	 * 还款方式
	 */
	private String repaymentType;
	/**
	 * 借款期限
	 */
	private String borrowLimit;
	/**
	 * 手机号码
	 */
	private String phoneNumber;

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
	 * 已还金额
	 */
	private BigDecimal repaidAmount;
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

	/**
	 * 代扣确认状态
	 */
	private Integer confirmFlag ;
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
	 * @param planListId
	 *            the planListId to set
	 */
	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(String borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(String borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the planId
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
	 * @return the confirmFlag
	 */
	public Integer getConfirmFlag() {
		return confirmFlag;
	}

	/**
	 * @param confirmFlag the confirmFlag to set
	 */
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	/**
	 * @return the orgBusinessId
	 */
	public String getOrgBusinessId() {
		return orgBusinessId;
	}

	/**
	 * @param orgBusinessId the orgBusinessId to set
	 */
	public void setOrgBusinessId(String orgBusinessId) {
		this.orgBusinessId = orgBusinessId;
	}

	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}

	/**
	 * @return the repaidAmount
	 */
	public BigDecimal getRepaidAmount() {
		return repaidAmount;
	}

	/**
	 * @param repaidAmount the repaidAmount to set
	 */
	public void setRepaidAmount(BigDecimal repaidAmount) {
		this.repaidAmount = repaidAmount;
	}

}
