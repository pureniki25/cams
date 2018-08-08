package com.hongte.alms.withhold.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.SysExceptionLog;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;
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
     * @param amount 金额为null时，默认为代扣每期应还金额
     * @param platformId 代扣平台ID,ID为空是,默认客户银行卡号绑定的平台代扣
     * @param merchOrderId 生成商户订单号
     * @param bankCardInfo 为null时默认为手动代扣
     * @param appType ,代扣来源：1.app_run(APP代扣) 2.auto_run(自动代扣)
     * @return
     */
    Result recharge(BasicBusiness business, RepaymentBizPlanList pList,Double amount,Integer boolLastRepay,Integer boolPartRepay,BankCardInfo bankCardInfo,WithholdingChannel channel,String appType);
    
    

    /**
     * 
     * 判断每期还款计划是否为最后一期
     * @param pList
     * @return
     */
	boolean istLastPeriod(RepaymentBizPlanList pList);
	

    /**
     * 
     * 判断每期还款计划有没有代扣处理中的数据
     * @param pList
     * @return
     */
	boolean isRepaying(RepaymentBizPlanList pList);
	
	/**
	 * 判断是否在宽限期内线上已还款
	 */
	
	boolean isInForgiveDayRepay(RepaymentBizPlanList list);
	
	/**
	 * 判断是否在宽限期外，如果是，就可以自动代扣，否则不能
	 */
	
	boolean isForgiveDayOutside(RepaymentBizPlanList list);
	  /**
	   * 是否可以执行自动代扣
	   * @param days 周期天数
	   */
	
	 Result EnsureAutoPayIsEnabled(RepaymentBizPlanList pList,Integer days);
	
	
	/**
	 * 根据身份证号码获取客户相关代扣信息
	 */
	 List<BankCardInfo>  getCustomerInfo(String identifyCard);
	 
	 
	 
	 /**
	  * 生成代扣唯一商户号
	  */
	 String getMerchOrderId();
	 
	 
	/**
	 * 插入代扣记录
	 * boolLastRepay：表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
	 * boolPartRepay：表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。  0:非最后一笔代扣，1:最后一笔代扣
	 */
		
	 WithholdingRepaymentLog recordRepaymentLog(String resultMsg,Integer status,RepaymentBizPlanList list,BasicBusiness business,BankCardInfo dto,Integer platformId,Integer boolLastRepay,Integer boolPartRepay,String merchOrderId,String merchantAccount,Integer settlementType,BigDecimal currentAmount,String appType);
		
     /**
	  * 查询每期扣除处理中和成功代扣的金额，得出剩余未还金额
	  */
	 BigDecimal getRestAmount(RepaymentBizPlanList list);
	 
	 
     /**
	  * 查询银行代扣线上应还金额
	  */
	 BigDecimal getOnlineAmount(RepaymentBizPlanList list);
	 
	 
     /**
	 
	  * 查询银行代扣线下应还金额
	  */
	 BigDecimal getUnderlineAmount(RepaymentBizPlanList list);
	 
	 
	 
     /**
	  * 查询当前期银行代扣成功次数
	  */
	 Integer getBankRepaySuccessCount(RepaymentBizPlanList list);
	 
	 

     /**
	  * 分润接口
	  */
	 Result shareProfit(RepaymentBizPlanList list,WithholdingRepaymentLog log);
	 
		/**
		 * 获得第三方代扣银行卡信息
		 * @param List<BankCardInfo> list
		 * @return
		 */
	 BankCardInfo getThirtyPlatformInfo(List<BankCardInfo> list);
	 
		/**
		 * 获得银行代扣银行卡信息
		 * @param List<BankCardInfo> list
		 * @return
		 */
	 BankCardInfo getBankCardInfo(List<BankCardInfo> list);
	 
	 
		/**
		 * 查询回调结果
		 * @param List<BankCardInfo> list
		 * @return
		 */
	 void getReturnResult();
	 
	 
	 
	/**
	 * 银行代扣结果查询
	 */
	 void getBankResult(WithholdingRepaymentLog log,String oidPartner);
	 
	 
	 
	/**
	 * 宝付代扣结果查询
	 */
	 void getBFResult(WithholdingRepaymentLog log);
	 
	 
	 
	/**
	 * 易宝代扣结果查询
	 */
	 void getYBResult(WithholdingRepaymentLog log);
	 
	 
	 /**
		 * 获取该还款计划最早一期没有还的代扣 
		 */
	RepaymentBizPlanList getEarlyPeriod(RepaymentBizPlanList list);
	
	/**
	 * 记录异常日志
	 * @param dto
	 */
	
	 void RecordExceptionLog(String OriginalBusinessId,String afterId,String msg);
	 
	 
	 
	 
	 
	 

}

