package com.hongte.alms.withhold.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.ht.ussp.core.Result;

/**
 * 贷后还款
 */
@RestController
@RequestMapping(value= "/Test")
public class TestController {

    @Autowired  
    private RedisService redisService;  
    
    @Autowired
    EipRemote eipRemote;
    
	@Autowired
	@Qualifier("AgencyRechargeLogService")
	AgencyRechargeLogService agencyRechargeLogService;
	
	@Autowired
	@Qualifier("RechargeService")
	RechargeService rechargeService;
	
	
	@Value("${tuandai_pay_cm_orderno}")
	private String oidPartner;
	
	@Value("${tuandai_org_username}")
	private String orgUserName;
	
	@Value("${tuandai_org_userid}")
	private String rechargeUserId;
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
    public Result recharge(){
    	
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
		System.out.println("=================商户号："+merchOrderId+"=====================");
    	BankRechargeReqDto dto=new BankRechargeReqDto();
    	dto.setOidPartner(oidPartner);
    	dto.setRechargeUserId(rechargeUserId);
    	dto.setOrgUserName(orgUserName);
    	dto.setAmount(Double.valueOf(100));
    	dto.setChannelType("102");
    	dto.setUserIP("127.0.0.1");
    	dto.setCmOrderNo(merchOrderId);
    	Result result=eipRemote.bankRecharge(dto);
    	System.out.println(result);
		return result;
    }
    
    /**
     * 银行代扣查询订单
  
     */
    @GetMapping("/searchStatus")
    public String searchStatus(@RequestParam String cmOrderNo) {
    	
    	agencyRechargeLogService.queryRechargeOrder(oidPartner, cmOrderNo);
		return "ok";
    }
    /**
     * 宝付代扣Test
  
     */
    @GetMapping("/bfcharge")
    public Result bfcharge(){ 
    	
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
		dto.setBizType("0000");
		dto.setPayCode("ABOC");
		dto.setPayCm("1");
		dto.setAccNo("6217000140014876690");
		dto.setIdCardType("01");
		dto.setIdCard("132440197104205314");
		dto.setIdHolder("李伟");
		dto.setMobile("13071354045");
		dto.setTransId(merchOrderId);
		dto.setTxnAmt(Double.valueOf(500));
		dto.setTransSerialNo(merchOrderId);
		dto.setTradeDate(String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))));
    	
    	Result result=eipRemote.baofuRecharge(dto);
		return result;
    }
    /**
     * 查询平台还款日期
  
     */
    @GetMapping("/queryProjectRepayment")
    public String queryProjectRepayment(){
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", "86fb6418-bf0a-40ab-b3b2-8c5fa7eb86fa");
		paramMap.put("type", "1");
		Result result = eipRemote.queryProjectRepayment(paramMap);
		HashMap<String,HashMap<String,String>> map=(HashMap) result.getData();
		List<HashMap<String,String>> list=(List<HashMap<String, String>>) map.get("RepaymentList");
		String dueDate="";
		for(int i=0;i<list.size();i++) {
			String periods=String.valueOf(list.get(i).get("Periods"));
			if(periods.equals("1"))
			{
				dueDate= list.get(i).get("CycDate").toString();
			}
		}
		return dueDate;
    }

    /**
     * 查询垫付本息
  
     */
    @GetMapping("/advanceShareProfit")
    public Result advanceShareProfit(){
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", "86fb6418-bf0a-40ab-b3b2-8c5fa7eb86fa");
	
		Result result = eipRemote.advanceShareProfit(paramMap);
		HashMap<String,HashMap<String,String>> map=(HashMap) result.getData();
		List<HashMap<String,String>> list=(List<HashMap<String, String>>) map.get("returnAdvanceShareProfits");
		String dueDate="";
		for(int i=0;i<list.size();i++) {
			String periods=String.valueOf(list.get(i).get("period"));
			if(periods.equals("1"))
			{
				dueDate= list.get(i).get("principalAndInterest").toString();
			}
		}
		return result;
    }
}
