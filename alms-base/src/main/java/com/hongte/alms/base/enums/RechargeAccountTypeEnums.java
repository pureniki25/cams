package com.hongte.alms.base.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:胡伟骞
 */
public enum RechargeAccountTypeEnums implements IEnum {

	CAR_LOAN(1, "车贷代充值"), HOUSE_LOAN(2, "房贷代充值"), POVERTY_ALLEVIATION_LOAN(3, "扶贫贷代充值"), QUICK_LOAN(4,
			"闪贷业务代充值"), CHE_QUAN_LOAN(5,
					"车全业务代充值"), ER_SHOU_CHE_LOAN(6, "二手车业务代充值"), YI_DIAN_LOAN(7, "一点车贷代充值"), CREDIT_LOAN(8, "信用贷代充值"),;

	private int value;
	private String name;
	
	private static List<String> nameList = new ArrayList<>();

	RechargeAccountTypeEnums(final int value, final String name) {
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

	public int value() {
		return this.value;
	}

	public static String getName(int value) {
		RechargeAccountTypeEnums[] rechargeAccountTypeEnums = values();
		for (RechargeAccountTypeEnums enums : rechargeAccountTypeEnums) {
			if (enums.value() == value) {
				return enums.getName();
			}
		}
		return null;
	}

	public static List<String> listName() {
		
		if (!nameList.isEmpty()) {
			return nameList;
		}
		
		RechargeAccountTypeEnums[] rechargeAccountTypeEnums = values();
		for (RechargeAccountTypeEnums enums : rechargeAccountTypeEnums) {
			nameList.add(enums.getName());
		}

		return nameList;
	}
}
