package com.hongte.alms.withhold.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.feignClient.dto.AutoRechargeReqDto;
import com.ht.ussp.core.Result;

/**
 * @author chenzesheng
 * @date 2018/5/24 
 * 代扣接口
 */


@FeignClient(value = "eip-out")
public interface AutoRechargeService {
	
     
    @RequestMapping(value = "/eip/td/assetside/autoRecharge",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    String autoRecharge(@RequestBody AutoRechargeReqDto dto);
}
