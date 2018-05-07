package com.hongte.alms.base.enums.repayPlan;

/**
 * @author zengkun
 * @since 2018/4/25
 * 生成还款计划还款方式(利息计算方式)枚举
 */
public enum RepayPlanRepayIniCalcWayEnum {

//1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
//到期还本息用1表示， 每月付息到期还本用2表示
	PAY_LAST(1,"到期还本息"),  //还未实现算法
	PRINCIPAL_LAST(2,"先息后本"),
	INT_AND_PRIN_EQUAL(5,"等额本息"),
	INT_AND_PRIN_EVERYTIME(9,"分期还本付息"),
	INT_AND_PRIN_AVERAGE(11,"等本等息")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private RepayPlanRepayIniCalcWayEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(RepayPlanRepayIniCalcWayEnum d : RepayPlanRepayIniCalcWayEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static RepayPlanRepayIniCalcWayEnum getByKey(Integer key){
		for(RepayPlanRepayIniCalcWayEnum d : RepayPlanRepayIniCalcWayEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(RepayPlanRepayIniCalcWayEnum d : RepayPlanRepayIniCalcWayEnum.values()){
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
