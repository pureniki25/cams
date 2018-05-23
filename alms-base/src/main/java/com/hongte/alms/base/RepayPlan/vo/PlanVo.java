package com.hongte.alms.base.RepayPlan.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/5/23
 */
public class PlanVo implements java.io.Serializable  {

    private  String period;  //View期数

    private  String  afterId;  //DB期数

    private Date date;  //还款日期

    private BigDecimal totalAmount;  //待还款总金额

    private String status;  //还款状态 还款中 逾期 已还款

    private  Boolean hasDeffer;  //是否已经展期

    private  Boolean isOver; //是否已经结清

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
