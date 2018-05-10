package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:曾坤
 * @date: 2018/5/10
 * 业务来源枚举
 */
public enum BusinessSourceTypeEnum implements IEnum {

//    0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
    NORMAL(0,"常规录入"),
    SETTLE_NEW(1,"结清续贷新业务"),
    SETTLE_CONTUNE(2,"结清续贷续贷业务"),
    OFFLINE_(3,"线下历史导入"),
    SCAN(4,"扫码业务"),
    CAR(5,"优质车抵贷"),
    YD_CREDIT(6,"一点授信");



    private int value;
    private String name;

    BusinessSourceTypeEnum(final int value, final String name) {
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
