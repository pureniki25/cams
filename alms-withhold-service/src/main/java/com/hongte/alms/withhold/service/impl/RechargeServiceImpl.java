package com.hongte.alms.withhold.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.withhold.service.RechargeService;
import java.util.Comparator;
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
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;
    
    @Autowired
    @Qualifier("withholdingChannelService")
    WithholdingChannelService withholdingChannelService;

	@Override
	public Result recharge(String businessId, String afterId, String bankCard,String amount,String platformId) {
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

   
}
