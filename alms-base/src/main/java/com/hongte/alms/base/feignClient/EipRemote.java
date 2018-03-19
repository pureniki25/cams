package com.hongte.alms.base.feignClient;

import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.ht.ussp.core.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:喻尊龙
 * @date: 2018/3/12
 */
@FeignClient(value = "eip-out")
public interface EipRemote {

    @RequestMapping(value = "/eip/xiaodai/AddProjectTrack",headers = {"app=DH", "content-type=application/json"},method = RequestMethod.POST)
    Result addProjectTrack(@RequestBody AddProjectTrackReqDto dto);
}
