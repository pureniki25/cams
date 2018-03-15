package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="审核信息",description="审核信息")
public class AuditVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	@ApiModelProperty(required= true,value = "流程ID")
	private String processId;
	@ApiModelProperty(required= true,value = "当前待处理节点(运作中的流程才有值)")
	private Integer currentStep;
	
	@ApiModelProperty(required= true,value = "是否定向打回(1 是 0 否)")
	private Integer isDirectBack;
	
	@ApiModelProperty(required= true,value = "下一个处理节点")
	private Integer nextStep;
	
	@ApiModelProperty(required= true,value = "回退前得节点(刚刚退回才有)")
	private Integer backStep;
	
	@ApiModelProperty(required= true,value = "审核是否通过 1通过 2不通过")
	private Integer isPass;
	
	@ApiModelProperty(required= true,value = "流程名称")
	private String processName;

	
    //抄送的用户ID
    private String[] sendUserIds;
    
    private String remark;
    
    
	public String[] getSendUserIds() {
		return sendUserIds;
	}

	public void setSendUserIds(String[] sendUserIds) {
		this.sendUserIds = sendUserIds;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public Integer getIsDirectBack() {
		return isDirectBack;
	}

	public void setIsDirectBack(Integer isDirectBack) {
		this.isDirectBack = isDirectBack;
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

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	 
}
