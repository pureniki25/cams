package com.hongte.alms.base.RepayPlan.dto;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/9/17
 */
public class TrailProjPlanListDto {



    /**
     * 期数
     */
    private Integer period;

    /**
     * 需还款金额（本金）
     */
    private BigDecimal amount;

    /**
     * 需还款利息
     */
    private BigDecimal interestAmount;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

}
