package com.hongte.alms.withhold.controller;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import com.hongte.alms.withhold.service.RedisService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    


}
