package com.hongte.alms.base.vo.module;


import java.util.Date;

/**
 * @author chenzs
 * @since 2018/3/03
 */
public class InfoSmsListSearchVO {

    private static final long serialVersionUID = 1L;



    /**
     * 短信ID
     */
	private String logId;
    /**
     * plan ID
     */
	private String planListId;
    /**
     * 业务ID
     */
	private String originalBusinessId;
    /**
     * after ID
     */
	private String afterId;
    /**
     * 手机号码
     */
	private String phoneNumber;
    /**
     * 短信类型
     */
	private String smsType;
    /**
     * 发送时间
     */
	private Date sendDate;
    /**
     * 短信内容
     */
	private String content;
    /**
     * 服务商名称
     */
	private String serviceName;
    /**
     * 状态
     */
	private String status;

    /**
     * 服务商状态代码
     */
	private String serviceStatusCode;
    /**
     * 创建时间
     */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createUser;
	 /**
     * 更新时间
     */
	private Date updateTime;
	 /**
     * 更新人
     */
	private String updateUser;
	
	 /**
     * 短信接收人
     */
	private String recipient;
	
	 /**
    * businessTypeId
    */
	private Integer businessTypeId;



	public Integer getBusinessTypeId() {
		return businessTypeId;
	}


	public void setBusinessTypeId(Integer businessTypeId) {
		this.businessTypeId = businessTypeId;
	}


	public String getDistrictId() {
		return districtId;
	}


	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}


	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	private String districtId;
	private String companyId;
	
	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	



	public String getLogId() {
		return logId;
	}


	public void setLogId(String logId) {
		this.logId = logId;
	}


	public String getPlanListId() {
		return planListId;
	}


	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}


	public String getOriginalBusinessId() {
		return originalBusinessId;
	}


	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}


	public String getAfterId() {
		return afterId;
	}


	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getSmsType() {
		return smsType;
	}


	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}


	public Date getSendDate() {
		return sendDate;
	}


	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getServiceStatusCode() {
		return serviceStatusCode;
	}


	public void setServiceStatusCode(String serviceStatusCode) {
		this.serviceStatusCode = serviceStatusCode;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getUpdateUser() {
		return updateUser;
	}


	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


}
