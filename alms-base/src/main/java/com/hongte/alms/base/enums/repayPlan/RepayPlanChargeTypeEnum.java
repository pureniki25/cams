package com.hongte.alms.base.enums.repayPlan;

/**
 * @author zengkun
 * @since 2018/4/25
 * 还款计划费用项收取方式枚举（是否是一次性收取）
 */
public enum RepayPlanChargeTypeEnum {

	//	[是否一次收取，1为按月收取，2为一次收取]
	BY_MONTH(1,"按月收取"),
	ONE_TIME(2,"一次收取")
	;




	private Integer key; // 数据保存的值
	private String name; // 名称

	private RepayPlanChargeTypeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(RepayPlanChargeTypeEnum d : RepayPlanChargeTypeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static RepayPlanChargeTypeEnum getByKey(String key){
		for(RepayPlanChargeTypeEnum d : RepayPlanChargeTypeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(RepayPlanChargeTypeEnum d : RepayPlanChargeTypeEnum.values()){
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
