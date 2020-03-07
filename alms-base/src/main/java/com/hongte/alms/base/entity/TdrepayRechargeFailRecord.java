package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-11-14
 */
@ApiModel
@TableName("tb_tdrepay_recharge_fail_record")
public class TdrepayRechargeFailRecord extends Model<TdrepayRechargeFailRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键id")
	private Integer id;
    /**
     * tb_tdrepay_recharge_log表ID
     */
	@TableField("log_id")
	@ApiModelProperty(required= true,value = "tb_tdrepay_recharge_log表ID")
	private String logId;
    /**
     * 标的ID
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 业务编号
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 资产端期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "资产端期数")
	private String afterId;
    /**
     * 期数
     */
	@ApiModelProperty(required= true,value = "期数")
	private Integer period;
    /**
     * 客户姓名
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "客户姓名")
	private String customerName;
    /**
     * 充值存管账户（资金分发失败时记录）
     */
	@TableField("td_user_id")
	@ApiModelProperty(required= true,value = "充值存管账户（资金分发失败时记录）")
	private String tdUserId;
    /**
     * 批次号（资金分发失败时记录）
     */
	@TableField("batch_id")
	@ApiModelProperty(required= true,value = "批次号（资金分发失败时记录）")
	private String batchId;
    /**
     * 订单号（资金分发失败时记录）
     */
	@TableField("request_no")
	@ApiModelProperty(required= true,value = "订单号（资金分发失败时记录）")
	private String requestNo;
    /**
     * 出款方账号（代充值账户，资金分发失败时记录）
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "出款方账号（代充值账户，资金分发失败时记录）")
	private String userId;
    /**
     * 参数json
     */
	@TableField("param_json")
	@ApiModelProperty(required= true,value = "参数json")
	private String paramJson;
    /**
     * 返回代码
     */
	@TableField("result_code")
	@ApiModelProperty(required= true,value = "返回代码")
	private String resultCode;
    /**
     * 返回描述
     */
	@TableField("result_desc")
	@ApiModelProperty(required= true,value = "返回描述")
	private String resultDesc;
    /**
     * 返回json
     */
	@TableField("result_json")
	@ApiModelProperty(required= true,value = "返回json")
	private String resultJson;
    /**
     * 记录类型：1、资金分发状态查询；2、偿还垫付；3、提前结清；4、资金分发
     */
	@TableField("record_type")
	@ApiModelProperty(required= true,value = "记录类型：1、资金分发状态查询；2、偿还垫付；3、提前结清；4、资金分发")
	private Integer recordType;
    /**
     * 是否已发送邮件（0：否；1：是）
     */
	@TableField("is_send_email")
	@ApiModelProperty(required= true,value = "是否已发送邮件（0：否；1：是）")
	private Integer isSendEmail;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

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

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTdUserId() {
		return tdUserId;
	}

	public void setTdUserId(String tdUserId) {
		this.tdUserId = tdUserId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getResultJson() {
		return resultJson;
	}

	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
	}

	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	public Integer getIsSendEmail() {
		return isSendEmail;
	}

	public void setIsSendEmail(Integer isSendEmail) {
		this.isSendEmail = isSendEmail;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeFailRecord{" +
			", id=" + id +
			", logId=" + logId +
			", projectId=" + projectId +
			", businessId=" + businessId +
			", afterId=" + afterId +
			", period=" + period +
			", customerName=" + customerName +
			", tdUserId=" + tdUserId +
			", batchId=" + batchId +
			", requestNo=" + requestNo +
			", userId=" + userId +
			", paramJson=" + paramJson +
			", resultCode=" + resultCode +
			", resultDesc=" + resultDesc +
			", resultJson=" + resultJson +
			", recordType=" + recordType +
			", isSendEmail=" + isSendEmail +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
