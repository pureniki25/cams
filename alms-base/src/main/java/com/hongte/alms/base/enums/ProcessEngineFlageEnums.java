package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/5
 * 流程引擎标志位
 */
public enum ProcessEngineFlageEnums {

	LOCAL_SIMPLE_ENGINE(1,"本地的简单流程引擎");

//	borrowLimitUnit

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessEngineFlageEnums(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessEngineFlageEnums d : ProcessEngineFlageEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessEngineFlageEnums getByKey(String key){
		for(ProcessEngineFlageEnums d : ProcessEngineFlageEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessEngineFlageEnums d : ProcessEngineFlageEnums.values()){
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
