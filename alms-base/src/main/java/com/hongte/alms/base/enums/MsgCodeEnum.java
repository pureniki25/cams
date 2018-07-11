package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:陈泽圣
 * @date: 2018/6/24
 * 短信模板
 */
public enum MsgCodeEnum implements IEnum {
   
	
	//********************你我金融短信模板*****************************//
    NIWO_REPAY_SUCCESS("niwo_repay_success","你我金融代扣成功短信代码"),
    NIWO_REPAY_FAIL("niwo_repay_fail","你我金融代扣失败短信代码"),
    NIWO_REPAY_REMIND("niwo_repay_remind","你我金融还款提醒"),
    NIWO_SETTLE_REMIND("niwo_settle_remind","你我金融结清提醒"),
    
	//********************贷后短信模板*****************************//
	
	AFTER_REPAY_SUCCESS("after_repay_success","扣款成功短信通知(贷后)"),
	AFTER_REPAY_FAIL("after_repay_fail","扣款失败短信通知(贷后)"),
	AFTER_UNBOND_REPAY_REMIND("after_unbond_repay_remind","还款提醒 （未绑卡用户），单笔还款，还款日前7天/1天提醒"),
	AFTER_UNBOND_SETTLE_REMIND("after_unbond_settle_remind","结清提醒（未绑卡用户）"),
	ATER_UNBOND_MUTIPLE_REPAY_REMIND("ater_unbond_mutiple_repay_remind","多笔还款提醒（未绑卡用户)"),
	AFTER_BINDING_REPAY_REMIND("after_binding_repay_remind","还款提醒（已绑卡用户）单笔还款，还款日前7天/1天提醒"),
	AFTER_BINDING_SETTLE_REMIND("after_binding_settle_remind","还款提醒（已绑卡用户），结清提醒，提前15天/1天提醒"),
	ATER_BINDING_MUTIPLE_REPAY_REMIND("ater_binding_mutiple_repay_remind","还款提醒（已绑卡用户），多笔还款，还款日前7天/1天提醒"),
	AFTER_OVERDUE_REMIND("after_overdue_remind","贷后逾期提醒（逾期的第1~3天）");
	
	
	
	
	
    private String value;
    private String desc;

    MsgCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }


  

    @JsonValue
    public String getDesc(){
        return this.desc;
    }



}
