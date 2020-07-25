package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/22
 */
public enum TokenTypeEnum implements IEnum {

    TOKEN("token","token"),
    COOKIES("cookies","cookies");

    private String value;
    private String desc;

    TokenTypeEnum(final String value, final String desc) {
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
