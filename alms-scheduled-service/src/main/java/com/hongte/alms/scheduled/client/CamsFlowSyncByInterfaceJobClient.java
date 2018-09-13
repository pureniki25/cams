package com.hongte.alms.scheduled.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ht.ussp.core.Result;

/**
 * @ClassName: SyncDaihouClient  
 * @Description: 贷后数据同步
 * @author liuzq  
 * @date 2018年7月9日    
 */
@FeignClient("alms-finance-service")
public interface CamsFlowSyncByInterfaceJobClient {
    @RequestMapping(value = "/camsFlowSync/addBatchFlow",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> addBatchFlow();
    
    @RequestMapping(value = "/camsFlowSync/addBatchFenFaFlow",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> addBatchFenFaFlow();
    
    @RequestMapping(value = "/camsFlowSync/cancelRepayFlow",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> cancelRepayFlow();
    
    @RequestMapping(value = "/camsFlowSync/cancelFenFaFlow",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> cancelFenFaFlow();
}
