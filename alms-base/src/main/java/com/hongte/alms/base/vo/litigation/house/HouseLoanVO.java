package com.hongte.alms.base.vo.litigation.house;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author huweiqian
 * @since 2018.3.4
 */
public class HouseLoanVO implements Serializable {

	private static final long serialVersionUID = -4036816465381796358L;

	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 联系电话
	 */
	private String phoneNumber;
	/**
	 * 业务类型
	 */
	private String businessTypeName;
	/**
	 * 借款金额
	 */
	private BigDecimal borrowMoney;
	/**
	 * 出款金额
	 */
	private BigDecimal factOutputMoney;
	/**
	 * 业务经办人
	 */
	private String operatorName;
	/**
	 * 业务获取人
	 */
	private String originalName;
	/**
	 * 主审风控
	 */
	private String lastReviewUserName;
	/**
	 * 费率（利息+服务费）
	 */
	private BigDecimal borrowRate;
	/**
	 * 借款期限
	 */
	private int borrowLimit;
	/**
	 * 公证类型
	 */
	private String notarizationType;

	/**
	 * 分公司名称
	 */
	private String companyName;

	/**
	 * 还款计划信息
	 */
	private List<HousePlanInfo> housePlanInfos;

	/**
	 * 抵押信息
	 */
	private List<MortgageInfo> mortgageInfos;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public BigDecimal getFactOutputMoney() {
		return factOutputMoney;
	}

	public void setFactOutputMoney(BigDecimal factOutputMoney) {
		this.factOutputMoney = factOutputMoney;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getLastReviewUserName() {
		return lastReviewUserName;
	}

	public void setLastReviewUserName(String lastReviewUserName) {
		this.lastReviewUserName = lastReviewUserName;
	}

	public BigDecimal getBorrowRate() {
		return borrowRate;
	}

	public void setBorrowRate(BigDecimal borrowRate) {
		this.borrowRate = borrowRate;
	}

	public int getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(int borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public String getNotarizationType() {
		return notarizationType;
	}

	public void setNotarizationType(String notarizationType) {
		this.notarizationType = notarizationType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<HousePlanInfo> getHousePlanInfos() {
		return housePlanInfos;
	}

	public void setHousePlanInfos(List<HousePlanInfo> housePlanInfos) {
		this.housePlanInfos = housePlanInfos;
	}

	public List<MortgageInfo> getMortgageInfos() {
		return mortgageInfos;
	}

	public void setMortgageInfos(List<MortgageInfo> mortgageInfos) {
		this.mortgageInfos = mortgageInfos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + borrowLimit;
		result = prime * result + ((borrowMoney == null) ? 0 : borrowMoney.hashCode());
		result = prime * result + ((borrowRate == null) ? 0 : borrowRate.hashCode());
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
		result = prime * result + ((businessTypeName == null) ? 0 : businessTypeName.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((factOutputMoney == null) ? 0 : factOutputMoney.hashCode());
		result = prime * result + ((housePlanInfos == null) ? 0 : housePlanInfos.hashCode());
		result = prime * result + ((lastReviewUserName == null) ? 0 : lastReviewUserName.hashCode());
		result = prime * result + ((mortgageInfos == null) ? 0 : mortgageInfos.hashCode());
		result = prime * result + ((notarizationType == null) ? 0 : notarizationType.hashCode());
		result = prime * result + ((operatorName == null) ? 0 : operatorName.hashCode());
		result = prime * result + ((originalName == null) ? 0 : originalName.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		HouseLoanVO other = (HouseLoanVO) obj;
		if (borrowLimit != other.borrowLimit)
			return false;
		if (borrowMoney == null) {
			if (other.borrowMoney != null)
				return false;
		} else if (!borrowMoney.equals(other.borrowMoney))
			return false;
		if (borrowRate == null) {
			if (other.borrowRate != null)
				return false;
		} else if (!borrowRate.equals(other.borrowRate))
			return false;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		if (businessTypeName == null) {
			if (other.businessTypeName != null)
				return false;
		} else if (!businessTypeName.equals(other.businessTypeName))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (factOutputMoney == null) {
			if (other.factOutputMoney != null)
				return false;
		} else if (!factOutputMoney.equals(other.factOutputMoney))
			return false;
		if (housePlanInfos == null) {
			if (other.housePlanInfos != null)
				return false;
		} else if (!housePlanInfos.equals(other.housePlanInfos))
			return false;
		if (lastReviewUserName == null) {
			if (other.lastReviewUserName != null)
				return false;
		} else if (!lastReviewUserName.equals(other.lastReviewUserName))
			return false;
		if (mortgageInfos == null) {
			if (other.mortgageInfos != null)
				return false;
		} else if (!mortgageInfos.equals(other.mortgageInfos))
			return false;
		if (notarizationType == null) {
			if (other.notarizationType != null)
				return false;
		} else if (!notarizationType.equals(other.notarizationType))
			return false;
		if (operatorName == null) {
			if (other.operatorName != null)
				return false;
		} else if (!operatorName.equals(other.operatorName))
			return false;
		if (originalName == null) {
			if (other.originalName != null)
				return false;
		} else if (!originalName.equals(other.originalName))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HouseLoanVO [businessId=" + businessId + ", customerName=" + customerName + ", phoneNumber="
				+ phoneNumber + ", businessTypeName=" + businessTypeName + ", borrowMoney=" + borrowMoney
				+ ", factOutputMoney=" + factOutputMoney + ", operatorName=" + operatorName + ", originalName="
				+ originalName + ", lastReviewUserName=" + lastReviewUserName + ", borrowRate=" + borrowRate
				+ ", borrowLimit=" + borrowLimit + ", notarizationType=" + notarizationType + ", companyName="
				+ companyName + ", housePlanInfos=" + housePlanInfos + ", mortgageInfos=" + mortgageInfos + "]";
	}

}
