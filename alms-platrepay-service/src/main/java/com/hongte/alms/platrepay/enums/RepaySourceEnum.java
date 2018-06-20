package com.hongte.alms.platrepay.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RepaySourceEnum implements IEnum {

	OFFLINE_TRANSFER(1, "线下转账"),
	THIRD_PARTY(2, "第三方代扣"),
	BANK(3, "银行代扣"),
	GATEWAY(4, "APP网关充值"),
	AGREEMENT(5, "协议代扣");

	private RepaySourceEnum(final int value, final String name) {
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
		RepaySourceEnum[] repaySourceEnums = values();
		for (RepaySourceEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.value() == value) {
				return repaySourceEnum.getName();
			}
		}
		return null;
	}

}
