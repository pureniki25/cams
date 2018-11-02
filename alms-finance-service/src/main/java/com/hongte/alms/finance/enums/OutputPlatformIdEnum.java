package com.hongte.alms.finance.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OutputPlatformIdEnum implements IEnum {

	//贷后 出款平台ID，0：线下出款，1：团贷网P2P上标，2：你我金融业务，3：粤财
	//核心 1.团贷网 2.你我金融; 3.粤财;4.线下出款 5.星享融 6.德古 99.其他
	
	OFF_LINE(0, 4, "线下出款"),
	TUANDAI(1, 1, "团贷网P2P上标"),
	NIWO(2, 2, "粤财"),
	YUE_CAI(3, 3, "你我金融业务");

	private OutputPlatformIdEnum(final int dhValue,final int hxValue,final String name) {
		this.dhValue = dhValue;
		this.hxValue = hxValue;
		this.name = name;
	}

	private final int dhValue;
	private final int hxValue;
	private final String name;

	@Override
	public Serializable getValue() {
		return this.dhValue;
	}

	public int dhValue() {
		return this.dhValue;
	}
	
	public int hxValue() {
		return this.hxValue;
	}
	
	@JsonValue
    public String getName(){
        return this.name;
    }

	public static String getName(int dhValue) {
		OutputPlatformIdEnum[] repaySourceEnums = values();
		for (OutputPlatformIdEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.dhValue() == dhValue) {
				return repaySourceEnum.getName();
			}
		}
		return null;
	}
	
	public static Integer getHxValue(int dhValue) {
		OutputPlatformIdEnum[] repaySourceEnums = values();
		for (OutputPlatformIdEnum repaySourceEnum : repaySourceEnums) {
			if (repaySourceEnum.dhValue() == dhValue) {
				return repaySourceEnum.hxValue;
			}
		}
		return null;
	}

}
