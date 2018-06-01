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
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
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
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("RechargeService")
public class RechargeServiceImpl implements RechargeService {
	
	
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
    private RedisService redisService;  
    

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;
    

	@Override
	public Result recharge(String businessId, String afterId, String bankCard,Double amount,String platformId,String merchOrderId) {
		Result result=new Result();
		BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		CustomerInfoDto dto=getCustomerInfo(business.getCustomerIdentifyCard());
		  //执行之前先检查一下同一个渠道，当前的失败或者执行中的日志有没超过对应渠道的失败次数，超过则不执行
        //同一个渠道，同一天，最多失败或者执行中运行2次
		Integer failCount=0;
		Integer maxFailCount=3;//渠道最大失败次数
		if(!StringUtil.isEmpty(platformId)) {
			List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectRepaymentLogForAutoRepay(businessId, afterId, platformId);
			failCount=logs.size();
		}else {
			if(!StringUtil.isEmpty(dto.getPlatformId())) {
				platformId=dto.getPlatformId();
				List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectRepaymentLogForAutoRepay(businessId, afterId, dto.getPlatformId());
				failCount=logs.size();
			}else {
				//同一天不同渠道一共失败或处理中的次数
				List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectRepaymentLogForAutoRepay(businessId, afterId, null);
				failCount=logs.size();
			}
		}
	
		
		if(!StringUtil.isEmpty(platformId)) {
			WithholdingChannel chanel=withholdingChannelService.selectOne(new EntityWrapper<WithholdingChannel>().eq("platform_id", platformId));
			maxFailCount=chanel.getFailTimes();
		}
		if(failCount>=maxFailCount) {
			result.setData(null);
			result.setCode("-1");
			result.setMsg("当前失败或者执行中次数为:" + failCount + ",超过限制次数，不允许执行。");
		}else {
			if(platformId.equals(PlatformEnum.YB_FORM.getValue().toString())) {
				//eipOutRechargeService.platfromRecharge(dto);
			}
			if(platformId.equals(PlatformEnum.BF_FORM.getValue().toString())) {
				//eipOutRechargeService.platfromRecharge(dto);
			}
            if(platformId.equals(PlatformEnum.YH_FORM.getValue().toString())) {
            	//eipOutRechargeService.bankRecharge(dto);
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

	
	public void recordRepaymentLog(Result result,RepaymentBizPlanList list,BasicBusiness business,CustomerInfoDto dto,Integer platformId,Integer boolLastRepay,Integer boolPartRepay,String merchOrderId,Integer settlementType,BigDecimal currentAmount) {
		WithholdingRepaymentLog log=new WithholdingRepaymentLog();
		log.setAfterId(list.getAfterId());
		log.setBankCard(dto.getBankBindCardNo());
		log.setBindPlatformId(platformId);
		log.setBoolLastRepay(boolLastRepay);
		log.setBoolPartRepay(boolPartRepay);
		log.setCreateTime(new Date());
		log.setCreateUser(loginUserInfoHelper.getUserId());
		log.setIdentityCard(business.getCustomerIdentifyCard());
		log.setMerchOrderId(merchOrderId);
		log.setOriginalBusinessId(business.getBusinessId());
		log.setPhoneNumber(dto.getPhone());
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
	public boolean EnsureAutoPayIsEnabled(RepaymentBizPlanList pList) {
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
        
//        if (next?.borrow_date != null && next.borrow_date.Value.Date.AddDays(-1) < dtfact_date.Date)
//        {
//            msg = "下一期还款时间开始停止自动代扣上一期";
//            return false;
//        }
		return true;
	}
   
}
