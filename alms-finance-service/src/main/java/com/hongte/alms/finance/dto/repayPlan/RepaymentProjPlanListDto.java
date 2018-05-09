package com.hongte.alms.finance.dto.repayPlan;

import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划List表Dto
 */
public class RepaymentProjPlanListDto {

    private RepaymentProjPlanList repaymentProjPlanList;

    private List<RepaymentProjPlanListDetail> projPlanListDetails;

    public RepaymentProjPlanList getRepaymentProjPlanList() {
        return repaymentProjPlanList;
    }

    public void setRepaymentProjPlanList(RepaymentProjPlanList repaymentProjPlanList) {
        this.repaymentProjPlanList = repaymentProjPlanList;
    }

    public List<RepaymentProjPlanListDetail> getProjPlanListDetails() {
        return projPlanListDetails;
    }

    public void setProjPlanListDetails(List<RepaymentProjPlanListDetail> projPlanListDetails) {
        this.projPlanListDetails = projPlanListDetails;
    }
}
