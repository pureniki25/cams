package com.hongte.alms.platrepay.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlatformStatusTypeEnum implements IEnum {

	REPAYMENT_ALREADY(1, "已还款"),
	THIRD_PARTY(2, "逾期"),
	BANK(3, "待还款");

	private PlatformStatusTypeEnum(final int value, final String name) {
		this.value = value;
		this.name = name;
	}

	private final int value;
	private final String name;

	@Override
	public Serializable getValue() {
		return this.value;
	}

	public int value() {
		return this.value;
	}
	
	@JsonValue
    public String getName(){
        return this.name;
    }

	public static String getName(int value) {
		PlatformStatusTypeEnum[] repaySourceEnums = values();
		for (PlatformStatusTypeEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.value() == value) {
				return repaySourceEnum.getName();
			}
		}
		return null;
	}

}
