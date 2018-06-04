package com.hongte.alms.base.enums;

/**
 * @author czs
 * @since 2018/6/1
 * 系统参数表参数类型枚举
 */
public enum SysParameterRepaydaysEnums {

	
	REPAY_DAYS("autoRepayDays","筛选自动代扣记录天数范围");

//	borrowLimitUnit

	private String key; // 数据保存的值
	private String name; // 名称

	private SysParameterRepaydaysEnums(String key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysParameterRepaydaysEnums d : SysParameterRepaydaysEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysParameterRepaydaysEnums getByKey(String key){
		for(SysParameterRepaydaysEnums d : SysParameterRepaydaysEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(SysParameterRepaydaysEnums d : SysParameterRepaydaysEnums.values()){
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
