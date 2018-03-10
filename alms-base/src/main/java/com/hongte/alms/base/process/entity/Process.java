package com.hongte.alms.base.process.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 流程信息
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@ApiModel
@TableName("tb_process")
public class Process extends Model<Process> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    @TableId("process_id")
	@ApiModelProperty(required= true,value = "流程ID")
	private String processId;
    /**
     * 流程名称
     */
	@TableField("process_name")
	@ApiModelProperty(required= true,value = "流程名称")
	private String processName;
    /**
     * 流程注释
     */
	@TableField("process_desc")
	@ApiModelProperty(required= true,value = "流程注释")
	private String processDesc;
    /**
     * 来源流程类别ID
     */
	@TableField("process_typeid")
	@ApiModelProperty(required= true,value = "来源流程类别ID")
	private String processTypeid;
    /**
     * 状态 (0:运行中,1:开始,2:结束,3:注销)
     */
	@ApiModelProperty(required= true,value = "状态 (0:运行中,1:开始,2:结束,3:注销)")
	private Integer status;
    /**
     * 发起人
     */
	@TableField("start_user_id")
	@ApiModelProperty(required= true,value = "发起人")
	private String startUserId;
    /**
     * 发起时间
     */
	@TableField("start_time")
	@ApiModelProperty(required= true,value = "发起时间")
	private Date startTime;
    /**
     * 当前审核人
     */
	@TableField("approve_user_id")
	@ApiModelProperty(required= true,value = "当前审核人")
	private String approveUserId;
    /**
     * 当前待处理节点(运作中的流程才有值)
     */
	@TableField("current_step")
	@ApiModelProperty(required= true,value = "当前待处理节点(运作中的流程才有值)")
	private Integer currentStep;
    /**
     * 回退前得节点(刚刚退回才有)
     */
	@TableField("back_step")
	@ApiModelProperty(required= true,value = "回退前得节点(刚刚退回才有)")
	private Integer backStep;
    /**
     * 是否定向打回(1 是 0 否)(定向打回时候，需要标记定时打回前节点，通过后，直接退回该节点)
     */
	@TableField("is_direct_back")
	@ApiModelProperty(required= true,value = "是否定向打回(1 是 0 否)(定向打回时候，需要标记定时打回前节点，通过后，直接退回该节点)")
	private Integer isDirectBack;
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
    /**
     * 流程引擎标志位
     */
	@TableField("process_engine_flage")
	@ApiModelProperty(required= true,value = "流程引擎标志位")
	private Integer processEngineFlage;

	/**
	 * 流程结果，1：通过， 2：不通过
	 */
	@TableField("process_result")
	@ApiModelProperty(required= true,value = "流程结果，1：通过， 2：不通过")
	private Integer processResult;

	/**
	 * 流程关联的业务编号
	 */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "流程关联的业务编号")
	private String businessId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessDesc() {
		return processDesc;
	}

	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}

	public String getProcessTypeid() {
		return processTypeid;
	}

	public void setProcessTypeid(String processTypeid) {
		this.processTypeid = processTypeid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public Integer getBackStep() {
		return backStep;
	}

	public void setBackStep(Integer backStep) {
		this.backStep = backStep;
	}

	public Integer getIsDirectBack() {
		return isDirectBack;
	}

	public void setIsDirectBack(Integer isDirectBack) {
		this.isDirectBack = isDirectBack;
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

	public Integer getProcessEngineFlage() {
		return processEngineFlage;
	}

	public void setProcessEngineFlage(Integer processEngineFlage) {
		this.processEngineFlage = processEngineFlage;
	}

	@Override
	protected Serializable pkVal() {
		return this.processId;
	}

	@Override
	public String toString() {
		return "Process{" +
			", processId=" + processId +
			", processName=" + processName +
			", processDesc=" + processDesc +
			", processTypeid=" + processTypeid +
			", status=" + status +
			", startUserId=" + startUserId +
			", startTime=" + startTime +
			", approveUserId=" + approveUserId +
			", currentStep=" + currentStep +
			", backStep=" + backStep +
			", isDirectBack=" + isDirectBack +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", processEngineFlage=" + processEngineFlage +
			"}";
	}

	public Integer getProcessResult() {
		return processResult;
	}

	public void setProcessResult(Integer processResult) {
		this.processResult = processResult;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}
