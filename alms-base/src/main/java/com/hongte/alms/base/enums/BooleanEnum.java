package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:是否枚举
 * @date: 2018/5/7
 */
public enum BooleanEnum  {

    YES(1,"是"),
    NO(0,"否");

    private int value;
    private String desc;

    BooleanEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
}
