package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import lombok.Data;

import java.util.List;

@Data
public class RepaymentSettlePlanListDto {
    private RepaymentBizPlanList repaymentBizPlanList;

    private List<RepaymentProjPlanList> repaymentProjPlanLists;
}
