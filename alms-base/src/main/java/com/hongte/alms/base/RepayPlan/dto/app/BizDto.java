package com.hongte.alms.base.RepayPlan.dto.app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业务信息
 * @author zengkun
 * @since 2018/5/23
 */
public class BizDto  implements Serializable {
    private String  businessId; //业务编号

    private String orgBusinessId; //原始业务编号

    private String businessType ;//业务类型
    private Boolean hasDeffer; //是否已经展期

    private Boolean isOver;//是否已经结清

    private  String repayWay;//还款方式

    private BigDecimal borrowMoney;//借款总金额

    private Integer borrowLimit;//借款期限

    private String borrowLimitUnit;//借款期限单位

    private Date inputTime;//进件日期


    private Integer plateType;//平台类型：1、团贷网；2、你我金融

    private String identityCard;//身份证编码（主借款人的）


//
//    	`borrow_limit` INT(11) NOT NULL COMMENT '借款期限',
//            `borrow_limit_unit` INT(11) NOT NULL COMMENT '借款期限单位，1：月，2：天',

//    private  String tip; //已展期/已结清（待还款列表为空时显示）


    private List<BizPlanDto> planDtoList;//还款计划列表

    private List<BizDto> renewBizs;//已申请展期的展期信息列表

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOrgBusinessId() {
        return orgBusinessId;
    }

    public void setOrgBusinessId(String orgBusinessId) {
        this.orgBusinessId = orgBusinessId;
    }

    public Boolean getHasDeffer() {
        return hasDeffer;
    }

    public void setHasDeffer(Boolean hasDeffer) {
        this.hasDeffer = hasDeffer;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }

//    public String getTip() {
//        return tip;
//    }
//
//    public void setTip(String tip) {
//        this.tip = tip;
//    }

    public List<BizPlanDto> getPlanDtoList() {
        return planDtoList;
    }

    public void setPlanDtoList(List<BizPlanDto> planDtoList) {
        this.planDtoList = planDtoList;
    }

    public List<BizDto> getRenewBizs() {
        return renewBizs;
    }

    public void setRenewBizs(List<BizDto> renewBizs) {
        this.renewBizs = renewBizs;
    }

	/**
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

    public String getRepayWay() {
        return repayWay;
    }

    public void setRepayWay(String repayWay) {
        this.repayWay = repayWay;
    }

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public Integer getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public String getBorrowLimitUnit() {
        return borrowLimitUnit;
    }

    public void setBorrowLimitUnit(String borrowLimitUnit) {
        this.borrowLimitUnit = borrowLimitUnit;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
