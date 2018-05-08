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

	@Override
	public String toString() {
		return "ClassifyConditionVO [businessId=" + businessId + ", opSourse=" + opSourse + ", guaranteeConditions="
				+ guaranteeConditions + ", mainBorrowerConditions=" + mainBorrowerConditions + "]";
	}

}
