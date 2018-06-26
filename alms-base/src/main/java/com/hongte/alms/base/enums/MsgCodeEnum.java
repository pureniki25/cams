package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:陈泽圣
 * @date: 2018/6/24
 * 短信模板
 */
public enum MsgCodeEnum implements IEnum {
   
    NIWO_REPAY_SUCCESS("niwo_repay_success","你我金融代扣成功短信代码"),
    NIWO_REPAY_FAIL("niwo_repay_fail","你我金融代扣失败短信代码"),
    NIWO_REPAY_REMIND("niwo_repay_remind","你我金融还款提醒"),
    NIWO_SETTLE_REMIND("niwo_repay_remind","你我金融结清提醒");
    

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
