package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:czs
 * @date: 2019/04/12
 */
public enum UnitEnum implements IEnum {

    JIN(2.0,"斤","公斤"),
    KE(1000.0,"克","千克");

    private Double value;
    private String desc;
    private String transferDesc;

    UnitEnum(final Double value, final String desc,final String transferDesc) {
        this.value = value;
        this.desc = desc;
        this.transferDesc = transferDesc;
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
    @JsonValue
    public String getTransferDesc(){
        return this.transferDesc;
    }
}
