package com.hongte.alms.base.RepayPlan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentSettleProjDto {

    private String userName;//借款人
    private int isMast;// 0共借标 1 主借标
    private BigDecimal projMoney;//上标金额

    private String projPlanId;
    private String businessId;
    private String planId;
}
