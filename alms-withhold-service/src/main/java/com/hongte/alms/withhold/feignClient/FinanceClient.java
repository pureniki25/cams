package com.hongte.alms.withhold.feignClient;

import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("alms-finance-service")
public interface FinanceClient {

    @RequestMapping(value = "/finance/shareProfit",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    public Result shareProfit(@RequestParam("businessId") String businessId, @RequestParam("afterId") String afterId,@RequestParam("logId") Integer logId);
}
