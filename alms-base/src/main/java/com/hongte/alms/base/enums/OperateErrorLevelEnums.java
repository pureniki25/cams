package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zengkun
 * @since 2018/11/26
 */

public enum OperateErrorLevelEnums implements IEnum {

    ERROR(1, "错误"),
    WARN(2,"警告"),
    DEBUG(3,"调试信息");


    private Integer value;
    private String desc;

    OperateErrorLevelEnums(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }


}
