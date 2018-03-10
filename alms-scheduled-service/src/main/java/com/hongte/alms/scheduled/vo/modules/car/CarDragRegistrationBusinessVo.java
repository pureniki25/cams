package com.hongte.alms.scheduled.vo.modules.car;

/**
 * @author dengzhiming
 * @date 2018/2/24 18:17
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 * 拖车登记页面显示的业务信息
 */
public class CarDragRegistrationBusinessVo {
    /**
     * 业务编号
     */
    private String businessId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 借款金额
     */
    private BigDecimal borrowMoney;

    /**
     * 借款期限
     */
    private Integer borrowLimit;

    /**
     * 借款期限单位，1：月，2：天
     */
    private Integer borrowLimitUnit;

    /**
     * 还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
     */
    private Integer repaymentTypeId;

    /**
     * 借款利率(%)，如11%则存11.0
     */
    private BigDecimal borrowRate;

    /**
     * 借款利率类型，1：年利率，2：月利率，3：日利率
     */
    private Integer borrowRateUnit;

    /**
     * 车牌号
     */
    private String licensePlateNumber;

    /**
     * 品牌型号
     */
    private String model;

    /**
     * 车架号
     */
    private String vin;

    /**
     *评估时间
     */
    private Date evaluationTime;

    /**
     * 车辆评估价值
     */
    private BigDecimal evaluationAmount;

    /**
     * 评估人
     */
    private String evaluationUser;



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

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public Integer getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public Integer getBorrowLimitUnit() {
        return borrowLimitUnit;
    }

    public void setBorrowLimitUnit(Integer borrowLimitUnit) {
        this.borrowLimitUnit = borrowLimitUnit;
    }

    public Integer getRepaymentTypeId() {
        return repaymentTypeId;
    }

    public void setRepaymentTypeId(Integer repaymentTypeId) {
        this.repaymentTypeId = repaymentTypeId;
    }

    public BigDecimal getBorrowRate() {
        return borrowRate;
    }

    public void setBorrowRate(BigDecimal borrowRate) {
        this.borrowRate = borrowRate;
    }

    public Integer getBorrowRateUnit() {
        return borrowRateUnit;
    }

    public void setBorrowRateUnit(Integer borrowRateUnit) {
        this.borrowRateUnit = borrowRateUnit;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public BigDecimal getEvaluationAmount() {
        return evaluationAmount;
    }

    public void setEvaluationAmount(BigDecimal evaluationAmount) {
        this.evaluationAmount = evaluationAmount;
    }

    public String getEvaluationUser() {
        return evaluationUser;
    }

    public void setEvaluationUser(String evaluationUser) {
        this.evaluationUser = evaluationUser;
    }


}
