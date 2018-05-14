package com.hongte.alms.base.vo.module.classify;

import java.io.Serializable;
import java.util.List;

public class ClassifyConditionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 业务ID
	 */
	private String businessId;

	/**
	 * 操作源
	 */
	private String opSourse;

	/**
	 * 操作源ID
	 */
	private String opSourseId;

	/**
	 * 分类名称
	 */
	private String className;

	/**
	 * 操作者ID
	 */
	private String opUserId;

	/**
	 * 操作者姓名
	 */
	private String opUsername;

	/**
	 * 抵押物信息
	 */
	private List<String> guaranteeConditions;

	/**
	 * 主借款人信息
	 */
	private List<String> mainBorrowerConditions;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOpSourse() {
		return opSourse;
	}

	public void setOpSourse(String opSourse) {
		this.opSourse = opSourse;
	}

	public String getOpSourseId() {
		return opSourseId;
	}

	public void setOpSourseId(String opSourseId) {
		this.opSourseId = opSourseId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getGuaranteeConditions() {
		return guaranteeConditions;
	}

	public void setGuaranteeConditions(List<String> guaranteeConditions) {
		this.guaranteeConditions = guaranteeConditions;
	}

	public List<String> getMainBorrowerConditions() {
		return mainBorrowerConditions;
	}

	public void setMainBorrowerConditions(List<String> mainBorrowerConditions) {
		this.mainBorrowerConditions = mainBorrowerConditions;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
	}

	public String getOpUsername() {
		return opUsername;
	}

	public void setOpUsername(String opUsername) {
		this.opUsername = opUsername;
	}

	@Override
	public String toString() {
		return "ClassifyConditionVO [businessId=" + businessId + ", opSourse=" + opSourse + ", opSourseId=" + opSourseId
				+ ", className=" + className + ", opUserId=" + opUserId + ", opUsername=" + opUsername
				+ ", guaranteeConditions=" + guaranteeConditions + ", mainBorrowerConditions=" + mainBorrowerConditions
				+ "]";
	}

}
