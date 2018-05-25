package com.hongte.alms.base.RepayPlan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/11
 */
public class PlanReturnInfoDto  implements Serializable {

//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    //信贷还款计划信息
    private List<XdPlanDto> xdPlanDtos;

    //贷后还款计划列表信息
    private  List<RepaymentBizPlanDto> repaymentBizPlanDtos;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testTime;

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

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }
}
