package com.hongte.alms.withhold.controller;

import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.hongte.alms.withhold.service.WithholdingService;

import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 贷后还款
 */
@RestController
@RequestMapping(value= "/repay")
public class AfterLoanRepaymentController {

    @Autowired
    private AfterLoanRepaymentService afterLoanRepaymentService;
    
    @Autowired
	@Qualifier("RechargeService")
    RechargeService rechargeService;
    
    
    @Autowired
  	@Qualifier("WithholdingService")
    WithholdingService withholdingService;


    /**
     * 执行代扣
     * @param businessId 业务编号
     * @param afterId 期数
     * @param bankCard 银行卡号
     * @return 是否代扣成功
     */
    @PostMapping("/submitAutoRepay")
    @ApiOperation(value = "执行代扣")
    public Result submitAutoRepay(String businessId,String afterId,String bankCard){
        return afterLoanRepaymentService.submitAutoRepay(businessId,afterId,bankCard);
    }
    
    /**
     * 手动代扣
     * @param businessId 业务编号
     * @param afterId 期数
     * @param bankCard 银行卡号
     * @return 是否代扣成功
     */
    @PostMapping("/handRepay")
    @ApiOperation(value = "手动代扣")
    public Result handRepay(@RequestBody DeductionVo deuctionVo){
    	if(deuctionVo.getPlatformId()==PlatformEnum.YH_FORM.getValue()) {
    		Result result=withholdingService.handBankRecharge(deuctionVo.getBusiness(), deuctionVo.getBankCardInfo(), deuctionVo.getpList(), BigDecimal.valueOf(deuctionVo.getTotal()));
    	    return result;
    	}else {
    		Result result=withholdingService.handThirdRepaymentCharge(deuctionVo.getBusiness(), deuctionVo.getBankCardInfo(), deuctionVo.getpList(), deuctionVo.getPlatformId(),deuctionVo.getRepayAmount());
    		 return result;
    	}
    
    }
    
    
    
    /**
     * 自动代扣
     */
    @GetMapping("/autoRepay")
    @ApiOperation(value = "自动代扣")
    public Result autoRepay(){
	    try {
	    	withholdingService.withholding();
	        return Result.success();
	    }catch (Exception ex){
	        ex.printStackTrace();
	        return Result.error("500", "自动代扣异常:"+ ex.getMessage());
	    }
    }
    
    /**
     * 查询代扣结果
 
     */
    @GetMapping("/searchRepayResult")
    @ApiOperation(value = "查询代扣结果")
    public void searchRepayResult(){
    	rechargeService.getReturnResult();
    }


}
