package com.hongte.alms.open.feignClient;



import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.dto.repayPlan.PlanReturnInfoDto;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 * 创建还款计划的远程调用
 */
@FeignClient(value = "alms-finance-service")
public interface CreatRepayPlanRemoteApi {

    @RequestMapping(value = "/RepayPlan/creatRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    @RequestMapping(value = "/RepayPlan/creatAndSaveRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    @RequestMapping(value = "/RepayPlan/creatAndSaveRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq);
    
    @RequestMapping(value = "/RepayPlan/queryRepayPlanByBusinessId",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(String businessId);
    
    @RequestMapping(value = "/RepayPlan/deleteRepayPlanByConditions",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    Result<PlanReturnInfoDto> deleteRepayPlanByConditions(@RequestParam(value = "businessId")String businessId, @RequestParam(value = "repaymentBatchId")String repaymentBatchId);

}
