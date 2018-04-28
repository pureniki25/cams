package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款来源枚举
 */
public enum RepayPlanRepaySrcEnum implements IEnum {

//还款来源，10：线下转账，20：线下代扣，30：银行代扣

    OFFLINE_TRANSFER(10,"线下转账"),
    OFFLINE_WITHHOLD(20,"线下代扣"),
    BNAK_WITHHOLD(30,"银行代扣")

    ;

    private int value;
    private String desc;

    RepayPlanRepaySrcEnum(final int value, final String desc) {
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
