package com.hongte.alms.base.vo.litigation.house;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 抵押信息
 * 
 * @author huweiqian
 * @since 2018-03-04
 */
public class MortgageInfo implements Serializable {

	private static final long serialVersionUID = -5855344677169846909L;

	/**
	 * 抵押物位置
	 */
	private String houseAddress;
	/**
	 * 权属人
	 */
	private String houseName;
	/**
	 * 我方抵押顺位
	 */
	private int ourMortgage;
	/**
	 * 债权人
	 */
	private String lender;
	/**
	 * 抵押权人
	 */
	private String pledee;
	/**
	 * 抵押债权
	 */
	private String mortgageSecurities;
	/**
	 * 抵押物面积
	 */
	private BigDecimal houseArea;
	/**
	 * 评估单价
	 */
	private BigDecimal housePrice;
	/**
	 * 评估总价
	 */
	private BigDecimal houseTotal;
	/**
	 * 第一顺位抵押权人
	 */
	private String housePledgedBank;
	/**
	 * 一抵债权金额
	 */
	private BigDecimal borrowTotal;
	/**
	 * 一抵债权余额
	 */
	private BigDecimal borrowBalance;
	/**
	 * 第二顺位抵押权人
	 */
	private String secondMortgageBank;
	/**
	 * 二抵债权金额
	 */
	private BigDecimal secondMortgageTotal;
	/**
	 * 二抵债权余额
	 */
	private BigDecimal secondMortgageBalance;
	/**
	 * 第三顺位抵押权人
	 */
	private String thirdMortgageBank;
	/**
	 * 三抵债权金额
	 */
	private BigDecimal thirdMortgageTotal;
	/**
	 * 三抵债权余额
	 */
	private BigDecimal thirdMortgageBalance;
	/**
	 * 第四顺位抵押权人
	 */
	private String fourthMortgageBank;
	/**
	 * 四抵债权金额
	 */
	private BigDecimal fourthMortgageTotal;
	/**
	 * 四抵债权余额
	 */
	private BigDecimal fourthMortgageBalance;
	/**
	 * 房产剩余空间
	 */
	private BigDecimal surplusValue;

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getOurMortgage() {
		return ourMortgage;
	}

	public void setOurMortgage(int ourMortgage) {
		this.ourMortgage = ourMortgage;
	}

	public String getLender() {
		return lender;
	}

	public void setLender(String lender) {
		this.lender = lender;
	}

	public String getPledee() {
		return pledee;
	}

	public void setPledee(String pledee) {
		this.pledee = pledee;
	}

	public String getMortgageSecurities() {
		return mortgageSecurities;
	}

	public void setMortgageSecurities(String mortgageSecurities) {
		this.mortgageSecurities = mortgageSecurities;
	}

	public BigDecimal getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(BigDecimal houseArea) {
		this.houseArea = houseArea;
	}

	public BigDecimal getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(BigDecimal housePrice) {
		this.housePrice = housePrice;
	}

	public BigDecimal getHouseTotal() {
		return houseTotal;
	}

	public void setHouseTotal(BigDecimal houseTotal) {
		this.houseTotal = houseTotal;
	}

	public String getHousePledgedBank() {
		return housePledgedBank;
	}

	public void setHousePledgedBank(String housePledgedBank) {
		this.housePledgedBank = housePledgedBank;
	}

	public BigDecimal getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(BigDecimal borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public BigDecimal getBorrowBalance() {
		return borrowBalance;
	}

	public void setBorrowBalance(BigDecimal borrowBalance) {
		this.borrowBalance = borrowBalance;
	}

	public String getSecondMortgageBank() {
		return secondMortgageBank;
	}

	public void setSecondMortgageBank(String secondMortgageBank) {
		this.secondMortgageBank = secondMortgageBank;
	}

	public BigDecimal getSecondMortgageTotal() {
		return secondMortgageTotal;
	}

	public void setSecondMortgageTotal(BigDecimal secondMortgageTotal) {
		this.secondMortgageTotal = secondMortgageTotal;
	}

	public BigDecimal getSecondMortgageBalance() {
		return secondMortgageBalance;
	}

	public void setSecondMortgageBalance(BigDecimal secondMortgageBalance) {
		this.secondMortgageBalance = secondMortgageBalance;
	}

	public String getThirdMortgageBank() {
		return thirdMortgageBank;
	}

	public void setThirdMortgageBank(String thirdMortgageBank) {
		this.thirdMortgageBank = thirdMortgageBank;
	}

	public BigDecimal getThirdMortgageTotal() {
		return thirdMortgageTotal;
	}

	public void setThirdMortgageTotal(BigDecimal thirdMortgageTotal) {
		this.thirdMortgageTotal = thirdMortgageTotal;
	}

	public BigDecimal getThirdMortgageBalance() {
		return thirdMortgageBalance;
	}

	public void setThirdMortgageBalance(BigDecimal thirdMortgageBalance) {
		this.thirdMortgageBalance = thirdMortgageBalance;
	}

	public String getFourthMortgageBank() {
		return fourthMortgageBank;
	}

	public void setFourthMortgageBank(String fourthMortgageBank) {
		this.fourthMortgageBank = fourthMortgageBank;
	}

	public BigDecimal getFourthMortgageTotal() {
		return fourthMortgageTotal;
	}

	public void setFourthMortgageTotal(BigDecimal fourthMortgageTotal) {
		this.fourthMortgageTotal = fourthMortgageTotal;
	}

	public BigDecimal getFourthMortgageBalance() {
		return fourthMortgageBalance;
	}

	public void setFourthMortgageBalance(BigDecimal fourthMortgageBalance) {
		this.fourthMortgageBalance = fourthMortgageBalance;
	}

	public BigDecimal getSurplusValue() {
		return surplusValue;
	}

	public void setSurplusValue(BigDecimal surplusValue) {
		this.surplusValue = surplusValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((borrowBalance == null) ? 0 : borrowBalance.hashCode());
		result = prime * result + ((borrowTotal == null) ? 0 : borrowTotal.hashCode());
		result = prime * result + ((fourthMortgageBalance == null) ? 0 : fourthMortgageBalance.hashCode());
		result = prime * result + ((fourthMortgageBank == null) ? 0 : fourthMortgageBank.hashCode());
		result = prime * result + ((fourthMortgageTotal == null) ? 0 : fourthMortgageTotal.hashCode());
		result = prime * result + ((houseAddress == null) ? 0 : houseAddress.hashCode());
		result = prime * result + ((houseArea == null) ? 0 : houseArea.hashCode());
		result = prime * result + ((houseName == null) ? 0 : houseName.hashCode());
		result = prime * result + ((housePledgedBank == null) ? 0 : housePledgedBank.hashCode());
		result = prime * result + ((housePrice == null) ? 0 : housePrice.hashCode());
		result = prime * result + ((houseTotal == null) ? 0 : houseTotal.hashCode());
		result = prime * result + ((lender == null) ? 0 : lender.hashCode());
		result = prime * result + ((mortgageSecurities == null) ? 0 : mortgageSecurities.hashCode());
		result = prime * result + ourMortgage;
		result = prime * result + ((pledee == null) ? 0 : pledee.hashCode());
		result = prime * result + ((secondMortgageBalance == null) ? 0 : secondMortgageBalance.hashCode());
		result = prime * result + ((secondMortgageBank == null) ? 0 : secondMortgageBank.hashCode());
		result = prime * result + ((secondMortgageTotal == null) ? 0 : secondMortgageTotal.hashCode());
		result = prime * result + ((surplusValue == null) ? 0 : surplusValue.hashCode());
		result = prime * result + ((thirdMortgageBalance == null) ? 0 : thirdMortgageBalance.hashCode());
		result = prime * result + ((thirdMortgageBank == null) ? 0 : thirdMortgageBank.hashCode());
		result = prime * result + ((thirdMortgageTotal == null) ? 0 : thirdMortgageTotal.hashCode());
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
		MortgageInfo other = (MortgageInfo) obj;
		if (borrowBalance == null) {
			if (other.borrowBalance != null)
				return false;
		} else if (!borrowBalance.equals(other.borrowBalance))
			return false;
		if (borrowTotal == null) {
			if (other.borrowTotal != null)
				return false;
		} else if (!borrowTotal.equals(other.borrowTotal))
			return false;
		if (fourthMortgageBalance == null) {
			if (other.fourthMortgageBalance != null)
				return false;
		} else if (!fourthMortgageBalance.equals(other.fourthMortgageBalance))
			return false;
		if (fourthMortgageBank == null) {
			if (other.fourthMortgageBank != null)
				return false;
		} else if (!fourthMortgageBank.equals(other.fourthMortgageBank))
			return false;
		if (fourthMortgageTotal == null) {
			if (other.fourthMortgageTotal != null)
				return false;
		} else if (!fourthMortgageTotal.equals(other.fourthMortgageTotal))
			return false;
		if (houseAddress == null) {
			if (other.houseAddress != null)
				return false;
		} else if (!houseAddress.equals(other.houseAddress))
			return false;
		if (houseArea == null) {
			if (other.houseArea != null)
				return false;
		} else if (!houseArea.equals(other.houseArea))
			return false;
		if (houseName == null) {
			if (other.houseName != null)
				return false;
		} else if (!houseName.equals(other.houseName))
			return false;
		if (housePledgedBank == null) {
			if (other.housePledgedBank != null)
				return false;
		} else if (!housePledgedBank.equals(other.housePledgedBank))
			return false;
		if (housePrice == null) {
			if (other.housePrice != null)
				return false;
		} else if (!housePrice.equals(other.housePrice))
			return false;
		if (houseTotal == null) {
			if (other.houseTotal != null)
				return false;
		} else if (!houseTotal.equals(other.houseTotal))
			return false;
		if (lender == null) {
			if (other.lender != null)
				return false;
		} else if (!lender.equals(other.lender))
			return false;
		if (mortgageSecurities == null) {
			if (other.mortgageSecurities != null)
				return false;
		} else if (!mortgageSecurities.equals(other.mortgageSecurities))
			return false;
		if (ourMortgage != other.ourMortgage)
			return false;
		if (pledee == null) {
			if (other.pledee != null)
				return false;
		} else if (!pledee.equals(other.pledee))
			return false;
		if (secondMortgageBalance == null) {
			if (other.secondMortgageBalance != null)
				return false;
		} else if (!secondMortgageBalance.equals(other.secondMortgageBalance))
			return false;
		if (secondMortgageBank == null) {
			if (other.secondMortgageBank != null)
				return false;
		} else if (!secondMortgageBank.equals(other.secondMortgageBank))
			return false;
		if (secondMortgageTotal == null) {
			if (other.secondMortgageTotal != null)
				return false;
		} else if (!secondMortgageTotal.equals(other.secondMortgageTotal))
			return false;
		if (surplusValue == null) {
			if (other.surplusValue != null)
				return false;
		} else if (!surplusValue.equals(other.surplusValue))
			return false;
		if (thirdMortgageBalance == null) {
			if (other.thirdMortgageBalance != null)
				return false;
		} else if (!thirdMortgageBalance.equals(other.thirdMortgageBalance))
			return false;
		if (thirdMortgageBank == null) {
			if (other.thirdMortgageBank != null)
				return false;
		} else if (!thirdMortgageBank.equals(other.thirdMortgageBank))
			return false;
		if (thirdMortgageTotal == null) {
			if (other.thirdMortgageTotal != null)
				return false;
		} else if (!thirdMortgageTotal.equals(other.thirdMortgageTotal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MortgageInfo [houseAddress=" + houseAddress + ", houseName=" + houseName + ", ourMortgage="
				+ ourMortgage + ", lender=" + lender + ", pledee=" + pledee + ", mortgageSecurities="
				+ mortgageSecurities + ", houseArea=" + houseArea + ", housePrice=" + housePrice + ", houseTotal="
				+ houseTotal + ", housePledgedBank=" + housePledgedBank + ", borrowTotal=" + borrowTotal
				+ ", borrowBalance=" + borrowBalance + ", secondMortgageBank=" + secondMortgageBank
				+ ", secondMortgageTotal=" + secondMortgageTotal + ", secondMortgageBalance=" + secondMortgageBalance
				+ ", thirdMortgageBank=" + thirdMortgageBank + ", thirdMortgageTotal=" + thirdMortgageTotal
				+ ", thirdMortgageBalance=" + thirdMortgageBalance + ", fourthMortgageBank=" + fourthMortgageBank
				+ ", fourthMortgageTotal=" + fourthMortgageTotal + ", fourthMortgageBalance=" + fourthMortgageBalance
				+ ", surplusValue=" + surplusValue + "]";
	}

}
