package com.hongte.alms.withhold.controller;

import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.SmsService;
import com.hongte.alms.withhold.service.WithholdingService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
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
  	@Qualifier("SmsService")
    SmsService smsService;
 
    /**
     * 发送逾期提醒(逾期的第1~3天)
     * 单笔还款，还款日前7天/1天提醒
     */
    @GetMapping("/sendSms")
    @ApiOperation(value = "发送短信")
    public void searchRepayResult(){
    	//******************逾期提醒(逾期的第1~3天)*****************//
//    	smsService.sendOverDueRemindMsg(1);
//    	smsService.sendOverDueRemindMsg(2);
//    	smsService.sendOverDueRemindMsg(3);
//    	//******************单笔还款，还款日前7天/1天提醒*****************//
//    	smsService.sendOneRemindMsg(1); 
//     	smsService.sendOneRemindMsg(7);
//    	//******************多笔还款，还款日前7天/1天提醒*****************//
//     	smsService.sendMutiplyRemindMsg(7);
//     	smsService.sendMutiplyRemindMsg(1);
//      	//******************单笔结清，结清日前15天/1天提醒*****************//
     	smsService.sendOneSettleRemindMsg(15);
//     	smsService.sendOneSettleRemindMsg(1);


    	
    }


}
