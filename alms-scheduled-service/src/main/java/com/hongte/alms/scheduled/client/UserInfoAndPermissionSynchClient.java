package com.hongte.alms.scheduled.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;


/**
 * @ClassName: UserInfoAndPermissionSynchClient  
 * @Description: 定时同步用户权限
 * @author liuzq  
 * @date 2018年7月13日    
 */
@FeignClient("alms-core-service")
public interface UserInfoAndPermissionSynchClient {

    @RequestMapping(value = "/userInfoAndPermissionSynch/getUserInfoForApp",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    public Result<Integer> getUserInfoForApp();

}
