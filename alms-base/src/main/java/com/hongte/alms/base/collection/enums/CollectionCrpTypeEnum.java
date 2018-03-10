package com.hongte.alms.base.collection.enums;

/**
 * @author zengkun
 * @since 2018/3/6
 * 设置催收方式枚举
 */
public enum CollectionCrpTypeEnum {

//	1.一般还款计划，2.末期还款计划
	NORMAL(1,"一般还款计划"),
	LAST(2,"末期还款计划")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private CollectionCrpTypeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(CollectionCrpTypeEnum d : CollectionCrpTypeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static CollectionCrpTypeEnum getByKey(String key){
		for(CollectionCrpTypeEnum d : CollectionCrpTypeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(CollectionCrpTypeEnum d : CollectionCrpTypeEnum.values()){
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
