package com.hongte.alms.base.feignClient;


import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.common.result.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 * 贷后跟踪相关的远程调用
 */
//@FeignClient(value = "alms-open-service")
@FeignClient(value = "alms-open-service")
public interface CollectionSynceToXindaiRemoteApi {

    /**
     * 同步一个电催设置到信贷
     * @param carBusinessAfter
     * @return
     */
    @RequestMapping(value = "/collection/transferOnePhoneSetToXd",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result transferOnePhoneSetToXd(CarBusinessAfter carBusinessAfter);

    /**
     * 同步一个催收设置到信贷
     * @param collection
     * @return
     */
    @RequestMapping(value = "/collection/transferOneVisitSetToXd",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result transferOneVisitSetToXd(Collection collection);

    /**
     * 同步一个贷后跟踪记录到信贷
     * @param parametertracelog
     * @return
     */
    @RequestMapping(value = "/collectionTrackLog/transferOneCollectionLogToXd",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result<Integer> transferOneCollectionLogToXd(@RequestBody Parametertracelog parametertracelog);


    /**
     * 根据信贷Id删除信贷的贷后跟踪记录
     */
    @RequestMapping(value = "/collectionTrackLog/deleteXdCollectionLogById",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result deleteXdCollectionLogById(@RequestBody Integer xdIndex) throws Exception;


    }
