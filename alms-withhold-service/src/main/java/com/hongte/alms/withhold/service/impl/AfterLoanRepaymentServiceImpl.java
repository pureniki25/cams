package com.hongte.alms.withhold.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.feignClient.WithHoldingClient;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.WithholdingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("AfterLoanRepaymentService")
public class AfterLoanRepaymentServiceImpl implements AfterLoanRepaymentService {

    @Autowired
    private WithHoldingClient withHoldingClient;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("WithholdingService")
    private WithholdingService withholdingService;

    @Autowired
    private RechargeService rechargeService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    CustomerInfoXindaiRemoteApi customerInfoXindaiRemoteApi;
    /**
     * 执行代扣
     * @param businessId 业务单号
     * @param afterId 期数
     * @param bankCard 银行卡号
     * @return
     */
    @Override
    public Result submitAutoRepay(String businessId, String afterId, String bankCard) {
    	RepaymentBizPlanList repaymentBizPlanList=repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",businessId).eq("after_id",afterId));
    	if(repaymentBizPlanList!=null){
    		//判断是否走贷后代扣
            if(repaymentBizPlanList.getSrcType()==null || repaymentBizPlanList.getSrcType().intValue()==1){
                return withHoldingClient.repayAssignBank(repaymentBizPlanList.getOrigBusinessId(),afterId,bankCard);
            }else {
                 withholdingService.appWithholding(repaymentBizPlanList);
                 Result result=new Result();
                 result.setCode("0000");
                 result.setMsg("执行成功");
                 return result;
            }
        }else {
            Result result=new Result();
            result.setCode("500");
            result.setMsg("找不到对应的业务单号！");
            return result;
       }
    }
}
