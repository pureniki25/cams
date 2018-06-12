package com.hongte.alms.scheduled.client;


import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: 曾坤
 * @Date: 2018/5/21
 */
@FeignClient(value = "alms-scheduled-service")
public interface WithholdingClient {

    @RequestMapping(value = "/repay/searchRepayResult",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    void searchRepayResult();




}
