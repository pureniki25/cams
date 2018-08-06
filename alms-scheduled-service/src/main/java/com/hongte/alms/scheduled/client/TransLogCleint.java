package com.hongte.alms.scheduled.client;


import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: 曾坤
 * @Date: 2018/5/21
 */
@FeignClient(value = "alms-core-service")
public interface TransLogCleint {

    @RequestMapping(value = "/collectionTrackLog/transfer",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    Result transferCollectionTransfer();

    @RequestMapping(value = "/alms/transfer",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    Result transferCollection();

}
