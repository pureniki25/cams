package com.hongte.alms.base.RepayPlan.req.trial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.RepayPlan.req.PrincipleReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("上标信息")
//@Data
public class TrailProjInfoReq {


    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号")
    private String projectId;

    /**
     *利率
     */
    @ApiModelProperty(required= true,value = "利率")
    @NotNull(message = "利率(rate)不能为空")
    private BigDecimal rate;

    @ApiModelProperty(required= true,value = "利率单位：1 年利率; 2 月利率; 3 日利率")
    @NotNull(message = "利率单位(rateUnitType)不能为空")
    private Integer rateUnitType;

//    @ApiModelProperty(required= true,value = "逾期滞纳金费率(%)")
//    private BigDecimal overDueRate;
//
//    @ApiModelProperty(required= true,value = "逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
//    private  Integer overDueRateUnit;


    @ApiModelProperty(required= true,value = "还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息")
    @NotNull(message = "还款方式(repayType)不能为空")
    private Integer repayType;

    @ApiModelProperty(value = "每期还本Map  Map<期数，还本金额>")
    private List<PrincipleReq> pricipleMap;

    /**
     * 满标金额(元)
     */
    @ApiModelProperty(required= true,value = "满标金额(元)")
    @NotNull(message = "满标金额(fullBorrowMoney)不能为空")
    private BigDecimal fullBorrowMoney;

    /**
     * 满标时间
     */
    @ApiModelProperty(required= true,value = "满标时间")
    @NotNull(message="ProjInfoReq 满标时间(queryFullsuccessDate)不能为空")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryFullsuccessDate;


    /**
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    @NotNull(message = "借款期限(periodMonth)不能为空")
    private Integer periodMonth;

    /**
     * 标的费用信息列表
     */
    @ApiModelProperty(required= true,value = "标的的出款费用信息列表")
    @NotNull(message = "标的费用信息列表(projFeeInfos)不能为空")
    private List<TrailProjFeeReq> projFeeInfos;

//    /**
//     * 业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)
//     */
//    @ApiModelProperty(required= true,value = "业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)")
//    private Integer projectType;



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
//    public Integer getOffLineInOverDueRateType() {
//        return overDueRateUnit;
//    }
//
//    public void setOffLineInOverDueRateType(Integer overDueRateUnit) {
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

    public Date getQueryFullsuccessDate() {
        return queryFullsuccessDate;
    }

    public void setQueryFullsuccessDate(Date queryFullsuccessDate) {
        this.queryFullsuccessDate = queryFullsuccessDate;
    }

//    public Integer getProjectType() {
//        return projectType;
//    }
//
//    public void setProjectType(Integer projectType) {
//        this.projectType = projectType;
//    }
}
