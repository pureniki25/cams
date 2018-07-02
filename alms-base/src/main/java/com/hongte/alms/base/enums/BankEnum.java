package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:胡伟骞
 * @date: 2018/6/29
 */
public enum BankEnum implements IEnum {

	GDB("GDB", "广发银行"), 
	ICBC("ICBC", "中国工商银行"), 
	BOC("BOC", "中国银行"), 
	CMBCHINA("CMBCHINA", "招商银行"), 
	PINGAN("PINGAN", "平安银行"), 
	HXYH("HXYH", "华夏银行"), 
	BOCO("BOCO", "交通银行"), 
	POST("POST", "中国邮政储蓄银行"), 
	CCB("CCB", "中国建设银行"), 
	SPDB("SPDB", "浦发银行"), 
	CMBC("CMBC", "中国民生银行"), 
	BCCB("BCCB", "北京银行"), 
	CEB("CEB", "中国光大银行"), 
	CIB("CIB", "兴业银行"), 
	ECITIC("ECITIC", "民生银行"), 
	ABC("ABC", "中国农业银行"), 
	;

	private String value;
	private String name;

	BankEnum(final String value, final String name) {
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
		BankEnum[] bankEnums = values();
		if (bankEnums != null && bankEnums.length > 0) {
			for (BankEnum bankEnum : bankEnums) {
				if (bankEnum.value() != null && bankEnum.value().equals(value)) {
					return bankEnum.getName();
				}
			}
		}
		return null;
	}
}
