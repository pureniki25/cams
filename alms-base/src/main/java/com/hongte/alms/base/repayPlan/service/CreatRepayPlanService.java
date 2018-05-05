package com.hongte.alms.base.repayPlan.service;

import com.hongte.alms.base.repayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.repayPlan.req.CreatRepayPlanReq;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/24
 */
public interface CreatRepayPlanService {

    /**
     * 根据传入的创建还款计划相关信息创建还款计划
     * 存储业务基本信息、出款计划、出款记录、出款费率、团贷上标信息
     * @param creatRepayPlanReq
     * @return
     */
    public List<RepaymentBizPlanDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq);


}
