package com.hongte.alms.base.feignClient;


import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.common.result.Result;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenzesheng
 * @date 2018/6/10 17:37
 * 贷后跟踪相关的远程调用
 */
//@FeignClient(value = "alms-open-service")
@FeignClient(value = "alms-open-service")
public interface CustomerInfoXindaiRemoteApi {

    /**
     * 根据身份证号码获取客户银行卡信息
     */
    @RequestMapping(value = "/CustomerBankcardController/getBankcardInfo",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.GET)
    List<BankCardInfo> getBankcardInfo(@RequestParam("identityCard") String identityCard) throws Exception;


    }
