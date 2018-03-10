package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/2/8
 * 流程状态定义中审核人类型定义
 */
public enum ProcessApproveUserType {

//	当前审核人类型 1固定审核人 2发起人 3动态SQL获取',

	FIXED(1,"固定审核人"),
	CREATER(2,"发起人"),
	BY_SQL(3,"动态SQL获取")
//	,BY_FUNCTION(4,"通过系统内置函数获取")
	,BY_ROLE(5,"通过角色配置获取")
	;


	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessApproveUserType(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessApproveUserType d : ProcessApproveUserType.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessApproveUserType getByKey(String key){
		for(ProcessApproveUserType d : ProcessApproveUserType.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessApproveUserType d : ProcessApproveUserType.values()){
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
