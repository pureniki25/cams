package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:胡伟骞
 */
public enum AlmsServiceNameEnums implements IEnum {

	CORE(1, "alms-core-service"),
	FINANCE(2, "alms-finance-service"),
	OPEN(3, "alms-open-service"),
	PLATREPAY(4, "alms-platrepay-service"),
	SCHEDULED(5, "alms-scheduled-service"),
	WOTHHOLD(6, "alms-withhold-service");


    private int value;
    private String name;

    AlmsServiceNameEnums(final int value, final String name) {
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
		AlmsServiceNameEnums[] businessTypeEnums = values();  
        for (AlmsServiceNameEnums businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
}
