package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款计划借款利率单位
 */
public enum RepayPlanBorrowRateUnitEnum implements IEnum {

//    1：年利率，2：月利率，3：日利率

    YEAR_RATE(1,"年利率"),
    MONTH_RATE(2,"月利率"),
    DAY_RATE(3,"日利率");

    private int value;
    private String desc;

    RepayPlanBorrowRateUnitEnum(final int value, final String desc) {
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
