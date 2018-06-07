package com.hongte.alms.withhold.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.YiBaoRechargeReqDto;
import com.ht.ussp.core.Result;

/**
 * @author chenzesheng
 * @date 2018/5/24 
 * 代扣接口
 */


@FeignClient(value = "eip-out")
public interface EipOutRechargeRemote {
	
   
    
}
