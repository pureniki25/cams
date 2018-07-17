package com.hongte.alms.withhold.service;



/**
 * @author czs
 * 代扣短信提醒服务类
 * @since 2018/7/14
 */
public interface SmsService {



    
    /**
     * 发送逾期提醒(逾期的第1~3天)
     */
    void sendOverDueRemindMsg(Integer count);
    
  
    
    /**
     * 单笔还款，还款日前7天/1天提醒
     */
    void sendOneRemindMsg(Integer count);
    
    

    /**
     * 多笔还款提醒,还款日前7天/1天提醒
     */
    void sendMutiplyRemindMsg(Integer count);
    
    
  
    
    /**
     * 单笔结清，结清日前15天/1天提醒
     */
    void sendOneSettleRemindMsg(Integer count);
    

   

}
