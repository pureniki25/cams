package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   业务额外的费率信息
 */
@ApiModel("业务额外的费率信息")
public class BusinessExtRateReq {

//    /**
//     * 业务ID
//     */
//    @ApiModelProperty(required= true,value = "业务ID")
//    private String businessId;
    /**
     * 费率类型 1，
     */
    @ApiModelProperty(required= true,value = "费率类型")
    private Integer rateType;
    /**
     * 费率名称
     */
    @ApiModelProperty(required= true,value = "费率名称")
    private String rateName;
    /**
     * 费率值
     */
    @ApiModelProperty(required= true,value = "费率值")
    private BigDecimal rateValue;
    /**
     * 费率值单位，1：年利率，2：月利率，3：日利率
     */
    @ApiModelProperty(required= true,value = "费率值单位，1：年利率，2：月利率，3：日利率")
    private Integer rateUnit;
    /**
     * 资产端费用项ID
     */
    @ApiModelProperty(value = "资产端费用项ID")
    private String feeId;
    /**
     * 资产端费用项名称
     */
    @ApiModelProperty(value = "资产端费用项名称")
    private String feeName;


//    public String getBusinessId() {
//        return businessId;
//    }
//
//    public void setBusinessId(String businessId) {
//        this.businessId = businessId;
//    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public BigDecimal getRateValue() {
        return rateValue;
    }

    public void setRateValue(BigDecimal rateValue) {
        this.rateValue = rateValue;
    }

    public Integer getRateUnit() {
        return rateUnit;
    }

    public void setRateUnit(Integer rateUnit) {
        this.rateUnit = rateUnit;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
}
