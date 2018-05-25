package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/5/9
 * 每期还本金信息
 */
public class PrincipleReq {

    @ApiModelProperty(required= true,value = "期数")
    @NotNull(message = "期数(period)不能为空")
    private Integer period;

    @ApiModelProperty(required= true,value = "本金")
    @NotNull(message = "本金(principle)不能为空")
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
