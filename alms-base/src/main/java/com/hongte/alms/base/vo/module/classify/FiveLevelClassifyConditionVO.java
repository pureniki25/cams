package com.hongte.alms.base.vo.module.classify;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hongte.alms.base.entity.FiveLevelClassifyCondition;

public class FiveLevelClassifyConditionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String subClassName;

	private String parentId;
	private String executeCondition;
	private String className;
	private String businessType;

	private Date updateTime;

	private List<FiveLevelClassifyCondition> classifyConditions;

	public String getSubClassName() {
		return subClassName;
	}

	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getExecuteCondition() {
		return executeCondition;
	}

	public void setExecuteCondition(String executeCondition) {
		this.executeCondition = executeCondition;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<FiveLevelClassifyCondition> getClassifyConditions() {
		return classifyConditions;
	}

	public void setClassifyConditions(List<FiveLevelClassifyCondition> classifyConditions) {
		this.classifyConditions = classifyConditions;
	}

	@Override
	public String toString() {
		return "FiveLevelClassifyConditionVO [subClassName=" + subClassName + ", parentId=" + parentId
				+ ", executeCondition=" + executeCondition + ", className=" + className + ", businessType="
				+ businessType + ", updateTime=" + updateTime + ", classifyConditions=" + classifyConditions + "]";
	}

}
