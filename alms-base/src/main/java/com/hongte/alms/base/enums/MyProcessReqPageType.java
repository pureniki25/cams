package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/1/24
 * 我的审批界面类型枚举定义
 */
public enum MyProcessReqPageType {

	WAIT_TO_APPROVE("waitToApprove","需要我审批的"),
	Approved("Approved","我已审批的"),
	SelfStart("SelfStart","我发起的"),
	CopySendToMe("CopySendToMe","抄送我的"),
	Search("Search","审批查询");

	private String key; // 数据保存的值
	private String name; // 名称

	private MyProcessReqPageType(String key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(MyProcessReqPageType d : MyProcessReqPageType.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static MyProcessReqPageType getByKey(String key){
		for(MyProcessReqPageType d : MyProcessReqPageType.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(MyProcessReqPageType d : MyProcessReqPageType.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public String getName() {
		return name;
	}



	public String getKey() {
		return key;
	}


}
