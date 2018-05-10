package com.hongte.alms.finance.req.repayPlan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("标的出款申请费用明细期限范围明细")
public class ProjFeeDetailReq {


    /**
     * 上标编号
     */

    @ApiModelProperty(required= true,value = "期数")
    private Integer peroid;

    /**
     * 本期金额
     */
    @ApiModelProperty(required= true,value = "本期金额")
    private BigDecimal feeValue;


    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    public Integer getPeroid() {
        return peroid;
    }

    public void setPeroid(Integer peroid) {
        this.peroid = peroid;
    }
}
