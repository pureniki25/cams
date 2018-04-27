package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 财务确认还款标志位
 */
public enum RepayPlanConfirmFlageEnum implements IEnum {
    //财务还款金额确认(1:已确认,0:未确认)
    CHECKED(1,"已确认"),
    NEVER_CHECK(0,"未确认");

    private int value;
    private String desc;

    RepayPlanConfirmFlageEnum(final int value, final String desc) {
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
