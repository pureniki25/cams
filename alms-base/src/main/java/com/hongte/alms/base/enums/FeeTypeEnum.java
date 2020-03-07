package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2019/06/23
 */
public enum FeeTypeEnum implements IEnum {

    FEE_BANK("1","费用银现"),
    FEE_PAY("3","费用现付"),
    FEE_CASH("2","费用现金支付");
    
	
	
	 

    private String value;
    private String desc;

    FeeTypeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
}
