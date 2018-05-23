package com.hongte.alms.base.RepayPlan.vo;

import java.util.List;

/**
 * 还款中的还款计划信息（从数据库查询出来的Dto）
 * @author zengkun
 * @since 2018/5/23
 */
public class RepayingPlanDto implements java.io.Serializable  {

    private String  businessId; //业务编号

    private String orgBusinessId; //原始业务编号

    private Integer  planStatus; //还款计划状态

    private Integer bizTypeId;//业务类型Id
    private String businessType; //业务类型


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


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public Integer getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(Integer bizTypeId) {
        this.bizTypeId = bizTypeId;
    }
}
