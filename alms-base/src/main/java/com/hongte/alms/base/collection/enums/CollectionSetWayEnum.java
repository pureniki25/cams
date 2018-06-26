package com.hongte.alms.base.collection.enums;

/**
 * @author zengkun
 * @since 2018/3/6
 * 设置催收方式枚举
 */
public enum CollectionSetWayEnum {

	MANUAL_SET(1,"界面手动设置"),
	AUTO_SET(2,"定时器自动设置"),
	XINDAI_CALL(3,"信贷回调"),
	XINDAI_LOG(4,"信贷历史数据导入"),
	XINDAI_LOG_ONE(5,"信贷单个历史数据导入")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private CollectionSetWayEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(CollectionSetWayEnum d : CollectionSetWayEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static CollectionSetWayEnum getByKey(String key){
		for(CollectionSetWayEnum d : CollectionSetWayEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(CollectionSetWayEnum d : CollectionSetWayEnum.values()){
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
