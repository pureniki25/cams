package com.hongte.alms.base.vo.module;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hongte.alms.base.process.enums.ProcessApproveUserType;
import com.hongte.alms.common.util.Constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 流程类型步骤
 * </p>
 *
 * @author chenzs
 * @since 2018-03-29
 */
public class ProcessTypeStepVO extends Model<ProcessTypeStepVO> {

    private static final long serialVersionUID = 1L;
    
    
    private String stepTypeName;
    
    public String getStepTypeName() {
		return stepTypeName;
	}

	public void setStepTypeName(String stepTypeName) {
		this.stepTypeName = stepTypeName;
	}

	/**
     * 类型步骤节点ID
     */
	private String typeStepId;
    /**
     * 类型ID
     */
	private String typeId;
    /**
     * 步骤名称
     */
	private String stepName;
	
	
    /**
     * 流程类型名称
     */
	private String typeName;
	
	
    /**
     * 审核人类型名称
     */
	private String approveUserTypeName;
	
	
    /**
     * 审核人
     */
	private String approveUserName;
    /**
     * 待审核人ID
     */
	private String approveUserId;
    /**
     * 当前审核人类型 1固定审核人 2发起人 3动态SQL获取
     */
	private Integer approveUserType;
    /**
     * 通过动态执行SQL来获取当前审核人ID(预留以后扩展用)
     */
	private String approveUserIdSelectSql;
    /**
     * 该节点是否允许编辑业务
     */
	private Integer isCanEdit;
	
    /**
     * 该节点是否允许编辑业务
     */
	private String isCanEditName;
    /**
     * 所在步骤
     */
	private Integer step;
    /**
     * 正常审核通过，下一步节点
     */
	private Integer nextStep;
    /**
     * 通过动态执行SQL来获取下一步处理节点(预留以后扩展用)
     */
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
	private Integer stepType;

	/**
	 * 审批者角色定义（通过角色定义来获取审批人 当approve_user_type 为5时使用）
	 */
	private String approveUserRole;
   private String userName;

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
		
     if(ProcessApproveUserType.FIXED.getKey()==approveUserType) {
    	 setApproveUserTypeName(ProcessApproveUserType.FIXED.getName());
     }
     if(ProcessApproveUserType.CREATER.getKey()==approveUserType) {
    	 setApproveUserTypeName(ProcessApproveUserType.CREATER.getName());
     }
     if(ProcessApproveUserType.BY_SQL.getKey()==approveUserType) {
    	 setApproveUserTypeName(ProcessApproveUserType.BY_SQL.getName());
     }
     if(ProcessApproveUserType.BY_ROLE.getKey()==approveUserType) {
    	 setApproveUserTypeName(ProcessApproveUserType.BY_ROLE.getName());
     }
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
		if(isCanEdit==0) {
			setIsCanEditName("不允许");
		}else {
			setIsCanEditName("允许");
		}
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
			", approveUserRole=" + approveUserRole +
			"}";
	}
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getApproveUserTypeName() {
		return approveUserTypeName;
	}

	public void setApproveUserTypeName(String approveUserTypeName) {
		this.approveUserTypeName = approveUserTypeName;
	}

	public String getApproveUserName() {
		return approveUserName;
	}

	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}

	public String getIsCanEditName() {
		return isCanEditName;
	}

	public void setIsCanEditName(String isCanEditName) {
		this.isCanEditName = isCanEditName;
	}

	public Integer getStepType() {
		return stepType;
	}

	public void setStepType(Integer stepType) {
		if(stepType==1) {
			setStepTypeName("起始节点");
		}
		if(stepType==2) {
			setStepTypeName("中间节点");
		}
		if(stepType==3) {
			setStepTypeName("结束节点");
		}
		this.stepType = stepType;
	}

	public void setApproveUserRole(String approveUserRole) {
		this.approveUserRole = approveUserRole;
	}

	public String getApproveUserRole(){
		return this.approveUserRole;
	}
}
