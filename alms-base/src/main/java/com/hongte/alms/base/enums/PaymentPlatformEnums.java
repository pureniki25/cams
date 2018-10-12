package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:胡伟骞
 * @date: 2018/8/3
 */
public enum PaymentPlatformEnums implements IEnum {

	XIANXIA(0, "线下出款"),
    TUANDAI(1, "团贷网"),
    NIWO(2, "你我金融"),
    YUECAI(3, "粤财"),	
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
     * 根据资金端编号获取对应的资金端名称
     * @param value
     * @return
     * @author huweiqian
     */
    public static String getName(int value) {  
		PaymentPlatformEnums[] paymentPlatformEnums = values();  
        for (PaymentPlatformEnums platformEnums : paymentPlatformEnums) {  
            if (platformEnums.value() == value) {  
                return platformEnums.getName();  
            }  
        }  
        return null;  
    } 
    
    /**
     * 根据资金端名称获取获取资金端编号
     * @param name
     * @return
     * @author huweiqian
     */
    public static int getValueByName(String name) {  
    	PaymentPlatformEnums[] paymentPlatformEnums = values();  
    	for (PaymentPlatformEnums enums : paymentPlatformEnums) {  
    		if (enums.getName().equals(name)) {  
    			return enums.value();  
    		}  
    	}  
    	return -1;  
    }
}
