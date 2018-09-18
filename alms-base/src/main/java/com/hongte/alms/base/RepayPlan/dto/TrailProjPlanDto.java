package com.hongte.alms.base.RepayPlan.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/9/17
 * 上标还款计划试算Dto
 */
public class TrailProjPlanDto {

    /**
     * 标的编号
     */
    private String projectId;

    /**
     * 业务编号
     */
    private String businessId;


    /**
     * 每一期应还信息列表
     */
    private List<TrailProjPlanListDto> projPlanLists;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<TrailProjPlanListDto> getProjPlanLists() {
        return projPlanLists;
    }

    public void setProjPlanLists(List<TrailProjPlanListDto> projPlanLists) {
        this.projPlanLists = projPlanLists;
    }
}
