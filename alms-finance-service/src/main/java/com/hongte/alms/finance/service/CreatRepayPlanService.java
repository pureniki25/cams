package com.hongte.alms.finance.service;

import com.hongte.alms.finance.dto.repayPlan.RepaymentBizPlanDto;
import com.hongte.alms.finance.req.repayPlan.CreatRepayPlanReq;

import java.util.List;

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
    public List<RepaymentBizPlanDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq);


    public  List<RepaymentBizPlanDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq) throws IllegalAccessException, InstantiationException;


}
