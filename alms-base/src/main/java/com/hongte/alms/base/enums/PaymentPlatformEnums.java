package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:胡伟骞
 * @date: 2018/8/3
 */
public enum PaymentPlatformEnums implements IEnum {

    TUANDAI(1, "团贷网"),
    NIWO(2, "你我金融"),
    YUECAI(3, "粤财"),	
    XIANXIA(4, "线下出款"),
    ;		

    private int value;	// 资金端编号
    private String name;	// 资金端名称

    /**
     * @param value	资金端编号
     * @param name	资金端名称
     */
    PaymentPlatformEnums(final int value, final String name) {
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
    
    /**
     * 根据业务类型ID获取对应的业务类型名称
     * @param value
     * @return
     * @author huweiqian
     */
    public static String getName(int value) {  
		PaymentPlatformEnums[] businessTypeEnums = values();  
        for (PaymentPlatformEnums businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
}
