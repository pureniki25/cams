package com.hongte.alms.finance.dto.repayPlan;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/11
 */
public class PlanReturnInfoDto {

//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    //信贷还款计划信息
    private List<XdPlanDto> xdPlanDtos;

    //贷后还款计划列表信息
    private  List<RepaymentBizPlanDto> repaymentBizPlanDtos;


    public List<RepaymentBizPlanDto> getRepaymentBizPlanDtos() {
        return repaymentBizPlanDtos;
    }

    public void setRepaymentBizPlanDtos(List<RepaymentBizPlanDto> repaymentBizPlanDtos) {
        this.repaymentBizPlanDtos = repaymentBizPlanDtos;
    }

    public List<XdPlanDto> getXdPlanDtos() {
        return xdPlanDtos;
    }

    public void setXdPlanDtos(List<XdPlanDto> xdPlanDtos) {
        this.xdPlanDtos = xdPlanDtos;
    }
}
