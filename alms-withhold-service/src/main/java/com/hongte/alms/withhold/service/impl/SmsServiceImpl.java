package com.hongte.alms.withhold.service.impl;


import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBizCustomer;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.service.BasicBizCustomerService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SendMessageService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.service.SmsService;
import com.ht.ussp.util.DateUtil;

@Service("SmsService")
public class SmsServiceImpl implements SmsService{
    private Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;
	
    @Autowired
  	@Qualifier("SendMessageService")
    SendMessageService sendMessageService;
    
    
    @Autowired
  	@Qualifier("BasicBizCustomerService")
    BasicBizCustomerService basicBizCustomerService;
    
    @Autowired
  	@Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
  	@Qualifier("TuandaiProjectInfoService")
    TuandaiProjectInfoService tuandaiProjectInfoService;
    
    @Autowired
    CustomerInfoXindaiRemoteApi customerInfoXindaiRemoteApi;


	@Override
	public void sendOverDueRemindMsg(Integer days) {
		   days=0-days;
    	   Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
    	  List<RepaymentBizPlanList> overDueLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","逾期").eq("due_date", dueDate).eq("src_type", 2));
    	  for(RepaymentBizPlanList pList:overDueLists) {
    		  if("e1571786-9a9b-44bd-a6aa-b09cbc9b4c92".equals(pList.getPlanId())) {
    			  System.out.println("stop");
    		  }
    		  List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
    		  BasicBizCustomer customer=basicBizCustomerService.selectOne(new EntityWrapper<BasicBizCustomer>().eq("business_id", pList.getBusinessId()).eq("ismain_customer", 1));
    			if(customer==null) {
    				logger.error("找不到该客户信息:business_id{0}:"+pList.getBusinessId());
    				continue;
    			}
    			logger.info("发送逾期提醒短信开始====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
    		  sendMessageService.sendAfterOverdueRemindSms(customer.getPhoneNumber(), customer.getCustomerName(), pList.getPeriod(), pLists.size());
    			logger.info("发送逾期提醒短信结束====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
    	  }
		
	}

	
	
	/**
	 *     
     * 单笔还款，还款日前7天/1天提醒
     * 针对单个还款计划
     */
	 

	@Override
	public void sendOneRemindMsg(Integer days) {
		  Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
		  List<RepaymentBizPlanList> remindLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","还款中").eq("due_date", dueDate).eq("src_type", 2));
		  
		   //筛选只有1个还款计划的记录
			for(Iterator<RepaymentBizPlanList> it = remindLists.iterator();it.hasNext();) {
				RepaymentBizPlanList pList=it.next();
				List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id", pList.getOrigBusinessId()));
				if(plans.size()>1) {
					it.remove();
				}
			}
		  
		  for(RepaymentBizPlanList pList:remindLists) {
    		  RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));
    		  BasicBizCustomer customer=basicBizCustomerService.selectOne(new EntityWrapper<BasicBizCustomer>().eq("business_id", pList.getBusinessId()).eq("ismain_customer", 1));
    		  BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getBusinessId()));
    			TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", plan.getBusinessId()));
    			Date borrowDate=null;
    			
    			if(customer==null) {
    				logger.error("找不到该客户信息:business_id{0}:"+plan.getBusinessId());
    				continue;
    			}
    			if(tuandaiProjectInfo!=null) {
    				if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
	    				borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
	    			}else {
	    				borrowDate=tuandaiProjectInfo.getCreateTime();
	    			}
    			}else {
    				logger.error("发送单笔还款提醒时,找不到借款日期:business_id{0}:"+plan.getBusinessId());
    				continue;
    			}
    			Boolean isBindding=false;
    			Result result=null;
    			try {
					 result=customerInfoXindaiRemoteApi.getBankcardInfo(business.getCustomerIdentifyCard());
				} catch (Exception e) {
					logger.error("获取客户银行卡信息出错"+e);
				}
    			if(result!=null&&result.getCode().equals("1")) {
    				List<BankCardInfo> bankCardInfos=null;
    	        	BankCardInfo bankCardInfo=null;
    				 bankCardInfos=JSON.parseArray(result.getData().toString(), BankCardInfo.class);
 					if(bankCardInfos!=null&&bankCardInfos.size()>0) {
 	        			bankCardInfo=bankCardInfos.get(0);
 	        		}
 					logger.info("发送单笔还款提醒短信开始====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
    				//绑卡
    		 		sendMessageService.sendAfterBindingRepayRemindSms(customer.getPhoneNumber(), customer.getCustomerName(),borrowDate,plan.getBorrowMoney(), pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount()),pList.getPeriod(), pList.getDueDate(),bankCardInfo.getBankCardNumber().substring(bankCardInfo.getBankCardNumber().length()-4, bankCardInfo.getBankCardNumber().length()));
    			}else {
    				//未绑卡
    		 		sendMessageService.sendAfterUnbondRepayRemindSms(customer.getPhoneNumber(), customer.getCustomerName(),borrowDate,plan.getBorrowMoney(), pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount()),pList.getPeriod(), pList.getDueDate());
    			
    			}
    			logger.info("发送单笔还款提醒短信结束====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
   	  }
	}

	/**
	 *     
     * 多笔还款，还款日前7天/1天提醒
     * 针对多个还款计划
     */
	 

	@Override
	public void sendMutiplyRemindMsg(Integer days) {
		   Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
		   List<RepaymentBizPlanList>  remindLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","还款中").eq("due_date", dueDate).groupBy("orig_business_id").orderBy("due_date",false));
    	
		   //筛选多个还款计划的记录
				for(Iterator<RepaymentBizPlanList> it = remindLists.iterator();it.hasNext();) {
					RepaymentBizPlanList pList=it.next();
					List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id", pList.getOrigBusinessId()));
					if(plans.size()==1) {
						it.remove();
					}
				}
			  for(RepaymentBizPlanList pList:remindLists) {
	    		  List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", pList.getBusinessId()).eq("current_status","还款中").eq("due_date", dueDate));
	    		  RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));
	    		  BasicBizCustomer customer=basicBizCustomerService.selectOne(new EntityWrapper<BasicBizCustomer>().eq("business_id", pList.getBusinessId()).eq("ismain_customer", 1));
	    		  BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getBusinessId()));
	    			TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", plan.getBusinessId()));
	    			Date borrowDate=null;
	    			if(customer==null) {
	    				logger.error("找不到该客户信息:business_id{0}:"+plan.getBusinessId());
	    				continue;
	    			}
	    			if(tuandaiProjectInfo!=null) {
	    				if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
		    				borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
		    			}else {
		    				borrowDate=tuandaiProjectInfo.getCreateTime();
		    			}
	    			}else {
	    				logger.error("发送多笔还款提醒时,找不到借款日期:business_id{0}:"+plan.getBusinessId());
	    				continue;
	    			}
	    			Boolean isBindding=false;
	    			Result result=null;
	    			try {
						 result=customerInfoXindaiRemoteApi.getBankcardInfo(business.getCustomerIdentifyCard());
					} catch (Exception e) {
						logger.error("获取客户银行卡信息出错"+e);
					}
	    			if(result!=null&&result.getCode().equals("1")) {
	    				List<BankCardInfo> bankCardInfos=null;
	    	        	BankCardInfo bankCardInfo=null;
	    				 bankCardInfos=JSON.parseArray(result.getData().toString(), BankCardInfo.class);
	 					if(bankCardInfos!=null&&bankCardInfos.size()>0) {
	 	        			bankCardInfo=bankCardInfos.get(0);
	 	        		}
	 					logger.info("发送多笔还款提醒短信开始====================:business_id{0}:",plan.getBusinessId());
	    				//绑卡
	    		 		sendMessageService.sendAfterBindingMutipleRepayRemindSms(bankCardInfo.getMobilePhone(), bankCardInfo.getBankCardName(), pLists, bankCardInfo.getBankCardNumber().substring(bankCardInfo.getBankCardNumber().length()-4, bankCardInfo.getBankCardNumber().length()));
	    			}else {
	    				//未绑卡
	    		 		sendMessageService.sendAfterUnbondMutipleRepayRemindSms(customer.getPhoneNumber(), customer.getCustomerName(), pLists);;
	    	    		
	    			}
	    			logger.info("发送多笔还款提醒短信结束====================:business_id{0}:",plan.getBusinessId());
	   	  }
			
			
	}
	
	public boolean istLastPeriod(RepaymentBizPlanList pList) {
		boolean isLast=false;
		List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
		RepaymentBizPlanList lastpList=pLists.stream().max(new Comparator<RepaymentBizPlanList>() {
			@Override
			public int compare(RepaymentBizPlanList o1, RepaymentBizPlanList o2) {
				return o1.getDueDate().compareTo(o2.getDueDate());
			}
		}).get();
		
		if(pList.getPlanListId().equals(lastpList.getPlanListId())) {
			isLast=true;
		}
		return isLast;
	}



	
	
	
	/**
	 *     
     * 单笔结清，结清日前15天/1天提醒
     * 针对单个还款计划
     */
	@Override
	public void sendOneSettleRemindMsg(Integer days) {
		  Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
		  List<RepaymentBizPlanList> remindLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","还款中").eq("due_date", dueDate).eq("src_type", 2));
		  
		   //筛选只有1个还款计划的记录且是最后一期
			for(Iterator<RepaymentBizPlanList> it = remindLists.iterator();it.hasNext();) {
				RepaymentBizPlanList pList=it.next();
				if(pList.getPlanListId().equals("ceeba869-8a55-477c-84be-15eacdecce01")) {
					System.out.println("stop");
				}
				List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id", pList.getOrigBusinessId()));
				if(plans.size()>1||(plans.size()==1&&!istLastPeriod(pList))) {
					it.remove();
				}
		
			}
		  
		  for(RepaymentBizPlanList pList:remindLists) {
    		  List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
    		  RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));
    		  BasicBizCustomer customer=basicBizCustomerService.selectOne(new EntityWrapper<BasicBizCustomer>().eq("business_id", pList.getBusinessId()).eq("ismain_customer", 1));
    		  BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getBusinessId()));
    			TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", plan.getBusinessId()));
    			Date borrowDate=null;
    			if(customer==null) {
    				logger.error("找不到该客户信息:business_id{0}:"+plan.getBusinessId());
    				continue;
    			}
    			if(tuandaiProjectInfo!=null) {
    				if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
	    				borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
	    			}else {
	    				borrowDate=tuandaiProjectInfo.getCreateTime();
	    			}
    			}else {
    				logger.error("发送单笔结清提醒时,找不到借款日期:business_id{0}:"+plan.getBusinessId());
    				continue;
    			}
    			Boolean isBindding=false;
    			Result result=null;
    			try {
					 result=customerInfoXindaiRemoteApi.getBankcardInfo(business.getCustomerIdentifyCard());
				} catch (Exception e) {
					logger.error("获取客户银行卡信息出错"+e);
				}
    			if(result!=null&&result.getCode().equals("1")) {
    				List<BankCardInfo> bankCardInfos=null;
    	        	BankCardInfo bankCardInfo=null;
    				 bankCardInfos=JSON.parseArray(result.getData().toString(), BankCardInfo.class);
 					if(bankCardInfos!=null&&bankCardInfos.size()>0) {
 	        			bankCardInfo=bankCardInfos.get(0);
 	        		}
 					logger.info("发送单笔结清提醒短信开始====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
    				//绑卡
    		 		sendMessageService.sendAfterBindingSettleRemindSms(customer.getPhoneNumber(), customer.getCustomerName(),borrowDate,plan.getBorrowMoney(), pList.getDueDate(),bankCardInfo.getBankCardNumber().substring(bankCardInfo.getBankCardNumber().length()-4, bankCardInfo.getBankCardNumber().length()));
    			}else {
    				//未绑卡
    		 		sendMessageService.sendAfterUnbondSettleRemindSms(customer.getPhoneNumber(), customer.getCustomerName(),borrowDate,plan.getBorrowMoney(), pList.getDueDate());
    	    		
    			}
    			logger.info("发送单笔结清提醒短信开始====================:business_id{0},pListId{1}:",pList.getBusinessId(),pList.getPlanListId());
   	  }
		
	}



	


  
}
