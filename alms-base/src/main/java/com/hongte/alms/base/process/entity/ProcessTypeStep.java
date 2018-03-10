package com.hongte.alms.base.process.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 流程类型步骤
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@ApiModel
@TableName("tb_process_type_step")
public class ProcessTypeStep extends Model<ProcessTypeStep> {

    private static final long serialVersionUID = 1L;

    /**
     * 类型步骤节点ID
     */
    @TableId("type_step_id")
	@ApiModelProperty(required= true,value = "类型步骤节点ID")
	private String typeStepId;
    /**
     * 类型ID
     */
	@TableField("type_id")
	@ApiModelProperty(required= true,value = "类型ID")
	private String typeId;
    /**
     * 步骤名称
     */
	@TableField("step_name")
	@ApiModelProperty(required= true,value = "步骤名称")
	private String stepName;
    /**
     * 待审核人ID
     */
	@TableField("approve_user_id")
	@ApiModelProperty(required= true,value = "待审核人ID")
	private String approveUserId;
    /**
     * 当前审核人类型 1固定审核人 2发起人 3动态SQL获取
     */
	@TableField("approve_user_type")
	@ApiModelProperty(required= true,value = "当前审核人类型 1固定审核人 2发起人 3动态SQL获取")
	private Integer approveUserType;
    /**
     * 通过动态执行SQL来获取当前审核人ID(预留以后扩展用)
     */
	@TableField("approve_user_id_select_sql")
	@ApiModelProperty(required= true,value = "通过动态执行SQL来获取当前审核人ID(预留以后扩展用)")
	private String approveUserIdSelectSql;
    /**
     * 该节点是否允许编辑业务
     */
	@TableField("is_can_edit")
	@ApiModelProperty(required= true,value = "该节点是否允许编辑业务")
	private Integer isCanEdit;
    /**
     * 所在步骤
     */
	@ApiModelProperty(required= true,value = "所在步骤")
	private Integer step;
    /**
     * 正常审核通过，下一步节点
     */
	@TableField("next_step")
	@ApiModelProperty(required= true,value = "正常审核通过，下一步节点")
	private Integer nextStep;
    /**
     * 通过动态执行SQL来获取下一步处理节点(预留以后扩展用)
     */
	@TableField("next_step_select_sql")
	@ApiModelProperty(required= true,value = "通过动态执行SQL来获取下一步处理节点(预留以后扩展用)")
	private String nextStepSelectSql;
//    /**
//     * 是否允许节点向前走
//     */
//	@TableField("is_can_forward")
//	@ApiModelProperty(required= true,value = "是否允许节点向前走")
//	private Integer isCanForward;
//    /**
//     * 是否允许节点向后走
//     */
//	@TableField("is_can_back")
//	@ApiModelProperty(required= true,value = "是否允许节点向后走")
//	private Integer isCanBack;
	/**
	 * 步骤类型：1，起始节点；2，中间节点，3，结束节点
	 */
	@TableField("step_type")
	@ApiModelProperty(required= true,value = "步骤类型：1，起始节点；2，中间节点，3，结束节点")
	private Integer stepType;


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

	public Integer getApproveUserType() {
		return approveUserType;
	}

	public void setApproveUserType(Integer approveUserType) {
		this.approveUserType = approveUserType;
	}

	public String getApproveUserIdSelectSql() {
		return approveUserIdSelectSql;
	}

	public void setApproveUserIdSelectSql(String approveUserIdSelectSql) {
		this.approveUserIdSelectSql = approveUserIdSelectSql;
	}

	public Integer getIsCanEdit() {
		return isCanEdit;
	}

	public void setIsCanEdit(Integer isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getNextStep() {
		return nextStep;
	}

	public void setNextStep(Integer nextStep) {
		this.nextStep = nextStep;
	}

	public String getNextStepSelectSql() {
		return nextStepSelectSql;
	}

	public void setNextStepSelectSql(String nextStepSelectSql) {
		this.nextStepSelectSql = nextStepSelectSql;
	}


	@Override
	protected Serializable pkVal() {
		return this.typeStepId;
	}

	@Override
	public String toString() {
		return "ProcessTypeStep{" +
			", typeStepId=" + typeStepId +
			", typeId=" + typeId +
			", stepName=" + stepName +
			", approveUserId=" + approveUserId +
			", approveUserType=" + approveUserType +
			", approveUserIdSelectSql=" + approveUserIdSelectSql +
			", isCanEdit=" + isCanEdit +
			", step=" + step +
			", nextStep=" + nextStep +
			", nextStepSelectSql=" + nextStepSelectSql +
			",stepType="+stepType+
			"}";
	}

	public Integer getStepType() {
		return stepType;
	}

	public void setStepType(Integer stepType) {
		this.stepType = stepType;
	}
}
