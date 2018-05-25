package com.hongte.alms.withhold.service;

import com.hongte.alms.common.result.Result;

public interface AfterLoanRepaymentService {

    /**
     * 执行代扣
     * @param businessId
     * @param afterId
     * @param bankCard
     * @return
     */
    Result submitAutoRepay(String businessId, String afterId, String bankCard);
}

