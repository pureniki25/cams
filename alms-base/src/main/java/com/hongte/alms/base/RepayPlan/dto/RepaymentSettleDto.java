package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import lombok.Data;

import java.util.List;

@Data
/**
 * 结清数据存储Dto
 */
public class RepaymentSettleDto {

    private List<RepaymentBizPlan> repaymentBizPlans;


    private  List<RepaymentSettlePlanDto> repaymentSettlePlanDtos;


}
