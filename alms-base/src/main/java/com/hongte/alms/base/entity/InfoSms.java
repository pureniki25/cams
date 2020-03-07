package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author chenzs
 * @since 2018-03-02
 */
@ApiModel
@TableName("tb_sms_log")
public class InfoSms extends Model<InfoSms> {

    private static final long serialVersionUID = 1L;



    /**
     * 短信ID
     */
    @TableId(value="log_id", type= IdType.INPUT)
	@ApiModelProperty(required= true,value = "短信ID")
	private Integer logId;
    /**
     * plan ID
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "planListId")
	private String planListId;
    /**
     * 业务ID
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "业务ID")
	private String originalBusinessId;
    /**
     * after ID
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "after id")
	private String afterId;
    /**
     * 手机号码
     */
	@TableField("phone_number")
	@ApiModelProperty(required= true,value = "手机号码")
	private String phoneNumber;
    /**
     * 短信类型
     */
	@TableField("sms_type")
	@ApiModelProperty(required= true,value = "短信类型")
	private String smsType;
    /**
     * 发送时间
     */
	@TableField("send_date")
	@ApiModelProperty(required= true,value = "发送时间")
	private Date sendDate;
    /**
     * 短信内容
     */
	@TableField("content")
	@ApiModelProperty(required= true,value = "发送内容")
	private String content;
    /**
     * 服务商名称
     */
	@TableField("service_name")
	@ApiModelProperty(required= true,value = "服务商名称")
	private String serviceName;
    /**
     * 状态
     */
	@TableField("status")
	@ApiModelProperty(required= true,value = "状态")
	private String status;

    /**
     * 服务商状态代码
     */
	@TableField("service_status_code")
	@ApiModelProperty(required= true,value = "服务商状态代码")
	private String serviceStatusCode;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
	 /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
	 /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
	
	 /**
     * 短信接收人
     */
	@TableField("recipient")
	@ApiModelProperty(required= true,value = "短信接收人")
	private String recipient;

	
	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	@Override
	public String toString() {
		return "InfoSms{" +
			"logId=" + logId +
			", planListId=" + planListId +
			", originalBusinessId=" + originalBusinessId +
			", afterId=" + afterId +
			", phoneNumber=" + phoneNumber +
			", smsType=" + smsType +
			", sendDate=" + sendDate +
			", content=" + content +
			", serviceName=" + serviceName +
			", status=" + status +
			", serviceStatusCode=" + serviceStatusCode +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", recipient=" + recipient +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}


	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}


	public Integer getLogId() {
		return logId;
	}


	public void setLogId(Integer logId) {
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
