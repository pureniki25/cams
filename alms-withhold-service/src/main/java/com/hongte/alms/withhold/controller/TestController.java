package com.hongte.alms.withhold.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.withhold.feignClient.EipOutRechargeRemote;
import com.hongte.alms.withhold.service.RedisService;

/**
 * 贷后还款
 */
@RestController
@RequestMapping(value= "/Test")
public class TestController {

    @Autowired  
    private RedisService redisService;  
    
    @Autowired
    EipOutRechargeRemote eipOutRechargeRemote;
    
    
    /**
     * test
  
     */
    @GetMapping("/test")
    public String test(){
    	String merchOrderId="";
		while(true) {
			 merchOrderId=MerchOrderUtil.getMerchOrderId();
			if(redisService.hasKey(merchOrderId)==false) {
				redisService.set(merchOrderId, merchOrderId);
				break;
			}else {
				continue;
			}
		}
		System.out.println(merchOrderId);
		return merchOrderId;
    }
    
    /**
     * 银行代扣Test
  
     */
    @GetMapping("/recharge")
    public String recharge(){
    	
    	String merchOrderId="";
		while(true) {
			 merchOrderId=MerchOrderUtil.getMerchOrderId();
			if(redisService.hasKey(merchOrderId)==false) {
				redisService.set(merchOrderId, merchOrderId);
				break;
			}else {
				continue;
			}
		}
    	BankRechargeReqDto dto=new BankRechargeReqDto();
    	dto.setOidPartner("100");
    	dto.setRechargeUserId("test");
    	dto.setOrgUserName("test");
    	dto.setAmount(Double.valueOf(100));
    	dto.setChannelType("102");
    	dto.setRechargeUserId(merchOrderId);
    	dto.setUserIP("127.0.0.1");
    	String result=eipOutRechargeRemote.bankRecharge(dto);
		return result;
    }
    
    
    /**
     * 宝付代扣Test
  
     */
    @GetMapping("/bfcharge")
    public String bfcharge(){
    	
    	String merchOrderId="";
		while(true) {
			 merchOrderId=MerchOrderUtil.getMerchOrderId();
			if(redisService.hasKey(merchOrderId)==false) {
				redisService.set(merchOrderId, merchOrderId);
				break;
			}else {
				continue;
			}
		}
		BaofuRechargeReqDto dto=new BaofuRechargeReqDto();
		dto.setPayCode("13245");
		dto.setPayCm("2");
		dto.setAccNo("132456789");
		dto.setIdCardType("1");
		dto.setIdCard("441900199009181315");
		dto.setIdHolder("周杰伦");
		dto.setMobile("15999706761");
		dto.setTransId(merchOrderId);
		dto.setTxnAmt(Double.valueOf(500));
		dto.setTransSerialNo(merchOrderId);
    	
    	String result=eipOutRechargeRemote.baofuRecharge(dto);
		return result;
    }
    

}
