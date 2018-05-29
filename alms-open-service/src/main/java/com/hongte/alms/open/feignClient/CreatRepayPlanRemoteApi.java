package com.hongte.alms.open.feignClient;



import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.vo.RepayPlanReq;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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

    @RequestMapping(value = "/RepayPlan/trailRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq);
    
    @RequestMapping(value = "/RepayPlan/queryRepayPlanByBusinessId",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(RepayPlanReq req);
    
    @RequestMapping(value = "/RepayPlan/deleteRepayPlanByConditions",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> deleteRepayPlanByConditions(RepayPlanReq req);


//    @RequestMapping(value = "/RepayPlan/testTime",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
//    Result<PlanReturnInfoDto> testTime() ;

    @RequestMapping(value = "/RepayPlan/getRepayList",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<List<BizDto>> getRepayList(@RequestParam(value = "businessIds") List<String> businessIds) ;

    @RequestMapping(value = "/RepayPlan/getLogBill",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<BizDto> getLogBill(@RequestParam(value = "businessId") String businessId);



    @RequestMapping(value = "/RepayPlan/getBizPlanBill",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
     Result<BizPlanDto> getBizPlanBill(@RequestParam(value = "planId") String planId);



    }
