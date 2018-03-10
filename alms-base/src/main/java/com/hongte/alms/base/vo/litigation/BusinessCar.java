package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * [车辆的详细信息表]
 * </p>
 *
 * @author huweiqian
 * @since 2018-03-01
 */

public class BusinessCar implements Serializable{

	private static final long serialVersionUID = 2248300875789582950L;
	/**
	 * 主键ID
	 */
	@ApiModelProperty(required = true, value = "主键ID")
	private String id;
	/**
	 * 车贷业务编号
	 */
	@ApiModelProperty(required = true, value = "车贷业务编号")
	private String carBusinessId;
	/**
	 * 所属人
	 */
	@ApiModelProperty(required = true, value = "所属人")
	private String ownerName;
	/**
	 * 车身颜色
	 */
	@ApiModelProperty(required = true, value = "车身颜色")
	private String carBodyColor;
	/**
	 * 车牌号
	 */
	@ApiModelProperty(required = true, value = "车牌号")
	private String licensePlateNumber;
	/**
	 * [汽车品牌]
	 */
	@ApiModelProperty(required = true, value = "[汽车品牌]")
	private String carBrand;
	/**
	 * 汽车型号
	 */
	@ApiModelProperty(required = true, value = "汽车型号")
	private String carModelNumber;
	/**
	 * 违章费用
	 */
	@ApiModelProperty(required = true, value = "违章费用")
	private String carViolations;
	/**
	 * 车架号
	 */
	@ApiModelProperty(required = true, value = "车架号")
	private String chassisNumber;
	/**
	 * 发动机号
	 */
	@ApiModelProperty(required = true, value = "发动机号")
	private String engineNumber;
	/**
	 * 评估价值
	 */
	@ApiModelProperty(required = true, value = "评估价值")
	private String carEvaluateMoney;
	/**
	 * 年审到期日
	 */
	@ApiModelProperty(required = true, value = "年审到期日")
	private String annualDueDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarBusinessId() {
		return carBusinessId;
	}

	public void setCarBusinessId(String carBusinessId) {
		this.carBusinessId = carBusinessId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCarBodyColor() {
		return carBodyColor;
	}

	public void setCarBodyColor(String carBodyColor) {
		this.carBodyColor = carBodyColor;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarModelNumber() {
		return carModelNumber;
	}

	public void setCarModelNumber(String carModelNumber) {
		this.carModelNumber = carModelNumber;
	}

	public String getCarViolations() {
		return carViolations;
	}

	public void setCarViolations(String carViolations) {
		this.carViolations = carViolations;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getCarEvaluateMoney() {
		return carEvaluateMoney;
	}

	public void setCarEvaluateMoney(String carEvaluateMoney) {
		this.carEvaluateMoney = carEvaluateMoney;
	}

	public String getAnnualDueDate() {
		return annualDueDate;
	}

	public void setAnnualDueDate(String annualDueDate) {
		this.annualDueDate = annualDueDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annualDueDate == null) ? 0 : annualDueDate.hashCode());
		result = prime * result + ((carBodyColor == null) ? 0 : carBodyColor.hashCode());
		result = prime * result + ((carBrand == null) ? 0 : carBrand.hashCode());
		result = prime * result + ((carBusinessId == null) ? 0 : carBusinessId.hashCode());
		result = prime * result + ((carEvaluateMoney == null) ? 0 : carEvaluateMoney.hashCode());
		result = prime * result + ((carModelNumber == null) ? 0 : carModelNumber.hashCode());
		result = prime * result + ((carViolations == null) ? 0 : carViolations.hashCode());
		result = prime * result + ((chassisNumber == null) ? 0 : chassisNumber.hashCode());
		result = prime * result + ((engineNumber == null) ? 0 : engineNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((licensePlateNumber == null) ? 0 : licensePlateNumber.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessCar other = (BusinessCar) obj;
		if (annualDueDate == null) {
			if (other.annualDueDate != null)
				return false;
		} else if (!annualDueDate.equals(other.annualDueDate))
			return false;
		if (carBodyColor == null) {
			if (other.carBodyColor != null)
				return false;
		} else if (!carBodyColor.equals(other.carBodyColor))
			return false;
		if (carBrand == null) {
			if (other.carBrand != null)
				return false;
		} else if (!carBrand.equals(other.carBrand))
			return false;
		if (carBusinessId == null) {
			if (other.carBusinessId != null)
				return false;
		} else if (!carBusinessId.equals(other.carBusinessId))
			return false;
		if (carEvaluateMoney == null) {
			if (other.carEvaluateMoney != null)
				return false;
		} else if (!carEvaluateMoney.equals(other.carEvaluateMoney))
			return false;
		if (carModelNumber == null) {
			if (other.carModelNumber != null)
				return false;
		} else if (!carModelNumber.equals(other.carModelNumber))
			return false;
		if (carViolations == null) {
			if (other.carViolations != null)
				return false;
		} else if (!carViolations.equals(other.carViolations))
			return false;
		if (chassisNumber == null) {
			if (other.chassisNumber != null)
				return false;
		} else if (!chassisNumber.equals(other.chassisNumber))
			return false;
		if (engineNumber == null) {
			if (other.engineNumber != null)
				return false;
		} else if (!engineNumber.equals(other.engineNumber))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (licensePlateNumber == null) {
			if (other.licensePlateNumber != null)
				return false;
		} else if (!licensePlateNumber.equals(other.licensePlateNumber))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessCar [id=" + id + ", carBusinessId=" + carBusinessId + ", ownerName=" + ownerName
				+ ", carBodyColor=" + carBodyColor + ", licensePlateNumber=" + licensePlateNumber + ", carBrand="
				+ carBrand + ", carModelNumber=" + carModelNumber + ", carViolations=" + carViolations
				+ ", chassisNumber=" + chassisNumber + ", engineNumber=" + engineNumber + ", carEvaluateMoney="
				+ carEvaluateMoney + ", annualDueDate=" + annualDueDate + "]";
	}

}
