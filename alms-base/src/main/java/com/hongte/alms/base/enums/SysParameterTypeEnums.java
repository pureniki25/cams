package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 系统参数表参数类型枚举
 */
public enum SysParameterTypeEnums {

	REPAY_STATUS(1,"还款状态"),
	COLLECTION_STATUS(2,"贷后状态"),
	COLLECTION_LEVERS(3,"催收级别"),
	AREA_LEVERS(4,"区域级别"),
	COLLECTION_FOLLOW_STATUS(5,"贷后跟踪状态"),
	REPAYMENT_TYPE(6,"还款方式"),
	BORROW_LIMIT_UNIT(7,"借款期限单位"),
	DERATE_TYPE(8,"申请减免费用项"),
	BORROW_RATE_UNIT(9,"借款利率类型"),
	COL_TYPE(10,"移交催收类型"),
	SMS_TYPE(11,"短信类型"),
	CAR_STATUS(12,"车辆状态");

//	borrowLimitUnit

	private Integer key; // 数据保存的值
	private String name; // 名称

	private SysParameterTypeEnums(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysParameterTypeEnums d : SysParameterTypeEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysParameterTypeEnums getByKey(String key){
		for(SysParameterTypeEnums d : SysParameterTypeEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(SysParameterTypeEnums d : SysParameterTypeEnums.values()){
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
