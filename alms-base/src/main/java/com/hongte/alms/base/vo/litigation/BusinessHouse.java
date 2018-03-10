package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 业务房屋信息(发起诉讼的时候，通过该页面相关的数据)
 * </p>
 *
 * @author huweiqian
 * @since 2018-03-01
 */
public class BusinessHouse implements Serializable{
	private static final long serialVersionUID = -5842303111750462345L;
	/**
	 * 主键ID(业务ID)
	 */
	@ApiModelProperty(required = true, value = "主键ID(业务ID)")
	private String businessHouseId;
	/**
	 * 小贷系统房产ID
	 */
	@ApiModelProperty(required = true, value = "小贷系统房产ID")
	private Integer xiaodaiHouseId;
	/**
	 * 业务编码
	 */
	@ApiModelProperty(required = true, value = "业务编码")
	private String businessId;
	/**
	 * 房产价值
	 */
	@ApiModelProperty(required = true, value = "房产价值")
	private BigDecimal houseValue;
	/**
	 * 房产权所属人
	 */
	@ApiModelProperty(required = true, value = "房产权所属人")
	private String houseName;
	/**
	 * 房产位置
	 */
	@ApiModelProperty(required = true, value = "房产位置")
	private String houseAddress;
	/**
	 * 房产证号
	 */
	@ApiModelProperty(required = true, value = "房产证号")
	private String houseNo;
	/**
	 * 房产面积
	 */
	@ApiModelProperty(required = true, value = "房产面积")
	private BigDecimal houseArea;
	/**
	 * 评估单价
	 */
	@ApiModelProperty(required = true, value = "评估单价")
	private BigDecimal housePrice;
	/**
	 * 一抵贷款原始金额
	 */
	@ApiModelProperty(required = true, value = "一抵贷款原始金额")
	private BigDecimal houseTotal;
	/**
	 * 购买年份
	 */
	@ApiModelProperty(required = true, value = "购买年份")
	private String butTime;
	/**
	 * 登记年月
	 */
	@ApiModelProperty(required = true, value = "登记年月")
	private String registerTime;
	/**
	 * 所属楼盘
	 */
	@ApiModelProperty(required = true, value = "所属楼盘")
	private String houseBelong;
	/**
	 * 开盘年份
	 */
	@ApiModelProperty(required = true, value = "开盘年份")
	private String openTime;
	/**
	 * 一抵贷款时间
	 */
	@ApiModelProperty(required = true, value = "一抵贷款时间")
	private String borrowTime;
	/**
	 * 一抵贷款类型
	 */
	@ApiModelProperty(required = true, value = "一抵贷款类型")
	private String borrowType;
	/**
	 * 一抵贷款总额
	 */
	@ApiModelProperty(required = true, value = "一抵贷款总额")
	private BigDecimal borrowTotal;
	/**
	 * 一抵贷款余额
	 */
	@ApiModelProperty(required = true, value = "一抵贷款余额")
	private BigDecimal borrowBalance;
	/**
	 * 一抵贷款银行
	 */
	@ApiModelProperty(required = true, value = "一抵贷款银行")
	private String fsdBankName;
	/**
	 * 房产负债率对应区域(作废)
	 */
	@ApiModelProperty(required = true, value = "房产负债率对应区域(作废)")
	private String debtRegion;
	/**
	 * 备注
	 */
	@ApiModelProperty(required = true, value = "备注")
	private String remark;
	/**
	 * 更新人
	 */
	@ApiModelProperty(required = true, value = "更新人")
	private String updateUser;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(required = true, value = "更新时间")
	private Date updateTime;
	/**
	 * [二抵贷款时间]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款时间]")
	private Date secondMortgageTime;
	/**
	 * [二抵贷款期限]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款期限]")
	private Integer secondMortgageYear;
	/**
	 * [二抵贷款类型]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款类型]")
	private String secondMortgageType;
	/**
	 * [二抵贷款银行]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款银行]")
	private String secondMortgageBank;
	/**
	 * [二抵贷款其他银行]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款其他银行]")
	private String secondMortgageOtherBank;
	/**
	 * [二抵贷款总额]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款总额]")
	private BigDecimal secondMortgageTotal;
	/**
	 * [二抵贷款余额]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款余额]")
	private BigDecimal secondMortgageBalance;
	/**
	 * [三抵贷款时间]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款时间]")
	private Date thirdMortgageTime;
	/**
	 * [三抵贷款期限]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款期限]")
	private Integer thirdMortgageYear;
	/**
	 * [三抵贷款类型]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款类型]")
	private String thirdMortgageType;
	/**
	 * [二抵贷款银行]
	 */
	@ApiModelProperty(required = true, value = "[二抵贷款银行]")
	private String thirdMortgageBank;
	/**
	 * [三抵贷款其他银行]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款其他银行]")
	private String thirdMortgageOtherBank;
	/**
	 * [三抵贷款总额]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款总额]")
	private BigDecimal thirdMortgageTotal;
	/**
	 * [三抵贷款余额]
	 */
	@ApiModelProperty(required = true, value = "[三抵贷款余额]")
	private BigDecimal thirdMortgageBalance;
	/**
	 * [四抵贷款时间]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款时间]")
	private Date fourthMortgageTime;
	/**
	 * [四抵贷款期限]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款期限]")
	private Integer fourthMortgageYear;
	/**
	 * [四抵贷款类型]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款类型]")
	private String fourthMortgageType;
	/**
	 * [四抵贷款银行]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款银行]")
	private String fourthMortgageBank;
	/**
	 * [四抵贷款其他银行]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款其他银行]")
	private String fourthMortgageOtherBank;
	/**
	 * [四抵贷款总额]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款总额]")
	private BigDecimal fourthMortgageTotal;
	/**
	 * [四抵贷款余额]
	 */
	@ApiModelProperty(required = true, value = "[四抵贷款余额]")
	private BigDecimal fourthMortgageBalance;

	@ApiModelProperty(required = true, value = "房产类型")
	private String propertyType;

	public String getBusinessHouseId() {
		return businessHouseId;
	}

	public void setBusinessHouseId(String businessHouseId) {
		this.businessHouseId = businessHouseId;
	}

	public Integer getXiaodaiHouseId() {
		return xiaodaiHouseId;
	}

	public void setXiaodaiHouseId(Integer xiaodaiHouseId) {
		this.xiaodaiHouseId = xiaodaiHouseId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public BigDecimal getHouseValue() {
		return houseValue;
	}

	public void setHouseValue(BigDecimal houseValue) {
		this.houseValue = houseValue;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
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

	public String getButTime() {
		return butTime;
	}

	public void setButTime(String butTime) {
		this.butTime = butTime;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getHouseBelong() {
		return houseBelong;
	}

	public void setHouseBelong(String houseBelong) {
		this.houseBelong = houseBelong;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getBorrowTime() {
		return borrowTime;
	}

	public void setBorrowTime(String borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
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

	public String getFsdBankName() {
		return fsdBankName;
	}

	public void setFsdBankName(String fsdBankName) {
		this.fsdBankName = fsdBankName;
	}

	public String getDebtRegion() {
		return debtRegion;
	}

	public void setDebtRegion(String debtRegion) {
		this.debtRegion = debtRegion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getSecondMortgageTime() {
		return secondMortgageTime;
	}

	public void setSecondMortgageTime(Date secondMortgageTime) {
		this.secondMortgageTime = secondMortgageTime;
	}

	public Integer getSecondMortgageYear() {
		return secondMortgageYear;
	}

	public void setSecondMortgageYear(Integer secondMortgageYear) {
		this.secondMortgageYear = secondMortgageYear;
	}

	public String getSecondMortgageType() {
		return secondMortgageType;
	}

	public void setSecondMortgageType(String secondMortgageType) {
		this.secondMortgageType = secondMortgageType;
	}

	public String getSecondMortgageBank() {
		return secondMortgageBank;
	}

	public void setSecondMortgageBank(String secondMortgageBank) {
		this.secondMortgageBank = secondMortgageBank;
	}

	public String getSecondMortgageOtherBank() {
		return secondMortgageOtherBank;
	}

	public void setSecondMortgageOtherBank(String secondMortgageOtherBank) {
		this.secondMortgageOtherBank = secondMortgageOtherBank;
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

	public Date getThirdMortgageTime() {
		return thirdMortgageTime;
	}

	public void setThirdMortgageTime(Date thirdMortgageTime) {
		this.thirdMortgageTime = thirdMortgageTime;
	}

	public Integer getThirdMortgageYear() {
		return thirdMortgageYear;
	}

	public void setThirdMortgageYear(Integer thirdMortgageYear) {
		this.thirdMortgageYear = thirdMortgageYear;
	}

	public String getThirdMortgageType() {
		return thirdMortgageType;
	}

	public void setThirdMortgageType(String thirdMortgageType) {
		this.thirdMortgageType = thirdMortgageType;
	}

	public String getThirdMortgageBank() {
		return thirdMortgageBank;
	}

	public void setThirdMortgageBank(String thirdMortgageBank) {
		this.thirdMortgageBank = thirdMortgageBank;
	}

	public String getThirdMortgageOtherBank() {
		return thirdMortgageOtherBank;
	}

	public void setThirdMortgageOtherBank(String thirdMortgageOtherBank) {
		this.thirdMortgageOtherBank = thirdMortgageOtherBank;
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

	public Date getFourthMortgageTime() {
		return fourthMortgageTime;
	}

	public void setFourthMortgageTime(Date fourthMortgageTime) {
		this.fourthMortgageTime = fourthMortgageTime;
	}

	public Integer getFourthMortgageYear() {
		return fourthMortgageYear;
	}

	public void setFourthMortgageYear(Integer fourthMortgageYear) {
		this.fourthMortgageYear = fourthMortgageYear;
	}

	public String getFourthMortgageType() {
		return fourthMortgageType;
	}

	public void setFourthMortgageType(String fourthMortgageType) {
		this.fourthMortgageType = fourthMortgageType;
	}

	public String getFourthMortgageBank() {
		return fourthMortgageBank;
	}

	public void setFourthMortgageBank(String fourthMortgageBank) {
		this.fourthMortgageBank = fourthMortgageBank;
	}

	public String getFourthMortgageOtherBank() {
		return fourthMortgageOtherBank;
	}

	public void setFourthMortgageOtherBank(String fourthMortgageOtherBank) {
		this.fourthMortgageOtherBank = fourthMortgageOtherBank;
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

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((borrowBalance == null) ? 0 : borrowBalance.hashCode());
		result = prime * result + ((borrowTime == null) ? 0 : borrowTime.hashCode());
		result = prime * result + ((borrowTotal == null) ? 0 : borrowTotal.hashCode());
		result = prime * result + ((borrowType == null) ? 0 : borrowType.hashCode());
		result = prime * result + ((businessHouseId == null) ? 0 : businessHouseId.hashCode());
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
		result = prime * result + ((butTime == null) ? 0 : butTime.hashCode());
		result = prime * result + ((debtRegion == null) ? 0 : debtRegion.hashCode());
		result = prime * result + ((fourthMortgageBalance == null) ? 0 : fourthMortgageBalance.hashCode());
		result = prime * result + ((fourthMortgageBank == null) ? 0 : fourthMortgageBank.hashCode());
		result = prime * result + ((fourthMortgageOtherBank == null) ? 0 : fourthMortgageOtherBank.hashCode());
		result = prime * result + ((fourthMortgageTime == null) ? 0 : fourthMortgageTime.hashCode());
		result = prime * result + ((fourthMortgageTotal == null) ? 0 : fourthMortgageTotal.hashCode());
		result = prime * result + ((fourthMortgageType == null) ? 0 : fourthMortgageType.hashCode());
		result = prime * result + ((fourthMortgageYear == null) ? 0 : fourthMortgageYear.hashCode());
		result = prime * result + ((fsdBankName == null) ? 0 : fsdBankName.hashCode());
		result = prime * result + ((houseAddress == null) ? 0 : houseAddress.hashCode());
		result = prime * result + ((houseArea == null) ? 0 : houseArea.hashCode());
		result = prime * result + ((houseBelong == null) ? 0 : houseBelong.hashCode());
		result = prime * result + ((houseName == null) ? 0 : houseName.hashCode());
		result = prime * result + ((houseNo == null) ? 0 : houseNo.hashCode());
		result = prime * result + ((housePrice == null) ? 0 : housePrice.hashCode());
		result = prime * result + ((houseTotal == null) ? 0 : houseTotal.hashCode());
		result = prime * result + ((houseValue == null) ? 0 : houseValue.hashCode());
		result = prime * result + ((openTime == null) ? 0 : openTime.hashCode());
		result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
		result = prime * result + ((registerTime == null) ? 0 : registerTime.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((secondMortgageBalance == null) ? 0 : secondMortgageBalance.hashCode());
		result = prime * result + ((secondMortgageBank == null) ? 0 : secondMortgageBank.hashCode());
		result = prime * result + ((secondMortgageOtherBank == null) ? 0 : secondMortgageOtherBank.hashCode());
		result = prime * result + ((secondMortgageTime == null) ? 0 : secondMortgageTime.hashCode());
		result = prime * result + ((secondMortgageTotal == null) ? 0 : secondMortgageTotal.hashCode());
		result = prime * result + ((secondMortgageType == null) ? 0 : secondMortgageType.hashCode());
		result = prime * result + ((secondMortgageYear == null) ? 0 : secondMortgageYear.hashCode());
		result = prime * result + ((thirdMortgageBalance == null) ? 0 : thirdMortgageBalance.hashCode());
		result = prime * result + ((thirdMortgageBank == null) ? 0 : thirdMortgageBank.hashCode());
		result = prime * result + ((thirdMortgageOtherBank == null) ? 0 : thirdMortgageOtherBank.hashCode());
		result = prime * result + ((thirdMortgageTime == null) ? 0 : thirdMortgageTime.hashCode());
		result = prime * result + ((thirdMortgageTotal == null) ? 0 : thirdMortgageTotal.hashCode());
		result = prime * result + ((thirdMortgageType == null) ? 0 : thirdMortgageType.hashCode());
		result = prime * result + ((thirdMortgageYear == null) ? 0 : thirdMortgageYear.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
		result = prime * result + ((xiaodaiHouseId == null) ? 0 : xiaodaiHouseId.hashCode());
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
		BusinessHouse other = (BusinessHouse) obj;
		if (borrowBalance == null) {
			if (other.borrowBalance != null)
				return false;
		} else if (!borrowBalance.equals(other.borrowBalance))
			return false;
		if (borrowTime == null) {
			if (other.borrowTime != null)
				return false;
		} else if (!borrowTime.equals(other.borrowTime))
			return false;
		if (borrowTotal == null) {
			if (other.borrowTotal != null)
				return false;
		} else if (!borrowTotal.equals(other.borrowTotal))
			return false;
		if (borrowType == null) {
			if (other.borrowType != null)
				return false;
		} else if (!borrowType.equals(other.borrowType))
			return false;
		if (businessHouseId == null) {
			if (other.businessHouseId != null)
				return false;
		} else if (!businessHouseId.equals(other.businessHouseId))
			return false;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		if (butTime == null) {
			if (other.butTime != null)
				return false;
		} else if (!butTime.equals(other.butTime))
			return false;
		if (debtRegion == null) {
			if (other.debtRegion != null)
				return false;
		} else if (!debtRegion.equals(other.debtRegion))
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
		if (fourthMortgageOtherBank == null) {
			if (other.fourthMortgageOtherBank != null)
				return false;
		} else if (!fourthMortgageOtherBank.equals(other.fourthMortgageOtherBank))
			return false;
		if (fourthMortgageTime == null) {
			if (other.fourthMortgageTime != null)
				return false;
		} else if (!fourthMortgageTime.equals(other.fourthMortgageTime))
			return false;
		if (fourthMortgageTotal == null) {
			if (other.fourthMortgageTotal != null)
				return false;
		} else if (!fourthMortgageTotal.equals(other.fourthMortgageTotal))
			return false;
		if (fourthMortgageType == null) {
			if (other.fourthMortgageType != null)
				return false;
		} else if (!fourthMortgageType.equals(other.fourthMortgageType))
			return false;
		if (fourthMortgageYear == null) {
			if (other.fourthMortgageYear != null)
				return false;
		} else if (!fourthMortgageYear.equals(other.fourthMortgageYear))
			return false;
		if (fsdBankName == null) {
			if (other.fsdBankName != null)
				return false;
		} else if (!fsdBankName.equals(other.fsdBankName))
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
		if (houseBelong == null) {
			if (other.houseBelong != null)
				return false;
		} else if (!houseBelong.equals(other.houseBelong))
			return false;
		if (houseName == null) {
			if (other.houseName != null)
				return false;
		} else if (!houseName.equals(other.houseName))
			return false;
		if (houseNo == null) {
			if (other.houseNo != null)
				return false;
		} else if (!houseNo.equals(other.houseNo))
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
		if (houseValue == null) {
			if (other.houseValue != null)
				return false;
		} else if (!houseValue.equals(other.houseValue))
			return false;
		if (openTime == null) {
			if (other.openTime != null)
				return false;
		} else if (!openTime.equals(other.openTime))
			return false;
		if (propertyType == null) {
			if (other.propertyType != null)
				return false;
		} else if (!propertyType.equals(other.propertyType))
			return false;
		if (registerTime == null) {
			if (other.registerTime != null)
				return false;
		} else if (!registerTime.equals(other.registerTime))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
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
		if (secondMortgageOtherBank == null) {
			if (other.secondMortgageOtherBank != null)
				return false;
		} else if (!secondMortgageOtherBank.equals(other.secondMortgageOtherBank))
			return false;
		if (secondMortgageTime == null) {
			if (other.secondMortgageTime != null)
				return false;
		} else if (!secondMortgageTime.equals(other.secondMortgageTime))
			return false;
		if (secondMortgageTotal == null) {
			if (other.secondMortgageTotal != null)
				return false;
		} else if (!secondMortgageTotal.equals(other.secondMortgageTotal))
			return false;
		if (secondMortgageType == null) {
			if (other.secondMortgageType != null)
				return false;
		} else if (!secondMortgageType.equals(other.secondMortgageType))
			return false;
		if (secondMortgageYear == null) {
			if (other.secondMortgageYear != null)
				return false;
		} else if (!secondMortgageYear.equals(other.secondMortgageYear))
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
		if (thirdMortgageOtherBank == null) {
			if (other.thirdMortgageOtherBank != null)
				return false;
		} else if (!thirdMortgageOtherBank.equals(other.thirdMortgageOtherBank))
			return false;
		if (thirdMortgageTime == null) {
			if (other.thirdMortgageTime != null)
				return false;
		} else if (!thirdMortgageTime.equals(other.thirdMortgageTime))
			return false;
		if (thirdMortgageTotal == null) {
			if (other.thirdMortgageTotal != null)
				return false;
		} else if (!thirdMortgageTotal.equals(other.thirdMortgageTotal))
			return false;
		if (thirdMortgageType == null) {
			if (other.thirdMortgageType != null)
				return false;
		} else if (!thirdMortgageType.equals(other.thirdMortgageType))
			return false;
		if (thirdMortgageYear == null) {
			if (other.thirdMortgageYear != null)
				return false;
		} else if (!thirdMortgageYear.equals(other.thirdMortgageYear))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		if (xiaodaiHouseId == null) {
			if (other.xiaodaiHouseId != null)
				return false;
		} else if (!xiaodaiHouseId.equals(other.xiaodaiHouseId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessHouse [businessHouseId=" + businessHouseId + ", xiaodaiHouseId=" + xiaodaiHouseId
				+ ", businessId=" + businessId + ", houseValue=" + houseValue + ", houseName=" + houseName
				+ ", houseAddress=" + houseAddress + ", houseNo=" + houseNo + ", houseArea=" + houseArea
				+ ", housePrice=" + housePrice + ", houseTotal=" + houseTotal + ", butTime=" + butTime
				+ ", registerTime=" + registerTime + ", houseBelong=" + houseBelong + ", openTime=" + openTime
				+ ", borrowTime=" + borrowTime + ", borrowType=" + borrowType + ", borrowTotal=" + borrowTotal
				+ ", borrowBalance=" + borrowBalance + ", fsdBankName=" + fsdBankName + ", debtRegion=" + debtRegion
				+ ", remark=" + remark + ", updateUser=" + updateUser + ", updateTime=" + updateTime
				+ ", secondMortgageTime=" + secondMortgageTime + ", secondMortgageYear=" + secondMortgageYear
				+ ", secondMortgageType=" + secondMortgageType + ", secondMortgageBank=" + secondMortgageBank
				+ ", secondMortgageOtherBank=" + secondMortgageOtherBank + ", secondMortgageTotal="
				+ secondMortgageTotal + ", secondMortgageBalance=" + secondMortgageBalance + ", thirdMortgageTime="
				+ thirdMortgageTime + ", thirdMortgageYear=" + thirdMortgageYear + ", thirdMortgageType="
				+ thirdMortgageType + ", thirdMortgageBank=" + thirdMortgageBank + ", thirdMortgageOtherBank="
				+ thirdMortgageOtherBank + ", thirdMortgageTotal=" + thirdMortgageTotal + ", thirdMortgageBalance="
				+ thirdMortgageBalance + ", fourthMortgageTime=" + fourthMortgageTime + ", fourthMortgageYear="
				+ fourthMortgageYear + ", fourthMortgageType=" + fourthMortgageType + ", fourthMortgageBank="
				+ fourthMortgageBank + ", fourthMortgageOtherBank=" + fourthMortgageOtherBank + ", fourthMortgageTotal="
				+ fourthMortgageTotal + ", fourthMortgageBalance=" + fourthMortgageBalance + ", propertyType="
				+ propertyType + "]";
	}

}
