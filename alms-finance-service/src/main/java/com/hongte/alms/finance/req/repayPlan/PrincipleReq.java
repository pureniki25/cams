package com.hongte.alms.finance.req.repayPlan;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/5/9
 * 每期还本金信息
 */
public class PrincipleReq {

    private Integer period;

    private BigDecimal  principle;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getPrinciple() {
        return principle;
    }

    public void setPrinciple(BigDecimal principle) {
        this.principle = principle;
    }
}
