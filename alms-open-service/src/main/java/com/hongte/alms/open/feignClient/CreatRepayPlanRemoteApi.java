package com.hongte.alms.open.feignClient;



import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.dto.repayPlan.PlanReturnInfoDto;
import com.hongte.alms.open.req.repayPlan.CreatRepayPlanReq;
import com.hongte.alms.open.req.repayPlan.trial.TrailRepayPlanReq;
import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 * 创建还款计划的远程调用
 */
@FeignClient(value = "alms-finance-service")
public interface CreatRepayPlanRemoteApi {

    @RequestMapping(value = "/alms/finance/RepayPlan/creatRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    @RequestMapping(value = "/alms/finance/RepayPlan/creatAndSaveRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq);

    @RequestMapping(value = "/alms/finance/RepayPlan/creatAndSaveRepayPlan",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq);

}
