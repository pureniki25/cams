package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;

/** 申请减免界面的基本信息
 * @author zengkun
 * @since 2018/2/5
 * 业务信息Vo
 */
public class BusinessInfoForApplyDerateVo  {

    private String businessId		   	; //业务编号
    private String customerName        ; //客户名称
    private String companyId           ; //所属分公司ID
    private String companyName  		; //所属分公司		需处理
    private Integer businessType        ; //业务类型Id
    private String businessTypeName	; //业务类型   		需处理
    private Integer borrowLimit         ; //借款期限
    private Integer repaymentTypeId     ; //还款方式Id
    private String repaymentTypeName   ; //还款方式  		需处理
    private BigDecimal borrowRate   			; //借款利率
    private String borrowRateStr   			; //借款利率
    private Integer borrowRateUnit   			; //借款利率类型ID
    private String borrowRateName		; //借款利率名称 		需处理
    private BigDecimal borrowMoney         ; //借款金额
    private BigDecimal getMoney 			; //出款金额   		待找
    private BigDecimal remianderPrincipal 	; //剩余本金   		待计算
    private Integer periods             ; //当前还款期数
    private Integer delayDays           ; //逾期天数
    private BigDecimal needPayInterest  	; //应付利息
    private BigDecimal needPayPrincipal   	; //应付本金
    private BigDecimal needPayPenalty 		; //应付违约金
    private BigDecimal otherPayAmount 		; //应付其他费用
    private BigDecimal totalBorrowAmount   ; //应付总额


     
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

     
    public String getCompanyId() {
        return companyId;
    }

     
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

     
    public Integer getBusinessType() {
        return businessType;
    }

     
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
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

    public String getRepaymentTypeName() {
        return repaymentTypeName;
    }

    public void setRepaymentTypeName(String repaymentTypeName) {
        this.repaymentTypeName = repaymentTypeName;
    }

     
    public BigDecimal getBorrowRate() {
        return borrowRate;
    }

    public void setBorrowRate(BigDecimal borrowRate) {
        this.borrowRate = borrowRate;
    }

    public String getBorrowRateName() {
        return borrowRateName;
    }

    public void setBorrowRateName(String borrowRateName) {
        this.borrowRateName = borrowRateName;
    }

     
    public  BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney( BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public  BigDecimal getGetMoney() {
        return getMoney;
    }

    public void setGetMoney( BigDecimal getMoney) {
        this.getMoney = getMoney;
    }

    public  BigDecimal getRemianderPrincipal() {
        return remianderPrincipal;
    }

    public void setRemianderPrincipal( BigDecimal remianderPrincipal) {
        this.remianderPrincipal = remianderPrincipal;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    public  BigDecimal getNeedPayInterest() {
        return needPayInterest;
    }

    public void setNeedPayInterest( BigDecimal needPayInterest) {
        this.needPayInterest = needPayInterest;
    }

    public  BigDecimal getNeedPayPrincipal() {
        return needPayPrincipal;
    }

    public void setNeedPayPrincipal( BigDecimal needPayPrincipal) {
        this.needPayPrincipal = needPayPrincipal;
    }

    public  BigDecimal getNeedPayPenalty() {
        return needPayPenalty;
    }

    public void setNeedPayPenalty( BigDecimal needPayPenalty) {
        this.needPayPenalty = needPayPenalty;
    }

    public  BigDecimal getOtherPayAmount() {
        return otherPayAmount;
    }

    public void setOtherPayAmount( BigDecimal otherPayAmount) {
        this.otherPayAmount = otherPayAmount;
    }

    public  BigDecimal getTotalBorrowAmount() {
        return totalBorrowAmount;
    }

    public void setTotalBorrowAmount( BigDecimal totalBorrowAmount) {
        this.totalBorrowAmount = totalBorrowAmount;
    }

    public Integer getBorrowRateUnit() {
        return borrowRateUnit;
    }

    public void setBorrowRateUnit(Integer borrowRateUnit) {
        this.borrowRateUnit = borrowRateUnit;
    }

    public String getBorrowRateStr() {
        return borrowRateStr;
    }

    public void setBorrowRateStr(String borrowRateStr) {
        this.borrowRateStr = borrowRateStr;
    }
}
