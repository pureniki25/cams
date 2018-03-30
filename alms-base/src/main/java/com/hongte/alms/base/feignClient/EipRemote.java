package com.hongte.alms.base.feignClient;

import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.vo.comm.SmsVo;
import com.ht.ussp.core.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:喻尊龙
 * @date: 2018/3/12
 */
@FeignClient(value = "eip-out",url ="http://172.16.200.112:30406")
public interface EipRemote {

    @RequestMapping(value = "/eip/xiaodai/AddProjectTrack",headers = {"app=DH", "content-type=application/json"},method = RequestMethod.POST)
    Result addProjectTrack(@RequestBody AddProjectTrackReqDto dto);
    
	/**
	 * 调用外联平台发送短信
	 * 
	 * @param emailVo
	 * @return
	 */
    @RequestMapping(value = "/eip/common/sendSms", headers = { "app=ALMS", "content-type=application/json" },method = RequestMethod.POST)
	public Result sendSms(@RequestBody SmsVo smsVo);
}
