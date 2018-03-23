package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/22
 */
public enum TimeTypeEnum implements IEnum {

    HOUR(0,"按小时"),
    MINUTE(1,"按分钟"),
    SECOND(2,"按秒"),
    DAY(3,"按天"),
    NONE(4,"不设时间");

    private int value;
    private String desc;

    TimeTypeEnum(final int value, final String desc) {
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
