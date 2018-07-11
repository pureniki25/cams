package com.hongte.alms.withhold.controller;

import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.service.SendMessageService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.hongte.alms.withhold.service.WithholdingService;
import com.hongte.alms.withhold.service.impl.WithholdingServiceimpl;

import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czs
 * 贷后短信和消息推送
 */
@RestController
@RequestMapping(value= "/sms")
public class SmsController {
	private static Logger logger = LoggerFactory.getLogger(SmsController.class);
    @Autowired
    private AfterLoanRepaymentService afterLoanRepaymentService;
    
    @Autowired
	@Qualifier("RechargeService")
    RechargeService rechargeService;
    
    
    @Autowired
  	@Qualifier("WithholdingService")
    WithholdingService withholdingService;
    
    @Autowired
  	@Qualifier("SendMessageService")
    SendMessageService sendMessageService;


 
    /**
     * 查询代扣结果
 
     */
    @GetMapping("/sendSms")
    @ApiOperation(value = "发送短信")
    public void searchRepayResult(){
    	rechargeService.getReturnResult();
    }


}
