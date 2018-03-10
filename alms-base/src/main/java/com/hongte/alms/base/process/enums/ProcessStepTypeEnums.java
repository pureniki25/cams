package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/3/1
 * 流程类型枚举
 */
public enum ProcessStepTypeEnums {

//	1，起始节点；2，中间节点，3，结束节点

	BEGIN_STEP(1,"起始节点"),
	MIDDLE_STEP(2,"中间节点"),
	END_STEP(3,"结束节点");

//	(0:运行中,1:开始,2:结束,3:注销)

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessStepTypeEnums(Integer key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessStepTypeEnums d : ProcessStepTypeEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessStepTypeEnums getByKey(String key){
		for(ProcessStepTypeEnums d : ProcessStepTypeEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessStepTypeEnums d : ProcessStepTypeEnums.values()){
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

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}




}
