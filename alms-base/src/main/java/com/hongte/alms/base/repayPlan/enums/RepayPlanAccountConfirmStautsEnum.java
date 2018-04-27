package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 会计确认状态
 */
public enum RepayPlanAccountConfirmStautsEnum implements IEnum {
    //会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
    WAIT(0,"待审核"),
    VERIFIED(1,"已审核"),
    RETURNED(2,"已退回"),
    VERIFIED_AGAIN(3,"已返审核"),
    IN_PORTED(4,"导入");

    private int value;
    private String desc;

    RepayPlanAccountConfirmStautsEnum(final int value, final String desc) {
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
