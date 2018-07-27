package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PepayPlanRepayFlagStatusEnum implements IEnum {
    //已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，
    // 30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，
    // 70：银行代扣全部结清',

    DELAY_REPAYMENT(6, "申请展期已还款"),
    UNDERLINE_REPAYMENT(10, "线下确认已还款"),
    AUTO_WITHHOLD_REPAYMENT(20, "自动线下代扣已还款"),

    MAN_WITHHOLD_REPAYMENT(21, "人工线下代扣已还款"),

    AUTO_BANK_WITHHOLD_REPAYMENT(30, "自动银行代扣已还款"),
    MAN_BANK_WITHHOLD_REPAYMENT(31, "人工银行代扣已还款"),

    APP_REPAYMENT(40, "用户APP主动还款"),

    UNDERLINE_ALL_SETTLE(50, "线下财务确认全部结清"),
    UNDERLINE_WITHHOLD_ALL_SETTLE(60, "线下代扣全部结清"),
    BANK_WITHHOLD_ALL_SETTLE(70, "银行代扣全部结清");

    private Integer value;
    private String desc;

    PepayPlanRepayFlagStatusEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc() {
        return this.desc;
    }


    public static String nameOf(Integer key) {
        for (PepayPlanRepayFlagStatusEnum d : PepayPlanRepayFlagStatusEnum.values()) {
            if (d.value.equals(key)) {
                return d.desc;
            }
        }
        return null;

    }

    public static PepayPlanRepayFlagStatusEnum getByKey(Integer key) {
        for (PepayPlanRepayFlagStatusEnum d : PepayPlanRepayFlagStatusEnum.values()) {
            if (d.value.equals(key)) {
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name) {
        for (PepayPlanRepayFlagStatusEnum d : PepayPlanRepayFlagStatusEnum.values()) {
            if (d.desc.equals(name)) {
                return d.value;
            }
        }
        return null;

    }


}
