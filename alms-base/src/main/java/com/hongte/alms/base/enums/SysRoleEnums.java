package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 系统角色枚举
 */
public enum SysRoleEnums {
    DH_CASHIER("DH_CASHIER", "出纳组"),
	DH_LOAN_COMMISSIONER ("DH_LOAN_COMMISSIONER","车辆拍卖申请专员"),
	HD_GENERAL_COMMISSIO ("HD_GENERAL_COMMISSIO"	,"综合管理专员"/*,false*/)      ,//HD_GENERAL_COMMISSIO
	HD_ASSET_COMMISSIONE ("HD_ASSET_COMMISSIONE"	,"资产管理专员"/*,true*/)      ,  //清算二
	HD_LIQ_COMMISSIONER	 ("HD_LIQ_COMMISSIONER"	,"电催专员")              ,			//清算一
	HD_EARE_ASSET_MANAGE ("HD_EARE_ASSET_MANAGE"	,"区域资产管理主管")  ,//HD_EARE_ASSET_MANAGE
	HD_VISIT_LEADER		 ("HD_VISIT_LEADER"	,"贷后回访组长")              ,
	DH_PHONE_URG_LEADER	 ("DH_PHONE_URG_LEADER"	,"电催组长")              ,
	DH_PLOAN_MANAGER	 ("DH_PLOAN_MANAGER"	,"贷后管理主管")         ,//  陈晓敏 DH_PLOAN_MANAGER
	DH_LIQ_MANAGER  	 ("DH_LIQ_MANAGER"	,"资产管理副经理")               ,
	DH_CENTER_DIRECTOR 	 ("DH_CENTER_DIRECTOR"	,"贷后管理中心总监")      ,
	DH_0001  			 ("DH_0001"	,"贷后管理员")                        ,
	DH_COMPANY_LEADER  	 ("DH_COMPANY_LEADER"	,"分公司负责人")          ,
	DH_DATA_ANALYSIS 	 ("DH_DATA_ANALYSIS"	,"数据分析专员")          ,
	DH_YC_FINANCE           ("DH_YC finance"	,"粤财出纳")          ,   
	DH_CAR_TELLER 	 ("DH_CAR_TELLER"	,"车贷业务车贷出纳人员")  ,
	DH_HOUSE_TELLER 	 ("DH_HOUSE_TELLER"	,"房贷业务出纳人员") ,
	DH_GENERAL_APPROVE 	 ("DH_GENERAL_APPROVE"	,"综合岗审批") ;
	


	private String key; // 数据保存的值
	private String name; // 名称
//	private boolean isAreaRole;


	private SysRoleEnums(String key, String name/*,boolean isAreaRole*/) {
		this.name = name;
		this.key = key;
//		this.isAreaRole  = isAreaRole;
	}


	public static String nameOf(Integer key){
		for(SysRoleEnums d : SysRoleEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static SysRoleEnums getByKey(String key){
		for(SysRoleEnums d : SysRoleEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(SysRoleEnums d : SysRoleEnums.values()){
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
