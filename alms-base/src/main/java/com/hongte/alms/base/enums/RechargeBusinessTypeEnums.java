package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hongte.alms.common.util.Constant;

/**
 * @author:胡伟骞
 */
public enum RechargeBusinessTypeEnums implements IEnum {

    CAR_ZQ_LOAN(1,Constant.CAR_LOAN),
    HOUSE_ZQ_LOAN(2,Constant.HOUSE_LOAN),
    FINANCE_STORAGE(3,Constant.HOUSE_LOAN),
    SAN_NONG_FINANCE(4,Constant.HOUSE_LOAN),
    CAR_LOAN(9,Constant.CAR_LOAN),
    HOUSE_LOAN(11,Constant.HOUSE_LOAN),
    CHE_QUAN_DIAN_ZI(12,Constant.CAR_LOAN),
    POVERTY_ALLEVIATION(13,Constant.POVERTY_ALLEVIATION_LOAN),
    CAR_FINANCING_LEASE(14,Constant.CAR_LOAN),
    ER_SHOU_CAR(15,Constant.ER_SHOU_CHE_LOAN),
    YI_DIAN_CAR(20,Constant.YI_DIAN_LOAN),
    OWNER_LOAN(26,Constant.CREDIT_LOAN),
    OWNER_GONGJIE(27,Constant.CREDIT_LOAN),
    JIA_ZHUANG(28,Constant.CREDIT_LOAN),
    BUSINESS_GONGJIE(29,Constant.CREDIT_LOAN),
    BUSINESS_LOAN(30,Constant.CREDIT_LOAN),
    YOU_FANG_LOAN(35,Constant.HOUSE_LOAN),
    ;


    private int value;
    private String name;

    RechargeBusinessTypeEnums(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
    
    public int value() {
        return this.value;
    }
    
    public static String getName(int value) {  
		RechargeBusinessTypeEnums[] businessTypeEnums = values();  
        for (RechargeBusinessTypeEnums businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
}
