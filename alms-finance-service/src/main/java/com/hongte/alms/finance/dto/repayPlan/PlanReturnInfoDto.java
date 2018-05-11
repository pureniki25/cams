package com.hongte.alms.finance.dto.repayPlan;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/11
 */
public class PlanReturnInfoDto {

//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private List<CarBusinessAfterDto> carBusinessAfterDtoList;

    private  List<RepaymentBizPlanDto> repaymentBizPlanDtos;

    public List<CarBusinessAfterDto> getCarBusinessAfterDtoList() {
        return carBusinessAfterDtoList;
    }

    public void setCarBusinessAfterDtoList(List<CarBusinessAfterDto> carBusinessAfterDtoList) {
        this.carBusinessAfterDtoList = carBusinessAfterDtoList;
    }

    public List<RepaymentBizPlanDto> getRepaymentBizPlanDtos() {
        return repaymentBizPlanDtos;
    }

    public void setRepaymentBizPlanDtos(List<RepaymentBizPlanDto> repaymentBizPlanDtos) {
        this.repaymentBizPlanDtos = repaymentBizPlanDtos;
    }
}
