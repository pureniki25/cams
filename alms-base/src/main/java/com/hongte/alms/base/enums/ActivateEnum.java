package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/22
 */
public enum ActivateEnum implements IEnum {

    VALID(1,"有效"),
    INVALID(0,"无效");

    private int value;
    private String desc;

    ActivateEnum(final int value, final String desc) {
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
