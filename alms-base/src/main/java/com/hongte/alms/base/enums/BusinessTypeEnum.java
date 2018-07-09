package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum BusinessTypeEnum implements IEnum {

    CYDZQ_TYPE(1,"车易贷展期"),
    FSDZQ_TYPE(2,"房速贷展期"),
    JRCC_TYPE(3,"金融仓储"),
    SN_TYPE(4,"三农金融"),
    CYD_TYPE(9,"车易贷"),
    FSD_TYPE(11,"房速贷"),
    CQSZDC_TYPE(12,"车全垫资代采"),
    FPD_TYPE(13,"扶贫贷"),
    QCRZZL_TYPE(14,"汽车融资租赁"),
    ESC_TYPE(15,"二手车商贷"),
    YD_CAR_TYPE(20,"一点车贷"),
    CREDIT_TYPE(25,"信用贷"),
    PROPRIETOR_TYPE(26,"业主贷"),
    INSTALMENT_CREDIT_TYPE(27,"家装分期"),
    COMMERCE_COMPANY_TYPE(28,"商贸共借"),
    PROPRIETOR_COMPANY_TYPE(29,"业主共借"),
	COMMERCE_TYPE(30,"商贸贷"),
	YOU_FANG_TYPE(35,"优房贷");


    private int value;
    private String name;

    BusinessTypeEnum(final int value, final String name) {
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
		BusinessTypeEnum[] businessTypeEnums = values();  
        for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
}
