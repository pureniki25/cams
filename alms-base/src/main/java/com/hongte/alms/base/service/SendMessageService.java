package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongte.alms.base.entity.RepaymentBizPlanList;

/**
 * <p>
 * 短信消息推送 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-07-10
 */
public interface SendMessageService {


     /**
      *  扣款成功短信通知(贷后
      *  @param phone 借款人电话号码
      * @param name 借款人名称
      * @param date 借款日期
      * @param borrowAmount 借款金额
      * @param period 还款期数
      * @param repayAmount 应还金额
      * @param factRepayAmount 实还金额
      */
	void sendAfterRepaySuccessSms(String phone,String name,Date date,BigDecimal borrowAmount,Integer period,BigDecimal repayAmount,BigDecimal factRepayAmount);
	
	
	


    /**
     *  扣款失败短信通知(贷后
     * @param name 借款人名称
     * @param date 借款日期
     * @param borrowAmount 借款金额
     * @param period 还款期数
     */
	void sendAfterRepayFailSms(String phone,String name,Date date,BigDecimal borrowAmount,Integer period);
	
	
	
	

     /**
      * 还款提醒 （未绑卡用户），单笔还款，还款日前7天/1天提醒
     *  @param phone 借款人电话号码
      * @param name 借款人名称
      * @param date 借款日期
      * @param borrowAmount 借款金额
      * @param period 还款期数
      * @param dueDate 还款日期
      * @param repayAmount 应还金额
      */
	void sendAfterUnbondRepayRemindSms(String phone,String name,Date date,BigDecimal borrowAmount,BigDecimal repayAmount,Integer period,Date dueDate);
	
	
	

    /**
      *  结清提醒(未绑卡用户) 提前15天/1天提醒
      *  @param phone 借款人电话号码
      * @param name 借款人名称
      * @param date 借款日期
      * @param borrowAmount 借款金额
      * @param dueDate 还款日期
     */
	void sendAfterUnbondSettleRemindSms(String phone,String name,Date date,BigDecimal borrowAmount,Date dueDate);
	
	
    /**
     *  多笔还款提醒（未绑卡用户) 还款日前7天/1天提醒
     */
	void sendAfterUnbondMutipleRepayRemindSms(String phone,String name,List<RepaymentBizPlanList> pLists);
	
	
	
	 /**
      *  还款提醒 （已绑卡用户），单笔还款，还款日前7天/1天提醒
      *  @param phone 借款人电话号码
      * @param name 借款人名称
      * @param date 借款日期
      * @param borrowAmount 借款金额
      * @param period 还款期数
      * @param dueDate 还款日期
      * @param repayAmount 应还金额
     */
	void sendAfterBindingRepayRemindSms(String phone,String name,Date date,BigDecimal borrowAmount,BigDecimal repayAmount,Integer period,Date dueDate,String tailNum);
	
	
	

    /**
      *  结清提醒(已绑卡用户) 提前15天/1天提醒
      *  @param phone 借款人电话号码
      * @param name 借款人名称
      * @param date 借款日期
      * @param borrowAmount 借款金额
      * @param dueDate 还款日期 
     */
	void sendAfterBindingSettleRemindSms(String phone,String name,Date date,BigDecimal borrowAmount,Date dueDate,String tailNum);
	
	
   /**
    * 多笔还款提醒（已绑卡用户) 还款日前7天/1天提醒
    * @param phone
    * @param name
    * @param pLists
    */
	void sendAfterBindingMutipleRepayRemindSms(String phone,String name,List<RepaymentBizPlanList> pLists,String tailNumber);
	
	
	
	 /**
     *  贷后逾期提醒（逾期的第1~3天）
     *  @param name 借款人名称
     *  @param period 逾期期数
     *  @param totalPeriods 总共期数
     */
	void sendAfterOverdueRemindSms(String phone,String name,Integer period,Integer taotalPeriods);
	
	
	
}
