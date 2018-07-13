package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import lombok.Data;

import java.util.List;

@Data
public class RepaymentSettlePlanDto {
    private RepaymentBizPlan repaymentBizPlan;

    private List<RepaymentProjPlan> repaymentProjPlans;

}
