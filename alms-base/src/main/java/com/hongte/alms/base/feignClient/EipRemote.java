package com.hongte.alms.base.feignClient;

import com.hongte.alms.base.dto.RechargeModalDTO;
import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.vo.comm.SmsVo;
import com.ht.ussp.core.Result;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:喻尊龙
 * @date: 2018/3/12
 */
@FeignClient(value = "eip-out")
public interface EipRemote {

    @RequestMapping(value = "/eip/xiaodai/AddProjectTrack",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
    Result addProjectTrack(@RequestBody AddProjectTrackReqDto dto);
    
	/**
	 * 调用外联平台发送短信
	 * 
	 * @param emailVo
	 * @return
	 */
    @RequestMapping(value = "/eip/common/sendSms", headers = { "app=ALMS", "content-type=application/json" },method = RequestMethod.POST)
	public Result sendSms(@RequestBody SmsVo smsVo);
    
    /**
     * 查询代充值账户余额
     * 
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/eip/xiaodai/QueryUserAviMoney", headers = { "app=ALMS", "content-type=application/json" },method = RequestMethod.POST)
    public Result queryUserAviMoney(@RequestBody Map<String, Object> paramMap);
    
    /**
     * 代充值
     * 
     * @param dto
     * @return
     */
    @RequestMapping(value = "/eip/td/assetside/agencyRecharge", headers = { "app=ALMS", "content-type=application/json" },method = RequestMethod.POST)
    public Result agencyRecharge(@RequestBody RechargeModalDTO dto);
    
    /**
     * 7.1.29	查询充值订单
     * 
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/eip/td/assetside/queryRechargeOrder", headers = { "app=ALMS", "content-type=application/json" },method = RequestMethod.POST)
    public Result queryRechargeOrder(@RequestBody Map<String, Object> paramMap);
}
