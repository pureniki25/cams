package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:曾坤
 * @date: 2018/5/11
 * 业务客户类型枚举
 */
public enum BizCustomerTypeEnum implements IEnum {

    PERSON(1,"个人"),
    COMPANY(2,"企业");



    private int value;
    private String name;

    BizCustomerTypeEnum(final int value, final String name) {
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
    
    public static BizCustomerTypeEnum convertByName(String name) {
    	for (BizCustomerTypeEnum e : BizCustomerTypeEnum.values()) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
    	return null ;
    }
}
