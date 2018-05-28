package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   标额外的费率信息
 */
@ApiModel("标额外的费率信息")
public class ProjExtRateReq {

    /**
     * 费率类型 ,70,违约金；
     */
    @ApiModelProperty(required= true,value = "费率类型：70,违约金")
    @NotNull(message = "费率类型(rateType)不能为空")
    private Integer rateType;


    /**
     * 费率名称
     */
    @ApiModelProperty(value = "费率名称，对应费率类型对应的名称，不填则从系统默认对应关系中取值")
    private String rateName;
    /**
     * 费率值
     */
    @ApiModelProperty(required= true,value = "费率值")
    @NotNull(message = "费率值(rateValue)不能为空")
    private BigDecimal rateValue;


    /**
     * 费用计算方式
     */
    @ApiModelProperty(required= true,value = "费用计算方式:1.借款金额*费率值；2剩余本金*费率值;3,1*费率值")
    @NotNull(message = "费用计算方式(calcWay)不能为空")
    private Integer calcWay;


    @ApiModelProperty(required= true,value = "资产端费用项ID：违约金，79069922-e13a-4229-8656-2a1e19b44879；本金违约金， bff1558f-5a9f-4f7c-87c9-90322fd09080；" +
            "月收服务费违约金，60e4c310-165d-4b59-beb1-66b878a51c48；平台服务费违约金，1192f59f-e5ad-4773-b785-8c41b43a4fa6")
    @NotNull(message="资产端费用项ID(feeId)不能为空")
    private String feeId;
    /**
     * 资产端费用项名称
     */
    @ApiModelProperty(value = "资产端费用项名称，费用ID对应的费用项名称，不填则取系统默认对应的名称填充")
    private String feeName;


    /**
     * 开始期数
     */
    @ApiModelProperty(required= true,value = "开始期数")
    @NotNull(message = "开始期数(beginPeroid)不能为空")
    private Integer beginPeroid;


    /**
     * 结束期数
     */
    @ApiModelProperty(required= true,value = "结束期数")
    @NotNull(message = "结束期数(endPeroid)不能为空")
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
