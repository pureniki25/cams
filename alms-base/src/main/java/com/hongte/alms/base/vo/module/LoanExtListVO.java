package com.hongte.alms.base.vo.module;

/**
 * @author 王继光
 *
 * @since 2018年3月6日 下午2:24:32
 */
public class LoanExtListVO {

	/**
	 * 展期id
	 */
	private String id ;
	/**
	 * 客户姓名
	 */
	private String customer ;
	/**
	 * 业务员
	 */
	private String salesman ;
	/**
	 * 业务类型
	 */
	private String businessType ;
	/**
	 * 借款金额
	 */
	private String loanAmount ;
	/**
	 * 借款期限
	 */
	private String loanTerm ;
	/**
	 * 借款期限单位，1:月,2:天
	 */
	private String loanTermUnit ;
	/**
	 * 已还本金
	 */
	private String repayAmount ;
	/**
	 * 展期金额
	 */
	private String extensionAmount ;
	/**
	 * 展期期限
	 */
	private String extensionTerm ;
	/**
	 * 展期状态
	 */
	private String status ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getLoanTerm() {
		return loanTerm;
	}
	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}
	public String getRepayAmount() {
		return repayAmount;
	}
	public void setRepayAmount(String repayAmount) {
		this.repayAmount = repayAmount;
	}
	public String getExtensionAmount() {
		return extensionAmount;
	}
	public void setExtensionAmount(String extensionAmount) {
		this.extensionAmount = extensionAmount;
	}
	public String getExtensionTerm() {
		return extensionTerm;
	}
	public void setExtensionTerm(String extensionTerm) {
		this.extensionTerm = extensionTerm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLoanTermUnit() {
		return loanTermUnit;
	}
	public void setLoanTermUnit(String loanTermUnit) {
		this.loanTermUnit = loanTermUnit;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
}
