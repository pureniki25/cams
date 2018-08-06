package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentBizPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author zengkun
 * @since 2018/5/5
 * 业务还款计划主表 DTO
 */
public class RepaymentBizPlanDto  implements Serializable {


	/**
     * 业务还款计划
     */
    protected RepaymentBizPlan repaymentBizPlan;


    /**
     * 业务还款计划列表DTO
     */
    protected List<RepaymentBizPlanListDto>  bizPlanListDtos;


    /**
     * 标的还款计划主表DTO
     */
    protected  List<RepaymentProjPlanDto> projPlanDtos;



    public RepaymentBizPlan getRepaymentBizPlan() {
        return repaymentBizPlan;
    }

    public void setRepaymentBizPlan(RepaymentBizPlan repaymentBizPlan) {
        this.repaymentBizPlan = repaymentBizPlan;
    }

    public List<RepaymentBizPlanListDto> getBizPlanListDtos() {
        return bizPlanListDtos;
    }

    public void setBizPlanListDtos(List<RepaymentBizPlanListDto> bizPlanListDtos) {
        this.bizPlanListDtos = bizPlanListDtos;
    }

    public List<RepaymentProjPlanDto> getProjPlanDtos() {
        return projPlanDtos;
    }

    public void setProjPlanDtos(List<RepaymentProjPlanDto> projPlanDtos) {
        this.projPlanDtos=projPlanDtos;
    }
    

}
