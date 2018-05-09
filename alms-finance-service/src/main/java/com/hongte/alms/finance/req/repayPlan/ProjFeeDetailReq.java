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
    @ApiModelProperty(required= true,value = "上标编号")
    private String projId;

    @ApiModelProperty(required= true,value = "期数")
    private Integer peroid;

    /**
     * 本期金额
     */
    @ApiModelProperty(required= true,value = "本期金额")
    private BigDecimal feeValue;
//
//    /**
//     * 最小期限范围
//     */
//    @ApiModelProperty(required= true,value = "最小期限范围")
//    private Integer feeTermRangeMin;
//
//
//
//
//    /**
//     * 最大期限范围
//     */
//    @ApiModelProperty(required= true,value = "最大期限范围")
//    private Integer feeTermRangeMax;
////    /**
////     * 费用项ID
////     */
////    @ApiModelProperty(required= true,value = "费用项ID")
////    private String feeId;
////    /**
////     * 费用名称
////     */
////    @ApiModelProperty(required= true,value = "费用名称")
////    private String feeName;
//    /**
//     * 本期金额
//     */
//    @ApiModelProperty(required= true,value = "本期金额")
//    private BigDecimal feeValue;
//    /**
//     * 本期比例
//     */
//    @ApiModelProperty(value = "本期比例")
//    private BigDecimal feeRate;
//    /**
//     * 费用收取方式，1为按比例，2为按金额
//     */
//    @ApiModelProperty(required= true,value = "费用收取方式，1为按比例，2为按金额")
//    private Integer feeChargingType;
//    /**
//     * 利率,1为年利率，2为月利率，3为日利率
//     */
//    @ApiModelProperty(value = "利率,1为年利率，2为月利率，3为日利率")
//    private Integer feeRateUnit;



    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }


    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getPeroid() {
        return peroid;
    }

    public void setPeroid(Integer peroid) {
        this.peroid = peroid;
    }
}
