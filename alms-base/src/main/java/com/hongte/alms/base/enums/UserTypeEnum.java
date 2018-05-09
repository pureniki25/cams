package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/1/24
 * 区域级别枚举定义
 */
public enum UserTypeEnum {

	PERSON(1,"个人"),
	COMPANY(2,"公司");

	private Integer key; // 数据保存的值
	private String name; // 名称

	private UserTypeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(UserTypeEnum d : UserTypeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static UserTypeEnum getByKey(String key){
		for(UserTypeEnum d : UserTypeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(UserTypeEnum d : UserTypeEnum.values()){
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
