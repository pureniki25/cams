package com.hongte.alms.withhold.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.withhold.Data;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.feignClient.WithHoldingClient;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.WithholdingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;
    /**
     * 执行代扣
     * @param businessId 业务单号
     * @param afterId 期数
     * @param bankCard 银行卡号
     * @return 返回代扣结果
     */
    @Override
    public Result submitAutoRepay(String businessId, String afterId, String bankCard) {
    	RepaymentBizPlanList repaymentBizPlanList=repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",businessId).eq("after_id",afterId));
    	if(repaymentBizPlanList!=null){
    		if(repaymentBizPlanList.getCurrentStatus()!=null && repaymentBizPlanList.getCurrentStatus().equals("已还款")) {
    			Result result=new Result();
                result.setCode("0000");
                result.setMsg("该期已还款不可进行代扣！");
                return result;
    		}
    		//判断是否贷后代扣
            if(repaymentBizPlanList.getSrcType()==null || repaymentBizPlanList.getSrcType().intValue()==1){
                Result result = withHoldingClient.repayAssignBank(repaymentBizPlanList.getOrigBusinessId(),afterId,bankCard);
                Object object = JSON.parse(result.getData().toString());
                Result resultObj=new Result();
                resultObj.setCode("0");
                resultObj.setMsg(result.getMsg());
                resultObj.setData(object);
                return resultObj;
            }else {
                 Result result=withholdingService.appWithholding(repaymentBizPlanList);
                
                 Data data=new Data();
                 if(result.getCode().equals("1")) {//成功
                	 data.setType(1);
                	 data.setMsg(result.getMsg());
                 }else if(result.getCode().equals("2")) {//
                	 data.setType(2);
                	 data.setMsg(result.getMsg());
                 }else {
                	 data.setType(3);
                	 data.setMsg(result.getMsg());
                 }
                 Object object = JSON.parse(data.toString());
                 Result resultObj=new Result();
                 resultObj.setCode("0");
                 resultObj.setMsg("执行成功");
                 resultObj.setData(object);
                 
                 return resultObj;
            }
        }else {
            Result result=new Result();
            result.setCode("-1");
            result.setMsg("找不到对应的业务单号！");
            return result;
       }
    }
}
