package com.hongte.alms.withhold.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.feignClient.dto.ThirdPlatform;
import com.hongte.alms.base.feignClient.dto.YiBaoRechargeReqDto;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.withhold.feignClient.EipOutRechargeRemote;
import com.hongte.alms.withhold.feignClient.FinanceClient;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("RechargeService")
public class RechargeServiceImpl implements RechargeService {
	private static Logger logger = LoggerFactory.getLogger(RechargeServiceImpl.class);
	
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;
    

    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;
    
    @Autowired
    @Qualifier("withholdingChannelService")
    WithholdingChannelService withholdingChannelService;
    
    @Autowired
    @Qualifier("BizOutputRecordService")
    BizOutputRecordService bizOutputRecordService;
    
    @Autowired
    EipOutRechargeRemote eipOutRechargeRemote;
    
    @Autowired
    FinanceClient financeClient;
    
    @Autowired  
    private RedisService redisService;  
    

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;
    

	@Override
	public Result recharge(String businessId, String afterId, String bankCard,Double amount,Integer platformId,String merchOrderId,BankCardInfo info) {
	
		BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));

		  //执行之前先检查一下同一个渠道，当前的失败或者执行中的日志有没超过对应渠道的失败次数，超过则不执行
        //同一个渠道，同一天，最多失败或者执行中运行2次
		Integer failCount=0;
		if(platformId!=null&&info!=null) {
			List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectRepaymentLogForAutoRepay(businessId, afterId, platformId);
			failCount=logs.size();
			Result result=	excuteEipRemote(platformId, failCount,amount, info,merchOrderId);
			return result;
		}else {
//			CustomerInfoDto dto=getCustomerInfo(business.getCustomerIdentifyCard());
//			List<BankCardInfo> bankCardInfos= dto.getList();
//			BankCardInfo bankCardInfo=getBankCardInfo(bankCardInfos);
//			BankCardInfo ThirtyCardInfo=getThirtyPlatformInfo(bankCardInfos);
//			List<Integer> platformIds=new ArrayList();
//			
//			if (bankCardInfo!=null) {
//				//银行代扣
//				platformIds.add(5);
//				
//			
//			}else if(ThirtyCardInfo!=null&&bankCardInfo==null){// 第三方代扣
//				List<ThirdPlatform> thirdPlatforms=ThirtyCardInfo.getThirdPlatformList();
//				for(ThirdPlatform form:thirdPlatforms) {
//					platformIds.add(form.getPlatformID());
//				}
//			}else{
//				logger.debug("业务编号为" + businessId+ "期数为"+afterId+"代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
//			} 
//			Result result=null;
//			for(Integer formId:platformIds) {
//				 result=excuteEipRemote(platformId, failCount,amount,info,merchOrderId);
//				if(!result.getCode().equals("1")) {
//					continue;
//				}else {
//					return result;
//				}
//			}
//			return result;
		}
		return null;
	
	
	}

	private Result  excuteEipRemote(Integer platformId,Integer failCount,Double amount,BankCardInfo info,String merchOrderId) {
		Result result=new Result();

		Integer maxFailCount=3;//渠道最大失败次数
		if(platformId!=null) {
			WithholdingChannel chanel=withholdingChannelService.selectOne(new EntityWrapper<WithholdingChannel>().eq("platform_id", platformId));
			maxFailCount=chanel.getFailTimes();
		}
		if(failCount>=maxFailCount) {
			result.setData(null);
			result.setCode("-1");
			result.setMsg("当前失败或者执行中次数为:" + failCount + ",超过限制次数，不允许执行。");
		}else {
			if(platformId==PlatformEnum.YB_FORM.getValue()) {
				YiBaoRechargeReqDto dto=new YiBaoRechargeReqDto();
				dto.setMerchantaccount(merchOrderId);
				dto.setOrderid(merchOrderId);
				dto.setTranstime(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
				dto.setAmount(amount);
				dto.setProductname("");
				dto.setIdentityid(info.getIdentityNo());
				dto.setIdentitytype("01");
				dto.setCard_top(info.getBankCardNumber().substring(0, 6));
				dto.setCard_last(info.getBankCardNumber().substring(info.getBankCardNumber().length()-4, info.getBankCardNumber().length()));
				dto.setCallbackurl("172.0.0.1");
				dto.setUserip("172.0.0.1");
				eipOutRechargeRemote.yibaoRecharge(dto);
			}
			if(platformId==PlatformEnum.BF_FORM.getValue()) {
				BaofuRechargeReqDto dto=new BaofuRechargeReqDto();
				dto.setPayCode(info.getBankCode());
				dto.setPayCm("2");
				dto.setAccNo(info.getBankCardNumber());
				dto.setIdCardType("01");
				dto.setIdHolder(info.getBankCardName());
				dto.setMobile(info.getMobilePhone());
				dto.setTransId(merchOrderId);
				dto.setTxnAmt(amount);
				dto.setTradeDate(String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))));
				dto.setTransSerialNo(merchOrderId);
				eipOutRechargeRemote.baofuRecharge(dto);
			}
            if(platformId==PlatformEnum.YH_FORM.getValue()) {
            	BankRechargeReqDto dto=new BankRechargeReqDto();
            	dto.setAmount(amount);
            	dto.setChannelType("102");//todo需要循环子渠道
            	dto.setRechargeUserId(info.getPlatformUserID());
            	dto.setCmOrderNo(merchOrderId);
            	eipOutRechargeRemote.bankRecharge(dto);
			}
			
		}
		return result;
		
	}
	@Override
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

	@Override
	public CustomerInfoDto getCustomerInfo(String identifyCard) {
		CustomerInfoDto dto=new CustomerInfoDto();
		//todo 调信贷接口
		return dto;
	}

	@Override
	public String getMerchOrderId() {
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
		return merchOrderId;
	}

	
	public void recordRepaymentLog(Result result,RepaymentBizPlanList list,BasicBusiness business,BankCardInfo dto,Integer platformId,Integer boolLastRepay,Integer boolPartRepay,String merchOrderId,Integer settlementType,BigDecimal currentAmount) {
		WithholdingRepaymentLog log=new WithholdingRepaymentLog();
		log.setAfterId(list.getAfterId());
		log.setBankCard(dto.getBankCardNumber());
		log.setBindPlatformId(platformId);
		log.setBoolLastRepay(boolLastRepay);
		log.setBoolPartRepay(boolPartRepay);
		log.setCreateTime(new Date());
		log.setCreateUser(loginUserInfoHelper.getUserId());
		log.setIdentityCard(business.getCustomerIdentifyCard());
		log.setMerchOrderId(merchOrderId);
		log.setOriginalBusinessId(business.getBusinessId());
		log.setPhoneNumber(dto.getMobilePhone());
		log.setRepayStatus(Integer.valueOf(result.getCode()));
		log.setRemark(result.getMsg());
		log.setThirdOrderId(merchOrderId);
		log.setSettlementType(settlementType);
		log.setPlanTotalRepayMoney(list.getTotalBorrowAmount().add(list.getOverdueAmount()));
		log.setUpdateTime(new Date());
		log.setUpdateUser(loginUserInfoHelper.getUserId());
		log.setCurrentAmount(currentAmount);//本次代扣金额
		withholdingRepaymentLogService.insert(log);
	}

	@Override
	public BigDecimal getRestAmount(RepaymentBizPlanList list) {
		List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", list.getOrigBusinessId()).eq("after_id", list.getAfterId()).ne("repay_status", "失败"));
		BigDecimal hasRepayAmount=BigDecimal.valueOf(0);
		for(WithholdingRepaymentLog log:logs) {
			hasRepayAmount=hasRepayAmount.add(log.getCurrentAmount());
		}
		BigDecimal restAmount=list.getTotalBorrowAmount().add(list.getOverdueAmount()).subtract(hasRepayAmount);
		return restAmount;
	}

	@Override
	public BigDecimal getOnlineAmount(RepaymentBizPlanList list) {
		List<RepaymentBizPlanListDetail> details=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE));
	   BigDecimal underAmount=BigDecimal.valueOf(0);
		for(RepaymentBizPlanListDetail detail:details) {
			underAmount=underAmount.add(detail.getPlanAmount());
		}
		BigDecimal onlineAmount=list.getTotalBorrowAmount().add(list.getOverdueAmount()).subtract(underAmount);
		return onlineAmount;
	}

	@Override
	public BigDecimal getUnderlineAmount(RepaymentBizPlanList list) {
		List<RepaymentBizPlanListDetail> details=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE));
		   BigDecimal underAmount=BigDecimal.valueOf(0);
			for(RepaymentBizPlanListDetail detail:details) {
				underAmount=underAmount.add(detail.getPlanAmount());
			}
			return underAmount;
	}

	@Override
	public Integer getBankRepaySuccessCount(RepaymentBizPlanList list) {
		List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", list.getOrigBusinessId()).eq("after_id", list.getAfterId()).ne("repay_status", "成功"));
        
		return logs.size();
	}

	@Override
	public boolean EnsureAutoPayIsEnabled(RepaymentBizPlanList pList,Integer days) {
		BizOutputRecord record=bizOutputRecordService.selectOne(new EntityWrapper<BizOutputRecord>().eq("business_id", pList.getBusinessId()));
        if (DateUtil.getDiff(new Date(), pList.getDueDate())>30)
        {
            //msg = "超出三十天不自动代扣";
            return false;
        }
        if (DateUtil.getDiff(new Date(),record.getFactOutputDate())>0)
        
        {
            //msg = "借款日期大于当前日期";
            return false;
        }
        if (pList.getPeriod()==0)
        
        {
            //msg = "自动代扣取消展期00期代扣";
            return false;
        }
        
        if(istLastPeriod(pList)) {
        	//msg="最后一期不能自动代扣"
        	return false;
        }
        RepaymentBizPlanList next= repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", pList.getBusinessId()).eq("plan_id", pList.getPlanId()).eq("period", pList.getPeriod()+1));
        
        if (next!=null )
        {
	        Date nowDate=new Date();
	        days=0-days;
	        Date before30Days=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days,nowDate)));
	        if(next.getDueDate().compareTo(before30Days)>=0&&next.getDueDate().compareTo(nowDate)<=0) {
	        	   //msg = "下一期还款时间开始停止自动代扣上一期";
	            return false;
	        }
       
        }
		return true;
	}

	@Override
	public Result shareProfit(RepaymentBizPlanList list) {
		Result result=financeClient.shareProfit(list.getOrigBusinessId(), list.getAfterId());
		return result;
	}

	@Override
	public BankCardInfo getThirtyPlatformInfo(List<BankCardInfo> list) {
		for(BankCardInfo card:list) {
			if(card.getPlatformType()==0&&card.getWithholdingType()==1) {//等于第三方银行代扣并且是代扣主卡
				return card;
			}else {
				return null;
			}
	    }
		return null;
	}

	@Override
	public BankCardInfo getBankCardInfo(List<BankCardInfo> list) {
		for(BankCardInfo card:list) {
			if(card.getPlatformType()!=0&&card.getWithholdingType()==1) {//不等于第三方代扣银行卡并且是代扣主卡
				return card;
			}else {
				return null;
			}
	    }
		return null;
	}


   
}
