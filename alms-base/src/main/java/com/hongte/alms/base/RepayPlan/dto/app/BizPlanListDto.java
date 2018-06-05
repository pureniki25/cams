package com.hongte.alms.base.RepayPlan.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.entity.RepaymentBizPlanList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/5/23
 */
public class BizPlanListDto  implements Serializable {

    private  Integer period;  //View期数

    private  String  afterId;  //DB期数

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;  //应还日期

    private BigDecimal totalAmount;  //待还款总金额

    private BigDecimal overdueAmount; //滞纳金

    private BigDecimal derateAmount;//减免金额

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payDate;  //实还日期

//    private BigDecimal payedAmount;//实还金额

    private String status;  //还款状态 还款中 逾期 已还款

    // 是否是本期的标志位
    private Boolean isCurrentPeriod = false;

    public BizPlanListDto(){

    }

    public  BizPlanListDto(RepaymentBizPlanList planList){
        this.afterId = planList.getAfterId();
        this.period = planList.getPeriod();
        this.dueDate = planList.getDueDate();
        this.totalAmount = planList.getTotalBorrowAmount();
        this.payDate = planList.getFactRepayDate();
        this.status = planList.getCurrentStatus();
        this.overdueAmount = planList.getOverdueAmount();
        this.derateAmount =planList.getDerateAmount();
//        this.payedAmount = planList.getF
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

//    public BigDecimal getPayedAmount() {
//        return payedAmount;
//    }
//
//    public void setPayedAmount(BigDecimal payedAmount) {
//        this.payedAmount = payedAmount;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsCurrentPeriod() {
        return isCurrentPeriod;
    }

    public void setIsCurrentPeriod(Boolean currentPeriod) {
        isCurrentPeriod = currentPeriod;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getDerateAmount() {
        return derateAmount;
    }

    public void setDerateAmount(BigDecimal derateAmount) {
        this.derateAmount = derateAmount;
    }
}
