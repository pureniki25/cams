package com.hongte.alms.finance.req.repayPlan.trial;

import com.hongte.alms.finance.req.repayPlan.PrincipleReq;
import com.hongte.alms.finance.req.repayPlan.ProjFeeReq;
import com.hongte.alms.finance.req.repayPlan.ProjectCarInfoReq;
import com.hongte.alms.finance.req.repayPlan.ProjectHouseInfoReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("上标信息")
public class TrailProjInfoReq {


    /**
     *利率
     */
    @ApiModelProperty(required= true,value = "利率")
    private BigDecimal rate;

    @ApiModelProperty(required= true,value = "利率单位：1 年利率; 2 月利率; 3 日利率")
    private Integer rateUnitType;

//    @ApiModelProperty(required= true,value = "逾期滞纳金费率(%)")
//    private BigDecimal overDueRate;
//
//    @ApiModelProperty(required= true,value = "逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
//    private  Integer overDueRateUnit;


    @ApiModelProperty(required= true,value = "还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息")
    private Integer repayType;

    @ApiModelProperty(required= true,value = "每期还本Map  Map<期数，还本金额>")
    private List<PrincipleReq> pricipleMap;

    /**
     * 满标金额(元)
     */
    @ApiModelProperty(required= true,value = "满标金额(元)")
    private BigDecimal fullBorrowMoney;

    /**
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    private Integer periodMonth;

    /**
     * 标的费用信息列表
     */
    @ApiModelProperty(required= true,value = "标的的出款费用信息列表")
    private List<TrailProjFeeReq> projFeeInfos;

    /**
     * 业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)
     */
    @ApiModelProperty(required= true,value = "业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)")
    private Integer projectType;



    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getRateUnitType() {
        return rateUnitType;
    }

    public void setRateUnitType(Integer rateUnitType) {
        this.rateUnitType = rateUnitType;
    }

    public Integer getRepayType() {
        return repayType;
    }

    public void setRepayType(Integer repayType) {
        this.repayType = repayType;
    }

//    public BigDecimal getOffLineInOverDueRate() {
//        return overDueRate;
//    }
//
//    public void setOffLineInOverDueRate(BigDecimal overDueRate) {
//        this.overDueRate = overDueRate;
//    }
//
//    public Integer getOffLineInOverDueRateUnit() {
//        return overDueRateUnit;
//    }
//
//    public void setOffLineInOverDueRateUnit(Integer overDueRateUnit) {
//        this.overDueRateUnit = overDueRateUnit;
//    }

    public List<TrailProjFeeReq> getProjFeeInfos() {
        return projFeeInfos;
    }

    public void setProjFeeInfos(List<TrailProjFeeReq> projFeeInfos) {
        this.projFeeInfos = projFeeInfos;
    }

    public List<PrincipleReq> getPricipleMap() {
        return pricipleMap;
    }

    public void setPricipleMap( List<PrincipleReq> pricipleMap) {
        this.pricipleMap = pricipleMap;
    }

    public BigDecimal getFullBorrowMoney() {
        return fullBorrowMoney;
    }

    public void setFullBorrowMoney(BigDecimal fullBorrowMoney) {
        this.fullBorrowMoney = fullBorrowMoney;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }
}
