package com.hongte.alms.base.repayPlan.enums;

/**
 * @author zengkun
 * @since 2018/4/25
 * 还款计划费用项是否是一次性收取的枚举
 */
public enum RepayPlanIsOneTimeChargeEnum {

//	[是否一次收取，1为按月收取，2为一次收取]
	BY_MONTH(1,"按月收取"),
	ONE_TIME(2,"一次收取")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private RepayPlanIsOneTimeChargeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(RepayPlanIsOneTimeChargeEnum d : RepayPlanIsOneTimeChargeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static RepayPlanIsOneTimeChargeEnum getByKey(String key){
		for(RepayPlanIsOneTimeChargeEnum d : RepayPlanIsOneTimeChargeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(RepayPlanIsOneTimeChargeEnum d : RepayPlanIsOneTimeChargeEnum.values()){
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
