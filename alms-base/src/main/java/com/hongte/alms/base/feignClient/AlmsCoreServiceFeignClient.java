package com.hongte.alms.base.feignClient;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.common.result.Result;

@FeignClient("alms-core-service")
public interface AlmsCoreServiceFeignClient {
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/transferOfLitigation/sendTransferLitigation", headers = {"app=ALMS", "content-type=application/json"}, method = RequestMethod.POST)
    Result sendTransferLitigation(@RequestBody Map<String, Object> paramMap);
}
