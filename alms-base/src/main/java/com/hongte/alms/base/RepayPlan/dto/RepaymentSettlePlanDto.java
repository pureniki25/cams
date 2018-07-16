package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RepaymentSettlePlanDto {
    private  String planId;
    private RepaymentBizPlan repaymentBizPlan;

    private List<RepaymentProjPlan> repaymentProjPlans;


//    private  List<RepaymentSettlePlanListDto> planLists=new ArrayList<>();

}
