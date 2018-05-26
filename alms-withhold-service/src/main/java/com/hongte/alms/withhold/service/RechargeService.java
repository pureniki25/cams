package com.hongte.alms.withhold.service;

import com.hongte.alms.common.result.Result;

public interface RechargeService {

    /**
     * 代扣入口
     * @author czs
     * @param businessId
     * @param afterId
     * @param bankCard
     * @return
     */
    Result recharge(String businessId, String afterId, String bankCard);
}

