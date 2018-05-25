package com.hongte.alms.withhold.controller;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.withhold.service.AfterLoanRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result SubmitAutoRepay(String businessId,String afterId,String bankCard){
        return afterLoanRepaymentService.submitAutoRepay(businessId,afterId,bankCard);
    }
}
