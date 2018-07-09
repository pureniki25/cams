package com.hongte.alms.base.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hongte.alms.common.util.Constant;

/**
 * @author:胡伟骞
 */
public enum RechargeAccountTypeEnums implements IEnum {

	CAR_LOAN(1, Constant.CAR_LOAN), 
	HOUSE_LOAN(2, Constant.HOUSE_LOAN), 
	POVERTY_ALLEVIATION_LOAN(3, Constant.POVERTY_ALLEVIATION_LOAN), 
	QUICK_LOAN(4, Constant.QUICK_LOAN), 
	CHE_QUAN_LOAN(5, Constant.CHE_QUAN_LOAN), 
	ER_SHOU_CHE_LOAN(6, Constant.ER_SHOU_CHE_LOAN), 
	YI_DIAN_LOAN(7, Constant.YI_DIAN_LOAN), 
	CREDIT_LOAN(8, Constant.CREDIT_LOAN),
	;

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
