package com.hongte.alms.finance.service;

import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.finance.dto.repayPlan.PlanReturnInfoDto;

/**
 * @author zengkun
 * @since 2018/4/24
 */
public interface CreatRepayPlanService {

    /**
     * 根据传入的创建还款计划相关信息创建还款计划
     * @param creatRepayPlanReq
     * @return
     */
    public PlanReturnInfoDto creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq) throws InstantiationException, IllegalAccessException;


    public  PlanReturnInfoDto creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq) throws IllegalAccessException, InstantiationException;

    /**
     * 根据 业务编号 查询还款计划
     * @param businessId
     * @return
     */
    PlanReturnInfoDto queryRepayPlanByBusinessId(String businessId);
    
    /**
     * 根据条件删除还款计划
     * @param businessId
     * @param repaymentBatchId
     */
    void deleteRepayPlanByConditions(String businessId, String repaymentBatchId);
}
