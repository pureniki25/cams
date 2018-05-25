package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   标额外的费率信息
 */
@ApiModel("标额外的费率信息")
public class ProjExtRateReq {

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
     * 费用计算方式
     */
    @ApiModelProperty(required= true,value = "费用计算方式")
    private Integer calcWay;
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


    /**
     * 开始期数
     */
    @ApiModelProperty(value = "开始期数")
    private Integer beginPeroid;


    /**
     * 结束期数
     */
    @ApiModelProperty(value = "结束期数")
    private  Integer  endPeroid;



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

    public Integer getCalcWay() {
        return calcWay;
    }

    public void setCalcWay(Integer calcWay) {
        this.calcWay = calcWay;
    }

    public Integer getBeginPeroid() {
        return beginPeroid;
    }

    public void setBeginPeroid(Integer beginPeroid) {
        this.beginPeroid = beginPeroid;
    }

    public Integer getEndPeroid() {
        return endPeroid;
    }

    public void setEndPeroid(Integer endPeroid) {
        this.endPeroid = endPeroid;
    }
}
