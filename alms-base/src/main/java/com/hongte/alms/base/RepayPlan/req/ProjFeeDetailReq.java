package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("标的出款申请费用明细期限范围明细")
public class ProjFeeDetailReq {


    /**
     * 期数
     */

    @ApiModelProperty(required= true,value = "期数")
    @NotNull(message = "期数(period)不能为空")
    private Integer period;

    /**
     * 本期金额
     */
    @ApiModelProperty(required= true,value = "本期金额")
    @NotNull(message = "本期金额(feeValue)不能为空")
    private BigDecimal feeValue;


    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
