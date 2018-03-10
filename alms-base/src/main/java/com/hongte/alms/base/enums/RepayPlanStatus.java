package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/1/24
 * 还款状态枚举定义
 */
public enum RepayPlanStatus {


//	"还款中"，"逾期"，"已还款"

	REPAYING(1,"还款中"),
	REPAYED(2,"已还款"),
	OVERDUE(3,"逾期"),
	PARTAIL(4,"部分还款");

	private Integer key; // 数据保存的值
	private String name; // 名称

	private RepayPlanStatus(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(RepayPlanStatus d : RepayPlanStatus.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static RepayPlanStatus getByKey(String key){
		for(RepayPlanStatus d : RepayPlanStatus.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(RepayPlanStatus d : RepayPlanStatus.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
