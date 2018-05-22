package com.hongte.alms.base.collection.vo;

import java.math.BigDecimal;

/**
 * 执行代扣信息vo
 */
public class DeductionVo {
    private String businessId; //业务编号
    private String originalBusinessId; //原业务编号
    private Integer borrowLimit;  // 借款期限
    private String borrowLimitUnit; //借款期限单位
    private String customerName;//客户姓名
    private String identifyCard;//客户身份证号码
    private double planPrincipal; //本金
    private double platformCharge; //平台费
    private String afterId; //还款期数
    private String bankCard; //客户绑定银行卡号
    private String bankName; //绑定银行卡名称
    private double planAccrual;    //利息
    private double planGuaranteeCharge;    //担保费
    private double otherFee;           //其他费用
    private String total; //本期应还金额


    private double borrowMoney;   //借款金额
    private String phoneNumber;//客户手机号码
    private String platformName; //代扣平台名称
    private double  planServiceCharge;//服务费
    private double  planOverDueMoney;//逾期费用
    private String nowdate;//当前日期
    
    private String repaymentTypeName;//还款方式
    private String  operatorName;//业务主办人
    private Integer platformId;
    
    private BigDecimal underLineOverDueMoney;//线下逾期费
    private BigDecimal onLineOverDueMoney;//线上逾期费
    
    
    public BigDecimal getUnderLineOverDueMoney() {
		return underLineOverDueMoney;
	}
	public void setUnderLineOverDueMoney(BigDecimal underLineOverDueMoney) {
		this.underLineOverDueMoney = underLineOverDueMoney;
	}
	public BigDecimal getOnLineOverDueMoney() {
		return onLineOverDueMoney;
	}
	public void setOnLineOverDueMoney(BigDecimal onLineOverDueMoney) {
		this.onLineOverDueMoney = onLineOverDueMoney;
	}
	public Integer getPlatformId() {
 		return platformId;
 	}
 	public void setPlatformId(Integer platformId) {
 		this.platformId = platformId;
 	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	private String  currentStatus;//还款状态
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getOriginalBusinessId() {
		return originalBusinessId;
	}
	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}
	public Integer getBorrowLimit() {
		return borrowLimit;
	}
	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}
	public String getBorrowLimitUnit() {
		return borrowLimitUnit;
	}
	public void setBorrowLimitUnit(String borrowLimitUnit) {
		this.borrowLimitUnit = borrowLimitUnit;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIdentifyCard() {
		return identifyCard;
	}
	public void setIdentifyCard(String identifyCard) {
		this.identifyCard = identifyCard;
	}
	public double getPlanPrincipal() {
		return planPrincipal;
	}
	public void setPlanPrincipal(double planPrincipal) {
		this.planPrincipal = planPrincipal;
	}
	public double getPlatformCharge() {
		return platformCharge;
	}
	public void setPlatformCharge(double platformCharge) {
		this.platformCharge = platformCharge;
	}
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public double getPlanAccrual() {
		return planAccrual;
	}
	public void setPlanAccrual(double planAccrual) {
		this.planAccrual = planAccrual;
	}
	public double getPlanGuaranteeCharge() {
		return planGuaranteeCharge;
	}
	public void setPlanGuaranteeCharge(double planGuaranteeCharge) {
		this.planGuaranteeCharge = planGuaranteeCharge;
	}
	public double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public double getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(double borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public double getPlanServiceCharge() {
		return planServiceCharge;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setPlanServiceCharge(double planServiceCharge) {
		this.planServiceCharge = planServiceCharge;
	}
	public double getPlanOverDueMoney() {
		return planOverDueMoney;
	}
	public void setPlanOverDueMoney(double planOverDueMoney) {
		this.planOverDueMoney = planOverDueMoney;
	}
	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public String getRepaymentTypeName() {
		return repaymentTypeName;
	}
	public void setRepaymentTypeName(String repaymentTypeName) {
		this.repaymentTypeName = repaymentTypeName;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
    
    
    

