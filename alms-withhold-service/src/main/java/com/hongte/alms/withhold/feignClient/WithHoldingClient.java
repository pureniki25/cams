package com.hongte.alms.withhold.feignClient;

import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("alms-open-service")
public interface WithHoldingClient {

    @PostMapping("/WithHoldingController/repayAssignBank")
    public Result repayAssignBank(@RequestParam("businessId") String businessId, @RequestParam("afterId") String afterId, @RequestParam("bankCard") String bankCard);
}
