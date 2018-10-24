package com.hongte.alms.withhold.controller;

import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.feignClient.FinanceClient;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.hongte.alms.withhold.service.WithholdingService;
import com.hongte.alms.withhold.service.impl.WithholdingServiceimpl;

import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger logger = LoggerFactory.getLogger(AfterLoanRepaymentController.class);
    @Autowired
    private AfterLoanRepaymentService afterLoanRepaymentService;
    
    @Autowired
	@Qualifier("RechargeService")
    RechargeService rechargeService;
    
    
    @Autowired
  	@Qualifier("WithholdingService")
    WithholdingService withholdingService;

    @Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService;

    
    @Autowired
	FinanceClient financeClient;
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
    		Result result=withholdingService.handBankRecharge(deuctionVo.getBusiness(), deuctionVo.getBankCardInfo(), deuctionVo.getpList(), deuctionVo.getRepayAmount());
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
	  		logger.info("@autoRepay@自动代扣--开始");
	    	withholdingService.withholding();
			logger.info("@autoRepay@自动代扣--结束");
	        return Result.success();
	    }catch (Exception ex){
	        ex.printStackTrace();
	        return Result.error("500", "自动代扣异常:"+ ex.getMessage());
	    }
    }
    
    /**
     * 自动检查代扣成功了没有核销的记录
     */
    @GetMapping("/autoCheckCancle")
    @ApiOperation(value = "自动检查核销")
    public Result autoCheckCancle(){
	    try {
	  		logger.info("@autoRepay@自动检查核销--开始");
	  		List<WithholdingRepaymentLog> logs=repaymentBizPlanListService.searchNoCancelList();
	  		for(WithholdingRepaymentLog log:logs) {
	  			try {
	  				logger.info("@shareProfit@自动检查核销-feign---开始[{}{}{}]", log.getOriginalBusinessId(),log.getAfterId(),log.getLogId());
	  			    financeClient.shareProfit(log.getOriginalBusinessId(), log.getAfterId(), log.getLogId());
	  				logger.info("@shareProfit@自动检查核销-feign--结束[{}{}{}]", log.getOriginalBusinessId(),log.getAfterId(),log.getLogId());
	  			}catch (Exception e) {
	  				logger.error("自动检查核销异常"+e);
	  			}
	  		}
	    	logger.info("@autoRepay@自动检查核销--结束");
	        return Result.success();
	    }catch (Exception ex){
	        ex.printStackTrace();
	        return Result.error("500", "自动检查核销:"+ ex.getMessage());
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
