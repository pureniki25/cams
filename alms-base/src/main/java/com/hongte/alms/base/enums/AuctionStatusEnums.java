package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/2/1
 * 系统角色枚举
 */
public enum AuctionStatusEnums {
	AUDIT ("02","提交审核"),
	DRAFT ("01"	,"保存草稿"/*,false*/)      ,
	CANCEL 	 ("03"	,"撤销")          ;
	


	private String key; // 数据保存的值
	private String name; // 名称
//	private boolean isAreaRole;


	private AuctionStatusEnums(String key, String name/*,boolean isAreaRole*/) {
		this.name = name;
		this.key = key;
//		this.isAreaRole  = isAreaRole;
	}


	public static String nameOf(Integer key){
		for(AuctionStatusEnums d : AuctionStatusEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static AuctionStatusEnums getByKey(String key){
		for(AuctionStatusEnums d : AuctionStatusEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static String keyOf(String name){
		for(AuctionStatusEnums d : AuctionStatusEnums.values()){
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
