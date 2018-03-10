package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 系统参数表参数类型枚举
 */
public enum SysParameterStautsEnums {

//	'状态，0：无效，1：有效',

	IN_ACTIVE(0,"无效")
	,ACTIVE(1,"有效")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private SysParameterStautsEnums(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysParameterStautsEnums d : SysParameterStautsEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysParameterStautsEnums getByKey(String key){
		for(SysParameterStautsEnums d : SysParameterStautsEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(SysParameterStautsEnums d : SysParameterStautsEnums.values()){
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
