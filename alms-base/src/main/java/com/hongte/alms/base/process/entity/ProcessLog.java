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
 * 流程运行日志
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@ApiModel
@TableName("tb_process_log")
public class ProcessLog extends Model<ProcessLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程运行日志ID
     */
    @TableId("process_log_id")
	@ApiModelProperty(required= true,value = "流程运行日志ID")
	private String processLogId;
    /**
     * 流程ID
     */
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "流程ID")
	private String processId;
    /**
     * 流程名称
     */
	@TableField("process_name")
	@ApiModelProperty(required= true,value = "流程名称")
	private String processName;
    /**
     * 流程节点ID
     */
	@TableField("type_step_id")
	@ApiModelProperty(required= true,value = "流程节点ID")
	private String typeStepId;
    /**
     * 流程类型ID
     */
	@TableField("type_id")
	@ApiModelProperty(required= true,value = "流程类型ID")
	private String typeId;
    /**
     * 流程节点名称
     */
	@TableField("step_name")
	@ApiModelProperty(required= true,value = "流程节点名称")
	private String stepName;
    /**
     * 流程审核人ID
     */
	@TableField("approve_user_id")
	@ApiModelProperty(required= true,value = "流程审核人ID")
	private String approveUserId;
    /**
     * 当前步骤节点
     */
	@TableField("current_step")
	@ApiModelProperty(required= true,value = "当前步骤节点")
	private Integer currentStep;
    /**
     * 审核是否通过 1通过 2不通过
     */
	@TableField("is_pass")
	@ApiModelProperty(required= true,value = "审核是否通过 1通过 2不通过")
	private Integer isPass;
    /**
     * 人工填写审核意见
     */
	@ApiModelProperty(required= true,value = "人工填写审核意见")
	private String remark;
    /**
     * 操作备注
     */
	@TableField("action_desc")
	@ApiModelProperty(required= true,value = "操作备注")
	private String actionDesc;
    /**
     * 下一个处理节点
     */
	@TableField("next_step")
	@ApiModelProperty(required= true,value = "下一个处理节点")
	private Integer nextStep;
    /**
     * 回退节点
     */
	@TableField("back_step")
	@ApiModelProperty(required= true,value = "回退节点")
	private Integer backStep;
    /**
     * 是否定向打回(1 是 0 否)
     */
	@TableField("is_direct_back")
	@ApiModelProperty(required= true,value = "是否定向打回(1 是 0 否)")
	private Integer isDirectBack;
    /**
     * 是否是由发起节点生成的节点数据 1是 0否
     */
	@TableField("is_send_from_start")
	@ApiModelProperty(required= true,value = "是否是由发起节点生成的节点数据 1是 0否")
	private Integer isSendFromStart;
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
	 * 抄送描述
	 */
	@TableField("copy_send_info")
	@ApiModelProperty(required= true,value = "抄送描述")
	private String copySendInfo;



	public String getProcessLogId() {
		return processLogId;
	}

	public void setProcessLogId(String processLogId) {
		this.processLogId = processLogId;
	}

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

	public String getTypeStepId() {
		return typeStepId;
	}

	public void setTypeStepId(String typeStepId) {
		this.typeStepId = typeStepId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
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

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public Integer getNextStep() {
		return nextStep;
	}

	public void setNextStep(Integer nextStep) {
		this.nextStep = nextStep;
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

	public Integer getIsSendFromStart() {
		return isSendFromStart;
	}

	public void setIsSendFromStart(Integer isSendFromStart) {
		this.isSendFromStart = isSendFromStart;
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

	@Override
	protected Serializable pkVal() {
		return this.processLogId;
	}

	@Override
	public String toString() {
		return "ProcessLog{" +
			", processLogId=" + processLogId +
			", processId=" + processId +
			", processName=" + processName +
			", typeStepId=" + typeStepId +
			", typeId=" + typeId +
			", stepName=" + stepName +
			", approveUserId=" + approveUserId +
			", currentStep=" + currentStep +
			", isPass=" + isPass +
			", remark=" + remark +
			", actionDesc=" + actionDesc +
			", nextStep=" + nextStep +
			", backStep=" + backStep +
			", isDirectBack=" + isDirectBack +
			", isSendFromStart=" + isSendFromStart +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}

	public String getCopySendInfo() {
		return copySendInfo;
	}

	public void setCopySendInfo(String copySendInfo) {
		this.copySendInfo = copySendInfo;
	}

}
