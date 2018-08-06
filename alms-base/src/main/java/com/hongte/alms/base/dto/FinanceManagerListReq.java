/**
 * 
 */
package com.hongte.alms.base.dto;

/**
 * @author 王继光
 * 2018年5月3日 下午3:20:20
 */
public class FinanceManagerListReq {

	private int curPage ;
	private int pageSize ;
	/**
	 * 客户姓名
	 */
	private String customer ;
	/**
	 * businessid
	 */
	private String businessId ;
	/**
	 * 是否本金到期
	 */
	private boolean principalDeadLine ;
	
	/**
	 * 会计确认状态
	 */
	private String accountantConfirmStatus ;
	/**
	 * 区域
	 */
	private String areaId ;
	/**
	 * 分公司
	 */
	private String companyId ;
	/**
	 * 业务类型
	 */
	private String businessType;
	/**
	 * 还款日期
	 */
	private String repayDate ;
	/**
	 * 还款登记日期起
	 */
	private String regDateStart ;
	/**
	 * 还款登记日期止
	 */
	private String regDateEnd ;
	/**
	 * 贷后状态
	 */
	private String status ;
	
	/**
	 * 当前操作人
	 */
	private String userId ;
	
	/**
	 * 电话号码
	 */
	private String phoneNumber;
	
	/**
	 * 所属投资端
	 */
	private String paymentPlatform;

	/**
	 * 所属投资端编码
	 */
	private Integer paymentPlatformCode;
	
	private Integer needPermission = 1;
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public boolean isPrincipalDeadLine() {
		return principalDeadLine;
	}
	public void setPrincipalDeadLine(boolean principalDeadLine) {
		this.principalDeadLine = principalDeadLine;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getRegDateStart() {
		return regDateStart;
	}
	public void setRegDateStart(String regDateStart) {
		this.regDateStart = regDateStart;
	}
	public String getRegDateEnd() {
		return regDateEnd;
	}
	public void setRegDateEnd(String regDateEnd) {
		this.regDateEnd = regDateEnd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the curPage
	 */
	public int getCurPage() {
		return curPage;
	}
	/**
	 * @param curPage the curPage to set
	 */
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	/**
	 * @return the accountantConfirmStatus
	 */
	public String getAccountantConfirmStatus() {
		return accountantConfirmStatus;
	}
	/**
	 * @param accountantConfirmStatus the accountantConfirmStatus to set
	 */
	public void setAccountantConfirmStatus(String accountantConfirmStatus) {
		this.accountantConfirmStatus = accountantConfirmStatus;
	}
	/**
	 * @return the areaId
	 */
	public String getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getNeedPermission() {
		return needPermission;
	}
	public void setNeedPermission(Integer needPermission) {
		this.needPermission = needPermission;
	}
	public String getPaymentPlatform() {
		return paymentPlatform;
	}
	public void setPaymentPlatform(String paymentPlatform) {
		this.paymentPlatform = paymentPlatform;
	}
	public Integer getPaymentPlatformCode() {
		return paymentPlatformCode;
	}
	public void setPaymentPlatformCode(Integer paymentPlatformCode) {
		this.paymentPlatformCode = paymentPlatformCode;
	}
	
}
