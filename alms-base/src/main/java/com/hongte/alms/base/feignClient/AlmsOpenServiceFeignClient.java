package com.hongte.alms.base.feignClient;

import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 调用alms-open-service微服务的Feign客户端接口
 * Created by 张贵宏 on 2018/6/28 17:38
 */
@FeignClient("alms-open-service")
//@FeignClient(value = "alms-open-service", url="http://192.168.14.7:30616")
public interface AlmsOpenServiceFeignClient {

    /*****还款计划相关*******/
    @RequestMapping(value = "/RepayPlan/updateRepayPlanToLMS", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.POST)
    Result updateRepayPlanToLMS(@RequestBody Map<String, Object> paramMap);


    /*****其它相关1*******/


    /*****其它相关2*******/


    /*****其它相关3*******/


    /*****其它相关4*******/
}
