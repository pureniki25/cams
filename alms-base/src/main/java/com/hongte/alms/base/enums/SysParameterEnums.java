package com.hongte.alms.base.enums;

/**
 * @author czs
 * @since 2018/6/1
 * 系统参数表参数类型枚举
 */
public enum SysParameterEnums {

	
	REPAY_DAYS("autoRepayDays","筛选自动代扣记录天数范围"),
	FORGIVE_DAYS("forgiveDay","代扣线下滞纳金宽限期"),
	BANK_CHANNEL("bankChannel","银行代扣子渠道"),
	APPLY_SQUENCE("applySquence","逾期类减免顺序"),
	NOT_OVER_APPLY_SQUENCE_AFTER28("notOverApplySquence28after","非逾期类减免顺序6月28日之后"),
	SLAVE_PROJECT_BUSINESS_TYPE("e69955f4-db17-11e8-b8f2-0242ac110003", "共借标业务类型汇总"),
	PLATFORM_PROFIT_ID("ac000c3f-e16e-11e8-b060-0242ac110003", "平台分润账户ID"),
	REPAY_RECHARGE_EXCEPTION_MONITOR("cb9f8643-e8aa-11e8-80f8-0242ac110005", "合规化异常监控"),
	BRANCH_COMPANY_PROFIT_USER_ID_MAPPING("branch_company_profit_user_id_mapping", "分公司分润ID映射"),
	;
	

//	borrowLimitUnit

	private String key; // 数据保存的值
	private String name; // 名称

	private SysParameterEnums(String key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(SysParameterEnums d : SysParameterEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysParameterEnums getByKey(String key){
		for(SysParameterEnums d : SysParameterEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(SysParameterEnums d : SysParameterEnums.values()){
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
