package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 用户可见业务范围，界面类型枚举
 */
public enum SysUPermissionPageTypeEnums {

	DAIHOU(1,"贷后管理列表")
	,CAR(2,"车辆管理列表")
	,DERATE(3,"减免管理列表")
	,WITHHOLD(4,"代扣管理列表")
	,WITHHOLD_QUERY(5,"代扣查询列表")
	,AUDIT(6,"审批查询列表")
	,DEFER(7,"展期管理列表")
	,MESSAGE(8,"消息管理列表")
	,FINANCE(9,"财务管理列表")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private SysUPermissionPageTypeEnums(Integer key, String name ) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysUPermissionPageTypeEnums d : SysUPermissionPageTypeEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;
	}

	public static SysUPermissionPageTypeEnums getByKey(String key){
		for(SysUPermissionPageTypeEnums d : SysUPermissionPageTypeEnums.values()){
			if(d.key == Integer.parseInt(key)){
				return d;
			}
		}
		return null;
	}

	public static Integer keyOf(String name){
		for(SysUPermissionPageTypeEnums d : SysUPermissionPageTypeEnums.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public String getName() {
		return name;
	}

	public Integer getKey() {
		return key;
	}

}
