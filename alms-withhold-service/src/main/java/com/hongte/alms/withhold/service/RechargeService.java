package com.hongte.alms.withhold.service;

import java.util.Comparator;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.common.result.Result;

/**
 * 
 * @author czs
 * 代扣服务
 *
 */

public interface RechargeService {

    /**
     * 代扣入口
     * @author czs
     * @param businessId
     * @param afterId
     * @param bankCard
     *  @param amount 金额为空时，默认代扣每期应还金额
     *   @param platformId 代扣平台ID,ID为空是,默认客户银行卡号绑定的平台代扣
     * @return
     */
    Result recharge(String businessId, String afterId, String bankCard,String amount,String platformId);
    
    
    
    /**
     * 
     * 判断每期还款计划是否为最后一期
     * @param projPlanList
     * @return
     */
	boolean istLastPeriod(RepaymentBizPlanList pList);
	
	
	/**
	 * 根据身份证号码获取客户相关代扣信息
	 */
	 CustomerInfoDto getCustomerInfo(String identifyCard);
	 
	 
	
	
}

