package com.hongte.alms.withhold.service;

import java.math.BigDecimal;
import java.util.List;

import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.common.result.Result;

/**
 * @author czs
 * @since 2018/5/24
 */
public interface WithholdingService {



    /**
     * 自动代扣操作
     */
    void withholding();
    
    /**
     * 手工银行代扣
     * @param handRepayAmount  手工代扣金额
     * 
     */
    void handBankRecharge(BasicBusiness basic, BankCardInfo bankCardInfo, RepaymentBizPlanList pList,
			List<BankCardInfo> bankCardInfos,BigDecimal handRepayMoney);
    
    /**
     * 手工第三方代扣
     * 
     * @param underAmount 线上逾期费用
     * @param handRepayAmount  手工代扣金额
     * @param platformId 第三方平台ID
     */
    void handThirdRepaymentCharge(BasicBusiness basic, BankCardInfo thirtyCardInfo, RepaymentBizPlanList pList,
			BigDecimal underAmount,BigDecimal handRepayAmount,Integer platformId); 
    
    
    
    
    

}
