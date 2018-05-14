package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author chenzs
 * @since 2018/5/07
 * 还款方式
 */
public enum RepayTypeEnum  implements IEnum{




	EXPIRY_INTEREST(1,"到期还本息"),
	FIRST_INTEREST(2,"先息后本"),
	EQUAL_INTEREST(4,"等本等息"),
	EQUAL_AMOUNT_INTEREST(5,"等额本息"),
	DIVIDE_INTEREST(9,"分期还本付息"),
	DIVIDE_INTEREST_FIVE(500,"分期还本付息5年"),
	DIVIDE_INTEREST_TEN(1000,"分期还本付息10年");


    private int value;
    private String name;

    RepayTypeEnum(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
}
