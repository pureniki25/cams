package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:胡伟骞
 */
public enum RechargeAccountTypeToBusinessTypeEnums implements IEnum {

    CAR_ZQ_LOAN(1,"车贷代充值"),
    HOUSE_ZQ_LOAN(2,"房贷代充值"),
    FINANCE_STORAGE(3,"房贷代充值"),
    SAN_NONG_FINANCE(4,"房贷代充值"),
    CAR_LOAN(9,"车贷代充值"),
    HOUSE_LOAN(11,"房贷代充值"),
    CHE_QUAN_DIAN_ZI(12,"车贷代充值"),
    POVERTY_ALLEVIATION(13,"扶贫贷代充值"),
    CAR_FINANCING_LEASE(14,"车贷代充值"),
    ER_SHOU_CAR(15,"二手车业务代充值"),
    YI_DIAN_CAR(20,"一点车贷代充值"),
    OWNER_LOAN(26,"信用贷代充值"),
    OWNER_GONGJIE(27,"信用贷代充值"),
    JIA_ZHUANG(28,"信用贷代充值"),
    BUSINESS_GONGJIE(29,"信用贷代充值"),
    BUSINESS_LOAN(30,"信用贷代充值"),
    
    ;


    private int value;
    private String name;

    RechargeAccountTypeToBusinessTypeEnums(final int value, final String name) {
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
		RechargeAccountTypeToBusinessTypeEnums[] businessTypeEnums = values();  
        for (RechargeAccountTypeToBusinessTypeEnums businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
}
