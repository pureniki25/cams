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
    
    //获取平台还款及垫付流水到贷后
    @RequestMapping(value = "/camsFlowSync/pullPlatformRepayInfo",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> pullPlatformRepayInfo();    
    
    //获取还垫付流水到贷后
    @RequestMapping(value = "/camsFlowSync/pullAdvanceRepayInfo",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> pullAdvanceRepayInfo();    
    
    //平台还款流水
    @RequestMapping(value = "/camsFlowSync/pushPlatformRepayFlowToCams",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> pushPlatformRepayFlowToCams();
    
    //平台垫付流水
    @RequestMapping(value = "/camsFlowSync/pushAdvancePayFlowToCams",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> pushAdvancePayFlowToCams();
    
    //平台还垫付流水
    @RequestMapping(value = "/camsFlowSync/pushAdvanceRepayFlowToCams",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    public Result<Object> pushAdvanceRepayFlowToCams();

    //你我金融流水
    @RequestMapping(value = "/camsFlowSync/pushNiWoRepayFlowToCams",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
	public Result<Object> pushNiWoRepayFlowToCams();
    
}
