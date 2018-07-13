package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import lombok.Data;

import java.util.List;

@Data
public class RepaymentSettlePlanListDetailDto {
    private RepaymentBizPlanListDetail repaymentBizPlanListDetail;

    private List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails;
}
