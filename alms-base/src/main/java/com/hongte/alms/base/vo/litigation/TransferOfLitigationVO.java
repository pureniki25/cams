package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class TransferOfLitigationVO implements Serializable {

	private static final long serialVersionUID = -7083361271450973294L;

	/**
	 * 业务编码
	 */
	@ApiModelProperty(required = true, value = "业务编码")
	private String businessId;

	/**
	 * 借款利率
	 */
	@ApiModelProperty(required = true, value = "借款利率")
	private double loanRate;

	/**
	 * 业务类型
	 */
	@ApiModelProperty(required = true, value = "业务类型")
	private String businessType;

	/**
	 * 业务类型名称
	 */
	@ApiModelProperty(required = true, value = "业务类型名称")
	private String businessTypeName;

	/**
	 * 业务类型组别名称
	 */
	@ApiModelProperty(required = true, value = "业务类型组别名称")
	private String businessTypeGroup;

	/**
	 * 业务所属分公司
	 */
	@ApiModelProperty(required = true, value = "业务所属分公司")
	private BusinessCompany businessCompany;

	/**
	 * 客户名称
	 */
	@ApiModelProperty(required = true, value = "客户名称")
	private String customerName;

	/**
	 * 客户类型
	 */
	@ApiModelProperty(required = true, value = "客户类型")
	private String customerType;

	/**
	 * 客户身份证号码
	 */
	@ApiModelProperty(required = true, value = "客户身份证号码")
	private String customerIdentifyCard;

	/**
	 * 业务获取人
	 */
	@ApiModelProperty(required = true, value = "业务获取人")
	private BusinessUser originalUser;

	/**
	 * 借款金额
	 */
	@ApiModelProperty(required = true, value = "借款金额")
	private BigDecimal finalBorrowMoney;

	/**
	 * 借款期限
	 */
	@ApiModelProperty(required = true, value = "借款期限")
	private Integer borrowLimit;

	/**
	 * 还款方式ID，1到期还本息，2每月付息到期还本，5每月等额本息,9是分期还本付息
	 */
	@ApiModelProperty(required = true, value = "还款方式ID，1到期还本息，2每月付息到期还本，5每月等额本息,9是分期还本付息")
	private Integer repaymentTypeId;

	/**
	 * 已还本金(累计)
	 */
	@ApiModelProperty(required = true, value = "已还本金(累计)")
	private BigDecimal repaymentPrincipa;

	/**
	 * 已还期数
	 */
	@ApiModelProperty(required = true, value = "已还期数")
	private Integer repaymentPeriods;

	/**
	 * 总期数
	 */
	@ApiModelProperty(required = true, value = "总期数")
	private Integer totalPeriods;

	/**
	 * 结清状态(财务未结清0 财务已结清10)
	 */
	@ApiModelProperty(required = true, value = "结清状态(财务未结清0 财务已结清10)")
	private Integer accountStatus;

	/**
	 * 诉讼案件来源 0信贷系统 1新贷后系统 2线下业务补录
	 */
	@ApiModelProperty(required = true, value = "诉讼案件来源 0信贷系统 1新贷后系统 2线下业务补录")
	private Integer sourceSystem;

	/**
	 * 相关的车辆信息列表
	 */
	@ApiModelProperty(required = true, value = "相关的车辆信息列表")
	private List<BusinessCar> carList;

	/**
	 * 相关的房屋信息列表
	 */
	@ApiModelProperty(required = true, value = "相关的房屋信息列表")
	private List<BusinessHouse> houseList;

	/**
	 * 还款明细
	 */
	@ApiModelProperty(required = true, value = "还款明细")
	private List<BusinessPayment> paymentList;
	
	/**
	 * 借款人明细
	 */
	@ApiModelProperty(required = true, value = "借款人明细")
	private List<LitigationBorrowerDetailed> litigationBorrowerDetailedList;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(required = true, value = "创建人ID")
	private String createUserId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public double getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(double loanRate) {
		this.loanRate = loanRate;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public String getBusinessTypeGroup() {
		return businessTypeGroup;
	}

	public void setBusinessTypeGroup(String businessTypeGroup) {
		this.businessTypeGroup = businessTypeGroup;
	}

	public BusinessCompany getBusinessCompany() {
		return businessCompany;
	}

	public void setBusinessCompany(BusinessCompany businessCompany) {
		this.businessCompany = businessCompany;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerIdentifyCard() {
		return customerIdentifyCard;
	}

	public void setCustomerIdentifyCard(String customerIdentifyCard) {
		this.customerIdentifyCard = customerIdentifyCard;
	}

	public BusinessUser getOriginalUser() {
		return originalUser;
	}

	public void setOriginalUser(BusinessUser originalUser) {
		this.originalUser = originalUser;
	}

	public BigDecimal getFinalBorrowMoney() {
		return finalBorrowMoney;
	}

	public void setFinalBorrowMoney(BigDecimal finalBorrowMoney) {
		this.finalBorrowMoney = finalBorrowMoney;
	}

	public Integer getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public Integer getRepaymentTypeId() {
		return repaymentTypeId;
	}

	public void setRepaymentTypeId(Integer repaymentTypeId) {
		this.repaymentTypeId = repaymentTypeId;
	}

	public BigDecimal getRepaymentPrincipa() {
		return repaymentPrincipa;
	}

	public void setRepaymentPrincipa(BigDecimal repaymentPrincipa) {
		this.repaymentPrincipa = repaymentPrincipa;
	}

	public Integer getRepaymentPeriods() {
		return repaymentPeriods;
	}

	public void setRepaymentPeriods(Integer repaymentPeriods) {
		this.repaymentPeriods = repaymentPeriods;
	}

	public Integer getTotalPeriods() {
		return totalPeriods;
	}

	public void setTotalPeriods(Integer totalPeriods) {
		this.totalPeriods = totalPeriods;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Integer getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(Integer sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public List<BusinessCar> getCarList() {
		return carList;
	}

	public void setCarList(List<BusinessCar> carList) {
		this.carList = carList;
	}

	public List<BusinessHouse> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<BusinessHouse> houseList) {
		this.houseList = houseList;
	}

	public List<BusinessPayment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<BusinessPayment> paymentList) {
		this.paymentList = paymentList;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((borrowLimit == null) ? 0 : borrowLimit.hashCode());
		result = prime * result + ((businessCompany == null) ? 0 : businessCompany.hashCode());
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
		result = prime * result + ((businessType == null) ? 0 : businessType.hashCode());
		result = prime * result + ((businessTypeGroup == null) ? 0 : businessTypeGroup.hashCode());
		result = prime * result + ((businessTypeName == null) ? 0 : businessTypeName.hashCode());
		result = prime * result + ((carList == null) ? 0 : carList.hashCode());
		result = prime * result + ((createUserId == null) ? 0 : createUserId.hashCode());
		result = prime * result + ((customerIdentifyCard == null) ? 0 : customerIdentifyCard.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((customerType == null) ? 0 : customerType.hashCode());
		result = prime * result + ((finalBorrowMoney == null) ? 0 : finalBorrowMoney.hashCode());
		result = prime * result + ((houseList == null) ? 0 : houseList.hashCode());
		result = prime * result
				+ ((litigationBorrowerDetailedList == null) ? 0 : litigationBorrowerDetailedList.hashCode());
		long temp;
		temp = Double.doubleToLongBits(loanRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((originalUser == null) ? 0 : originalUser.hashCode());
		result = prime * result + ((paymentList == null) ? 0 : paymentList.hashCode());
		result = prime * result + ((repaymentPeriods == null) ? 0 : repaymentPeriods.hashCode());
		result = prime * result + ((repaymentPrincipa == null) ? 0 : repaymentPrincipa.hashCode());
		result = prime * result + ((repaymentTypeId == null) ? 0 : repaymentTypeId.hashCode());
		result = prime * result + ((sourceSystem == null) ? 0 : sourceSystem.hashCode());
		result = prime * result + ((totalPeriods == null) ? 0 : totalPeriods.hashCode());
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
		TransferOfLitigationVO other = (TransferOfLitigationVO) obj;
		if (accountStatus == null) {
			if (other.accountStatus != null)
				return false;
		} else if (!accountStatus.equals(other.accountStatus))
			return false;
		if (borrowLimit == null) {
			if (other.borrowLimit != null)
				return false;
		} else if (!borrowLimit.equals(other.borrowLimit))
			return false;
		if (businessCompany == null) {
			if (other.businessCompany != null)
				return false;
		} else if (!businessCompany.equals(other.businessCompany))
			return false;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		if (businessType == null) {
			if (other.businessType != null)
				return false;
		} else if (!businessType.equals(other.businessType))
			return false;
		if (businessTypeGroup == null) {
			if (other.businessTypeGroup != null)
				return false;
		} else if (!businessTypeGroup.equals(other.businessTypeGroup))
			return false;
		if (businessTypeName == null) {
			if (other.businessTypeName != null)
				return false;
		} else if (!businessTypeName.equals(other.businessTypeName))
			return false;
		if (carList == null) {
			if (other.carList != null)
				return false;
		} else if (!carList.equals(other.carList))
			return false;
		if (createUserId == null) {
			if (other.createUserId != null)
				return false;
		} else if (!createUserId.equals(other.createUserId))
			return false;
		if (customerIdentifyCard == null) {
			if (other.customerIdentifyCard != null)
				return false;
		} else if (!customerIdentifyCard.equals(other.customerIdentifyCard))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (customerType == null) {
			if (other.customerType != null)
				return false;
		} else if (!customerType.equals(other.customerType))
			return false;
		if (finalBorrowMoney == null) {
			if (other.finalBorrowMoney != null)
				return false;
		} else if (!finalBorrowMoney.equals(other.finalBorrowMoney))
			return false;
		if (houseList == null) {
			if (other.houseList != null)
				return false;
		} else if (!houseList.equals(other.houseList))
			return false;
		if (litigationBorrowerDetailedList == null) {
			if (other.litigationBorrowerDetailedList != null)
				return false;
		} else if (!litigationBorrowerDetailedList.equals(other.litigationBorrowerDetailedList))
			return false;
		if (Double.doubleToLongBits(loanRate) != Double.doubleToLongBits(other.loanRate))
			return false;
		if (originalUser == null) {
			if (other.originalUser != null)
				return false;
		} else if (!originalUser.equals(other.originalUser))
			return false;
		if (paymentList == null) {
			if (other.paymentList != null)
				return false;
		} else if (!paymentList.equals(other.paymentList))
			return false;
		if (repaymentPeriods == null) {
			if (other.repaymentPeriods != null)
				return false;
		} else if (!repaymentPeriods.equals(other.repaymentPeriods))
			return false;
		if (repaymentPrincipa == null) {
			if (other.repaymentPrincipa != null)
				return false;
		} else if (!repaymentPrincipa.equals(other.repaymentPrincipa))
			return false;
		if (repaymentTypeId == null) {
			if (other.repaymentTypeId != null)
				return false;
		} else if (!repaymentTypeId.equals(other.repaymentTypeId))
			return false;
		if (sourceSystem == null) {
			if (other.sourceSystem != null)
				return false;
		} else if (!sourceSystem.equals(other.sourceSystem))
			return false;
		if (totalPeriods == null) {
			if (other.totalPeriods != null)
				return false;
		} else if (!totalPeriods.equals(other.totalPeriods))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransferOfLitigationVO [businessId=" + businessId + ", loanRate=" + loanRate + ", businessType="
				+ businessType + ", businessTypeName=" + businessTypeName + ", businessTypeGroup=" + businessTypeGroup
				+ ", businessCompany=" + businessCompany + ", customerName=" + customerName + ", customerType="
				+ customerType + ", customerIdentifyCard=" + customerIdentifyCard + ", originalUser=" + originalUser
				+ ", finalBorrowMoney=" + finalBorrowMoney + ", borrowLimit=" + borrowLimit + ", repaymentTypeId="
				+ repaymentTypeId + ", repaymentPrincipa=" + repaymentPrincipa + ", repaymentPeriods="
				+ repaymentPeriods + ", totalPeriods=" + totalPeriods + ", accountStatus=" + accountStatus
				+ ", sourceSystem=" + sourceSystem + ", carList=" + carList + ", houseList=" + houseList
				+ ", paymentList=" + paymentList + ", createUserId=" + createUserId + "]";
	}

}
