package com.hongte.alms.scheduled.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: 陈泽圣
 * @Date: 2018/6/19
 */
@FeignClient(value = "alms-scheduled-service")
public interface WithholdingClient {

    @RequestMapping(value = "/repay/searchRepayResult",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    void searchRepayResult();



    @RequestMapping(value = "/repay/autoRepay",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    void autoRepay();
}
