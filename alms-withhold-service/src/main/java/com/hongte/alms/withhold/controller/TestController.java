package com.hongte.alms.withhold.controller;


import java.net.InetAddress;
import java.net.UnknownHostException;
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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.YiBaoRechargeReqDto;
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
	
	
//	@Value("${tuandai_pay_cm_orderno}")
	private String oidPartner;
	
//	@Value("${tuandai_org_username}")
	private String orgUserName;
	
//	@Value("${tuandai_org_userid}")
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
    	
//    	agencyRechargeLogService.queryRechargeOrder(oidPartner, cmOrderNo,null);
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
		dto.setPayCode("BOC");
		dto.setPayCm("1");
		dto.setAccNo("6217857000026618551");
		dto.setIdCardType("01");
		dto.setIdCard("430681199205087616");
		dto.setIdHolder("何灿");
		dto.setMobile("13071354045");
		dto.setTransId(merchOrderId);
		dto.setTxnAmt((int)(500));
		dto.setTransSerialNo(merchOrderId);
		dto.setTradeDate(String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))));
    	
    	Result result=eipRemote.baofuRecharge(dto);
		return result;
    }
    
    
    
    /**
     * 宝付查询
  
     */
    @GetMapping("/queryBaofuStatus")
    public Result queryBaofuStatus(){ 
    	
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("transSerialNo", "201806072039444377997");
		paramMap.put("origTransId", "201806072039444377997");
		paramMap.put("tradeDate", "20180607203944");
		Result result = eipRemote.queryBaofuStatus(paramMap);
		return result;

    }
    /**
     * 查询平台还款日期
  
     */
    @GetMapping("/queryProjectRepayment")
    public String queryProjectRepayment(){
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", "157ed1b5-38b0-4d61-95ab-3c308a55f7c5");
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
    
    
    
    /**
     * 易宝绑卡
  
     */
    @GetMapping("/invokeBindBankCard")
    public Result invokeBindBankCard(){
   	    String merchOrderId=MerchOrderUtil.getMerchOrderId();
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantaccount", "10000419568");
		paramMap.put("identityid", "15999706761");
		paramMap.put("identitytype",5);
		paramMap.put("requestid", merchOrderId);
		paramMap.put("cardno", "6222803230501010140");
		paramMap.put("idcardtype", "01");
		paramMap.put("idcardno", "441900199009181315");
		paramMap.put("username", "陈泽圣");
		paramMap.put("phone", "15999706761");
		paramMap.put("userip", "127.0.0.1");
		Result result = eipRemote.invokeBindBankCard(paramMap);
	
		return result;
    }
    
    
    
    
    /**
     * 易宝确定绑卡
  
     */
    @GetMapping("/confirmBindBankCard")
    public Result confirmBindBankCard(){
    	Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantaccount", "10000419568");
		paramMap.put("requestid", "201806081617237045153");
		paramMap.put("validatecode","536021");
		Result result = eipRemote.confirmBindBankCard(paramMap);
		return result;
    }
    
    
    /**
     * 易宝代扣
  
     */
    @GetMapping("/directBindPay")
    public Result directBindPay(){
    	YiBaoRechargeReqDto dto=new YiBaoRechargeReqDto();
    	   String merchOrderId=MerchOrderUtil.getMerchOrderId();
    	dto.setMerchantaccount("10000419568");
    	dto.setOrderid(merchOrderId);
    	dto.setTranstime((int) (System.currentTimeMillis()));
    	dto.setAmount((int)1);
    	dto.setProductname("123");
    	dto.setIdentityid("201806081558011369520");
    	dto.setIdentitytype("5");
    	dto.setCard_top("622280");
    	dto.setCard_last("0140");
    	dto.setCallbackurl("");
    	dto.setUserip("172.0.0.1");
		Result result = eipRemote.directBindPay(dto);
		if(result.getData()!=null) {
				String dataJson = JSONObject.toJSONString(result.getData());
				Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
				String yborderid = (String) resultMap.get("yborderid");
				if(yborderid!=null) {
					System.out.println(true);
				}else {
					System.out.println(false);
				}
			}else {
				result.getCodeDesc();
			}
		return result;
    }
    
    
    /**
     * 易宝状态查询
  
     */
    @GetMapping("/queryOrder")
    public Result queryOrder(){
    	YiBaoRechargeReqDto dto=new YiBaoRechargeReqDto();
      	Map<String, Object> paramMap = new HashMap<>();
    	   String merchOrderId=MerchOrderUtil.getMerchOrderId();
    		paramMap.put("merchantaccount", "10000419568");
    		paramMap.put("orderid", "201806081719364804731");
    		
    		paramMap.put("identitytype", "5");
		Result result = eipRemote.queryOrder(paramMap);
		
		return result;
    }
    public static void main(String[] args) throws UnknownHostException {
    	System.out.println(InetAddress.getLocalHost());
	}
}
