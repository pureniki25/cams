package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/1/24
 * 区域级别枚举定义
 */
public enum AreaLevel {

	AREA_LEVEL(40,"区域级别"),
	COMPANY_LEVEL(60,"分公司级别"),
	DEPARTMENT_LEVEL(80,"部门级别"),
	GROUP_LEVEL(100,"小组级别");

	private Integer key; // 数据保存的值
	private String name; // 名称

	private AreaLevel(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(AreaLevel d : AreaLevel.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static AreaLevel getByKey(String key){
		for(AreaLevel d : AreaLevel.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(AreaLevel d : AreaLevel.values()){
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
