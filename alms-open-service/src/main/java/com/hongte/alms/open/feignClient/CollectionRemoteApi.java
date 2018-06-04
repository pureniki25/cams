package com.hongte.alms.open.feignClient;


import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.vo.RepayPlanReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 * 贷后跟踪相关的远程调用
 */
@FeignClient(value = "alms-core-service", url="http://192.168.14.163:30606")
//@FeignClient(value = "alms-core-service")
public interface CollectionRemoteApi {

    /**
     * 同步一个电催设置
     * @param carBusinessAfter
     * @return
     */
    @RequestMapping(value = "/alms/transferOnePhoneSet",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result transferOnePhoneSet( CarBusinessAfter carBusinessAfter);


    /**
     * 同步一个催收设置
     * @param collection
     * @return
     */
    @RequestMapping(value = "/alms/transOneCollectSet",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result transOneCollectSet(Collection collection);




    /**
     * 同步一个贷后跟踪记录
     * @param parametertracelog
     * @return
     */
    @RequestMapping(value = "/collectionTrackLog/transferOneCollectionLog",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result transferOneCollectionLog(@RequestBody Parametertracelog parametertracelog);

    /**
     * 根据信贷Id删除贷后跟踪记录
     * @param xdIndexId
     * @return
     */
    @RequestMapping(value = "/collectionTrackLog/deleteByxdId",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result deleteByxdId(@RequestBody Integer xdIndexId);






    }
