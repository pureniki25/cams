package com.hongte.alms.finance.dto.repayPlan.app;

import java.util.List;

/**
 * 业务还款计划
 * @author zengkun
 * @since 2018/5/23
 */
public class BizPlanDto {

    //还款计划ID
    private String planId;

//    private String  planStatus; //还款计划状态

    private Boolean hasDeffer; //是否已经展期

    private Boolean isOver;//是否已经结清


    private List<BizPlanListDto> payedPeroids;//已支付的还款计划列表

    private List<BizPlanListDto> payingPeroids;//待支付的还款计划列表


    private BizPlanListDto currentPeroid;//当前期


    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

//    public String getPlanStatus() {
//        return planStatus;
//    }
//
//    public void setPlanStatus(String planStatus) {
//        this.planStatus = planStatus;
//    }

    public List<BizPlanListDto> getPayedPeroids() {
        return payedPeroids;
    }

    public void setPayedPeroids(List<BizPlanListDto> payedPeroids) {
        this.payedPeroids = payedPeroids;
    }

    public List<BizPlanListDto> getPayingPeroids() {
        return payingPeroids;
    }

    public void setPayingPeroids(List<BizPlanListDto> payingPeroids) {
        this.payingPeroids = payingPeroids;
    }

    public BizPlanListDto getCurrentPeroid() {
        return currentPeroid;
    }

    public void setCurrentPeroid(BizPlanListDto currentPeroid) {
        this.currentPeroid = currentPeroid;
    }

    public Boolean getHasDeffer() {
        return hasDeffer;
    }

    public void setHasDeffer(Boolean hasDeffer) {
        this.hasDeffer = hasDeffer;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean over) {
        isOver = over;
    }
}
