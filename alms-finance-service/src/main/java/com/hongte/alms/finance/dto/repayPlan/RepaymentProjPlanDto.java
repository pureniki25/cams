package com.hongte.alms.finance.dto.repayPlan;

import com.hongte.alms.base.entity.RepaymentProjPlan;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划主表
 */
public class RepaymentProjPlanDto {

    private RepaymentProjPlan repaymentProjPlan;


    private List<RepaymentProjPlanListDto>  projPlanListDtos;


    public RepaymentProjPlan getRepaymentProjPlan() {
        return repaymentProjPlan;
    }

    public void setRepaymentProjPlan(RepaymentProjPlan repaymentProjPlan) {
        this.repaymentProjPlan = repaymentProjPlan;
    }

    public List<RepaymentProjPlanListDto> getProjPlanListDtos() {
        return projPlanListDtos;
    }

    public void setProjPlanListDtos(List<RepaymentProjPlanListDto> projPlanListDtos) {
        this.projPlanListDtos = projPlanListDtos;
    }


}
