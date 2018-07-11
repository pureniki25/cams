package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:czs
 * @date: 2018/7/7
 */
public enum WithholdTypeEnum implements IEnum {

	APPRUN("app_run", "APP代扣"), 
	AUTORUN("auto_run", "自动代扣库");

	private String value;
	private String name;

	WithholdTypeEnum(final String value, final String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public Serializable getValue() {
		return this.value;
	}

	@JsonValue
	public String getName() {
		return this.name;
	}

	public String value() {
		return this.value;
	}

	public static String getName(String value) {
		WithholdTypeEnum[] bankEnums = values();
		if (bankEnums != null && bankEnums.length > 0) {
			for (WithholdTypeEnum bankEnum : bankEnums) {
				if (bankEnum.value() != null && bankEnum.value().equals(value)) {
					return bankEnum.getName();
				}
			}
		}
		return null;
	}
}
