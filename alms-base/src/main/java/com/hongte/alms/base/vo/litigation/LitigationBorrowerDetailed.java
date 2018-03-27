package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

public class LitigationBorrowerDetailed implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 身份证号
	 */
	private String identifyCard;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 借款人类型 0个人 1企业
	 */
	private Integer borrowerType;
	/**
	 * 1主借款人、2共借人 3担保人 4反担保人 5出借人
	 */
	private Integer type;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getIdentifyCard() {
		return identifyCard;
	}

	public void setIdentifyCard(String identifyCard) {
		this.identifyCard = identifyCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "LitigationBorrowerDetailed [businessId=" + businessId + ", identifyCard=" + identifyCard + ", name="
				+ name + ", borrowerType=" + borrowerType + ", type=" + type + "]";
	}

}
