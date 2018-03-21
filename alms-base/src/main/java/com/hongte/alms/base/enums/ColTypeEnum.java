package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/20
 */
public enum ColTypeEnum implements IEnum {

    PHONE_STAFF(1,"移交清算一"),
    COLLECTING(50,"移交清算二"),
    TO_LAW_WORK(100,"移交法务诉讼");

    private int value;
    private String desc;

    ColTypeEnum(final int value, final String desc) {
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
