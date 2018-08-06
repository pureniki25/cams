package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 系统角色区域控制枚举
 */
public enum SysRoleAreaTypeEnums {

	OVERALL(1,"全局的角色")
	,AREA(2,"区域的角色")
	,ONLY_SELF(3,"只查看自己跟进的")  //根据电催、催收设置
	,SEE_CAR_BUSINESS(4,"只查看车贷业务")
	,SEE_HOURSE_BUSINESS(5,"只查看房贷业务")
	,SEE_FINANCE_BUSINESS(6,"财务跟单方式控制")
	;


	private Integer key; // 数据保存的值
	private String name; // 名称


	private SysRoleAreaTypeEnums(Integer key, String name ) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysRoleAreaTypeEnums d : SysRoleAreaTypeEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysRoleAreaTypeEnums getByKey(String key){
		for(SysRoleAreaTypeEnums d : SysRoleAreaTypeEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(SysRoleAreaTypeEnums d : SysRoleAreaTypeEnums.values()){
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
