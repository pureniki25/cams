package com.hongte.alms.scheduled.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

/**
 * @ClassName: SyncDaihouClient  
 * @Description: 贷后数据同步
 * @author liuzq  
 * @date 2018年7月9日    
 */
@FeignClient("alms-finance-service")
public interface CamsFlowSyncByInterfaceJobClient {
    @RequestMapping(value = "/camsFlowSync/addBatchFlow",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    public Result<Integer> addBatchFlow();
}
