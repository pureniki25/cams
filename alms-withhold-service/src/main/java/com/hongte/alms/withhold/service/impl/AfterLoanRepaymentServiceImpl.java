package com.hongte.alms.withhold.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.feignClient.WithHoldingClient;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
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
            if(repaymentBizPlanList.getSrcType()!=null && repaymentBizPlanList.getSrcType()==1){
                return withHoldingClient.repayAssignBank(businessId,afterId,bankCard);
            }else {
                return null;
            }
        }
        Result result=new Result();
        result.setCode("500");
        result.setMsg("找不到到对应的业务单号！");
        return result;
    }
}
