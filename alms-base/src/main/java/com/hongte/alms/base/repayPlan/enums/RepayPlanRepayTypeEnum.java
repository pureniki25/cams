package com.hongte.alms.base.repayPlan.enums;

/**
 * @author zengkun
 * @since 2018/4/25
 * 生成还款计划还款方式枚举
 */
public enum RepayPlanRepayTypeEnum {

//	到期还本息用1表示， 每月付息到期还本用2表示

	PRINCIPAL_LAST(1,"先息后本"),
	INT_AND_PRIN_EVERYTIME(2,"分期还本付息"),
	INT_AND_PRIN_EQUAL(3,"等额本息"),
	INT_AND_PRIN_AVERAGE(4,"等本等息")

	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private RepayPlanRepayTypeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(RepayPlanRepayTypeEnum d : RepayPlanRepayTypeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static RepayPlanRepayTypeEnum getByKey(String key){
		for(RepayPlanRepayTypeEnum d : RepayPlanRepayTypeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(RepayPlanRepayTypeEnum d : RepayPlanRepayTypeEnum.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public String getName() {
		return name;
	}

	public int getKey() {
		return key;
	}

}
