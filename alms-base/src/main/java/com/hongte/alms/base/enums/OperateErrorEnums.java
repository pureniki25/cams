package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zengkun
 * @since 2018/11/26
 */

public enum OperateErrorEnums implements IEnum {

    NO_PROJ_INFO("001", "没有标的信息"),
    JG("02", "极光");


    private String value;
    private String desc;

    OperateErrorEnums(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }





}
