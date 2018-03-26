package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum PlatformEnum implements IEnum {

    YB_FORM(0,"易宝绑卡"),
    YS_FORM(1,"银盛绑卡"),
    FY_FORM(2,"富友绑卡"),
    BF_FORM(3,"宝付代扣"),
    AN_FORM(4,"爱农代扣");

    private int value;
    private String name;

    PlatformEnum(final int value, final String name) {
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
}
