package com.hongte.alms.tool.importOffLineBusiness;



import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 * 创建还款计划的远程调用
 */
//@FeignClient(value = "alms-finance-service",url="http://localhost:30621" ,qualifier="api")
@FeignClient(value = "alms-finance-service",url="http://47.106.190.190:30621" ,qualifier="api")
//@FeignClient(value = "alms-finance-service",url="http://172.16.200.112:30621" ,qualifier="api")
//@FeignClient(value = "alms-finance-service" ,url="http://192.168.12.101:30621")
public interface CreatRepayPlanRemoteApi {

    @RequestMapping(value = "/RepayPlan/creatRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    @RequestMapping(value = "/RepayPlan/creatAndSaveRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    }
