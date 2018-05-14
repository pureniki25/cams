package com.hongte.alms.open.dto.repayPlan;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 业务还款计划列表DTO
 */
public class RepaymentBizPlanListDto {

    /**
     * 还款计划列表
     */
    private  RepaymentBizPlanList repaymentBizPlanList;


    /**
     * 还款计划列表详情列表
     */
    private List<RepaymentBizPlanListDetail>  bizPlanListDetails;


    public RepaymentBizPlanList getRepaymentBizPlanList() {
        return repaymentBizPlanList;
    }

    public void setRepaymentBizPlanList(RepaymentBizPlanList repaymentBizPlanList) {
        this.repaymentBizPlanList = repaymentBizPlanList;
    }

    public List<RepaymentBizPlanListDetail> getBizPlanListDetails() {
        return bizPlanListDetails;
    }

    public void setBizPlanListDetails(List<RepaymentBizPlanListDetail> bizPlanListDetails) {
        this.bizPlanListDetails = bizPlanListDetails;
    }
}
