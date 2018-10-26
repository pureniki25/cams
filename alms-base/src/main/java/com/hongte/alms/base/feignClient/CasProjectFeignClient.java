package com.hongte.alms.base.feignClient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ht.ussp.core.Result;


/**
 * 资金平台feignClient
 * @author huweiqian
 *
 */
@FeignClient("cas-project")
public interface CasProjectFeignClient {

    /*****还款计划相关*******/
    @RequestMapping(value = "/project/getProjectInfoList", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.POST)
    Result getProjectInfoList(@RequestBody List<String> projectIds);
    
//    @RequestMapping(value = "/project/getProjectInfoList", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.GET)
//    Result projectStatus(@RequestParam String projectId);

}
