package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import lombok.Data;

@Data
public class RepaymentSettleSortDto {

    private RepaymentProjPlan repaymentProjPlan;
    /**
     * 标的信息
     */
    private TuandaiProjectInfo tuandaiProjectInfo ;
}
