package com.hongte.alms.base.RepayPlan.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业务还款计划
 * @author zengkun
 * @since 2018/5/23
 */
public class BizPlanDto  implements Serializable {

    //还款计划ID
    private String planId;

//    private String  planStatus; //还款计划状态

    private Boolean hasDeffer; //是否已经展期

    private Boolean isOver;//是否已经结清


    private List<BizPlanListDto> payedPeroids;//已支付的还款计划列表

    private List<BizPlanListDto> payingPeroids;//待支付的还款计划列表


    private BizPlanListDto currentPeroid;//当前期


    private String businessType ;//业务类型
    private  String repayWay;//还款方式
    private BigDecimal borrowMoney;//借款总金额
    private Integer borrowLimit;//借款期限
    private String borrowLimitUnit;//借款期限单位

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inputTime;//进件日期


    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

//    public String getPlanStatus() {
//        return planStatus;
//    }
//
//    public void setPlanStatus(String planStatus) {
//        this.planStatus = planStatus;
//    }

    public List<BizPlanListDto> getPayedPeroids() {
        return payedPeroids;
    }

    public void setPayedPeroids(List<BizPlanListDto> payedPeroids) {
        this.payedPeroids = payedPeroids;
    }

    public List<BizPlanListDto> getPayingPeroids() {
        return payingPeroids;
    }

    public void setPayingPeroids(List<BizPlanListDto> payingPeroids) {
        this.payingPeroids = payingPeroids;
    }

    public BizPlanListDto getCurrentPeroid() {
        return currentPeroid;
    }

    public void setCurrentPeroid(BizPlanListDto currentPeroid) {
        this.currentPeroid = currentPeroid;
    }

    public Boolean getHasDeffer() {
        return hasDeffer;
    }

    public void setHasDeffer(Boolean hasDeffer) {
        this.hasDeffer = hasDeffer;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean over) {
        isOver = over;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRepayWay() {
        return repayWay;
    }

    public void setRepayWay(String repayWay) {
        this.repayWay = repayWay;
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

    public String getBorrowLimitUnit() {
        return borrowLimitUnit;
    }

    public void setBorrowLimitUnit(String borrowLimitUnit) {
        this.borrowLimitUnit = borrowLimitUnit;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }
}
