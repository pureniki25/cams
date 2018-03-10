package com.hongte.alms.base.collection.enums;

/**
 * @author zengkun
 * @since 2018/1/26
 * 催收人员类型枚举定义
 */
public enum StaffPersonType {


	PHONE_STAFF("phoneStaff","电催",1),
	VISIT_STAFF("visitStaff","上门催收",2)
	,LAW("lawStaff","法务",3);

	private String key; // 数据保存的值
	private String name; // 名称
	private Integer intKey;//数据库存储的int类型值

	private StaffPersonType(String key, String name,Integer intKey) {
		this.name = name;
		this.key = key;
		this.intKey =intKey;
	}


	public static String nameOf(Integer key){
		for(StaffPersonType d : StaffPersonType.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static StaffPersonType getByKey(String key){
		for(StaffPersonType d : StaffPersonType.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(StaffPersonType d : StaffPersonType.values()){
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getIntKey() {
		return intKey;
	}

	public void setIntKey(Integer intKey) {
		this.intKey = intKey;
	}
}
