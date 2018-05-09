package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款计划是否有效的标志位
 */
public enum RepayPlanActiveEnum implements IEnum {

    ACTIVE(1,"有效"),
    NOACTIVE(0,"无效");

    private int value;
    private String desc;

    RepayPlanActiveEnum(final int value, final String desc) {
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
