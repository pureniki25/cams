package com.hongte.alms.scheduled.client;

import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 * @author zengkun
 * @since 2018/7/12
 * 贷后跟踪相关的远程调用
 */
//@FeignClient(value = "alms-core-service", url="http://192.168.12.101:30606")
@FeignClient(value = "alms-core-service")
public interface CollectionRemoteApi {

    /**
     * 同步一个电催设置
     *
     * @param carBusinessAfter
     * @return
     */
    @RequestMapping(value = "/alms/transferOnePhoneSet", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.POST)
    Result transferOnePhoneSet(CarBusinessAfter carBusinessAfter);
}