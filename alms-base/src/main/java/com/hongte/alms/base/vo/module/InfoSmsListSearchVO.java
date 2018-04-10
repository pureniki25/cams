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

    private String businessTypeName;
    

	public String getBusinessTypeName() {
		return businessTypeName;
	}


	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}


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
	private String companyName ;
	
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
		switch(smsType){
		case "200115":
			this.smsType="还款提前15天发短信给客户";
			break;
		case "200201":
			this.smsType="还款提前1天发短信给业务获取人";
			break;
		case "20001":
			this.smsType="借款成功";
			break;
		case "20002":
			this.smsType="借款审核失败";
			break;
		case "20005":
			this.smsType="放款异常";
			break;
		case "1":
			this.smsType="扣款成功";
			break;
		case "2":
			this.smsType="扣款失败";
			break;
		case "5":
			this.smsType="贷款还款日当天发短信给业务获取人";
			break;
		case "6":
			this.smsType="贷款还款日逾期三天发短信给业务获取人";
			break;
		case "0":
			this.smsType="贷款还款提醒提前3天";
			break;
		case "3":
			this.smsType="贷款还款提醒发给业务员的";
			break;
		case "200107":
			this.smsType="还款提前7天发短信给客户";
			break;
		case "4":
			this.smsType="贷款还款日提前3天发短信给业务获取人";
			break;
		case "100":
			this.smsType="结清申请";
			break;
		case "101":
			this.smsType="展期申请";
			break;
		case "103":
			this.smsType="代扣主卡变更通知";
			break;
		case "1000":
			this.smsType="普通短信";
			break;
		case "200101":
			this.smsType="还款提前1天发短信给客户";
			break;
		case "-1":
			this.smsType="还款逾期1天发短信给客户";
			break;
		case "-15":
			this.smsType="还款逾期15天发短信给客户";
			break;
		case "-16":
			this.smsType="还款逾期16天发短信给客户";
			break;
		case "200215":
			this.smsType="还款提前15天发短信给业务获取人";
			break;
		case "200207":
			this.smsType="还款提前7天发短信给业务获取人";
			break;
			
	}
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


	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}


	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


}
