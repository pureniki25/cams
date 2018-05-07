package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 是否展期还款计划的标志位
 */
public enum RepayPlanIsDeferEnum implements IEnum {

    //0:否，1:是

    YES(1,"是"),
    NO(0,"否");

    private int value;
    private String desc;

    RepayPlanIsDeferEnum(final int value, final String desc) {
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
