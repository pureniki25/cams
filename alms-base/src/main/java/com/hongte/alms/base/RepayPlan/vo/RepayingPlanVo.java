package com.hongte.alms.base.RepayPlan.vo;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 还款中的还款计划信息(提供给App)
 * @author zengkun
 * @since 2018/5/23
 */
public class RepayingPlanVo implements java.io.Serializable  {

    private String  businessId; //业务编号

    private String orgBusinessId; //原始业务编号

    private Boolean hasDeffer; //是否已经展期

    private String businessType; //业务类型

    private Boolean isOver;//是否已经结清

    private  String tip; //已展期/已结清（待还款列表为空时显示）

    private List<PlanVo> plans;

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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean over) {
        isOver = over;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<PlanVo> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanVo> plans) {
        this.plans = plans;
    }
}
