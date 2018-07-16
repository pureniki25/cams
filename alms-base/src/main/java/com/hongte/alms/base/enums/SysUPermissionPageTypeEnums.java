package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 用户可见业务范围，界面类型枚举
 */
public enum SysUPermissionPageTypeEnums {

	DH_MANGER(1,"贷后管理页")
	,FINANCE_MANAGER(2,"财务管理页面")
	,DERATE_MANAGER(3,"减免管理页面")
	,PROCESS_MANAGER(4,"审批查询界面")
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
			if(d.key.equals(key)){
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
