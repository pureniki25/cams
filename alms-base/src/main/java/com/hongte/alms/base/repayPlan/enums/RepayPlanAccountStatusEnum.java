package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 分账标记(冲应收还款，根据冲应收明细进行分账)
 */
public enum RepayPlanAccountStatusEnum implements IEnum {

//            * 分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，
// 30：分账到资金端账户(平台)，40：分账到担保公司账户

    NO_DIVISION(0,"不线上分账"),
    DIVISION_TO_BORROWER(10,"分账到借款人账户"),
    DIVISION_TO_ASSET(20,"分账到资产端账户")
    ,DIVISION_TO_PLAT(30,"分账到资金端账户（平台）")
    ,DIVISION_TO_BOND(40,"分账到担保公司账户")
    ;

    private int value;
    private String desc;

    RepayPlanAccountStatusEnum(final int value, final String desc) {
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
