package com.hongte.alms.scheduled.client;

import com.hongte.alms.common.result.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zengkun
 * @since 2018/3/8
 */
@FeignClient("alms-core-service")
public interface SysUserClient {

    @GetMapping("/SysUser/setUserPermissions")
    public Result setUserPermissions(
            @Param("userId") String userId);

}
