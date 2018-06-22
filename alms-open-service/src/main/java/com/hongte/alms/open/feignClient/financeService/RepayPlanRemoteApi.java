package com.hongte.alms.open.feignClient.financeService;

import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.vo.RepayPlanReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 调用财务微服务的还款计划相关接口
 * Created by 张贵宏 on 2018/6/21 11:18
 */
@FeignClient(value = "alms-finance-service", url = "http://192.168.14.7:30621")
public interface RepayPlanRemoteApi {

    /**
     * 调用财务微服务的还款计划相关接口
     * 根据业务ID从财务微服务获取还款计划数据
     *
     * @param repayPlanReq 业务id
     * @return 包含还款计划数据的结果对象
     */
    @RequestMapping(value = "/RepayPlan/queryRepayPlanByBusinessId", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.POST)
    Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(RepayPlanReq repayPlanReq);
}
