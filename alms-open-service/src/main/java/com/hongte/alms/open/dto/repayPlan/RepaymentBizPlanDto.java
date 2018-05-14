package com.hongte.alms.open.dto.repayPlan;

import com.hongte.alms.base.entity.RepaymentBizPlan;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 业务还款计划主表 DTO
 */
public class RepaymentBizPlanDto {

    /**
     * 业务还款计划
     */
    private RepaymentBizPlan repaymentBizPlan;


    /**
     * 业务还款计划列表DTO
     */
    private List<RepaymentBizPlanListDto>  bizPlanListDtos;


    /**
     * 标的还款计划主表DTO
     */
    private  List<RepaymentProjPlanDto> projPlanDtos;



    public RepaymentBizPlan getRepaymentBizPlan() {
        return repaymentBizPlan;
    }

    public void setRepaymentBizPlan(RepaymentBizPlan repaymentBizPlan) {
        this.repaymentBizPlan = repaymentBizPlan;
    }

    public List<RepaymentBizPlanListDto> getBizPlanListDtos() {
        return bizPlanListDtos;
    }

    public void setBizPlanListDtos(List<RepaymentBizPlanListDto> bizPlanListDtos) {
        this.bizPlanListDtos = bizPlanListDtos;
    }

    public List<RepaymentProjPlanDto> getProjPlanDtos() {
        return projPlanDtos;
    }

    public void setProjPlanDtos(List<RepaymentProjPlanDto> projPlanDtos) {
        this.projPlanDtos = projPlanDtos;
    }
}
