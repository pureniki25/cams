package com.hongte.alms.core.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessStatusTypeEnum implements IEnum {

	TO_DO(0, "待分发"),
	IN_PROCESS(1, "分发处理中"),
	SUCCESED(2, "分发成功"),
	FAILED(3, "分发失败");

	private ProcessStatusTypeEnum(final int value, final String name) {
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
		ProcessStatusTypeEnum[] repaySourceEnums = values();
		for (ProcessStatusTypeEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.value() == value) {
				return repaySourceEnum.getName();
			}
		}
		return null;
	}

}
