package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum BusinessTypeEnum implements IEnum {

    CYDZQ_TYPE(1,"车易贷展期"),
    FSDZQ_TYPE(2,"房速贷展期"),
    JRCC_TYPE(3,"金融仓储"),
    SN_TYPE(4,"三农金融"),
    CYD_TYPE(9,"车易贷"),
    FSD_TYPE(11,"房速贷"),
    CQSZDC_TYPE(12,"车全垫资代采"),
    FPD_TYPE(13,"扶贫贷"),
    QCRZZL_TYPE(14,"汽车融资租赁"),
    ESC_TYPE(15,"房速贷");
    


    private int value;
    private String name;

    BusinessTypeEnum(final int value, final String name) {
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
