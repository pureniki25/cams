package com.hongte.alms.open.feignClient.financeService;

import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 调用财务微服务的还款计划相关接口
 * Created by 张贵宏 on 2018/6/21 11:18
 */
@FeignClient(value = "alms-finance-service")
public interface RepayPlanRemoteApi {

    /**
     * 调用财务微服务的还款计划相关接口
     * 根据业务ID从财务微服务获取还款计划数据
     *
     * @param businessId 业务id
     * @return 包含还款计划数据的结果对象
     */
    @PostMapping(value = "/RepayPlan/queryRepayPlanByBusinessId", headers = {"app-ALMS", "content-type=application/json"})
    Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(String businessId);
}
