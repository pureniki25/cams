package com.hongte.alms.base.enums;

/**
 * @author zengkun
 * @since 2018/6/16
 * 标的平台类型枚举(业务平台类型保持与标的一致)
 */
public enum ProjPlatTypeEnum {

//	平台标志位：1，团贷网； 2，你我金融

	OFF_LINE(0,"线下出款"),
	TUANDAI(1,"团贷网"),
	NIWO_JR(2,"你我金融"),
	YUE_MONEY(3,"粤财"),
	BOHAI_TRUST(4,"渤海信托")
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProjPlatTypeEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProjPlatTypeEnum d : ProjPlatTypeEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProjPlatTypeEnum getByKey(String key){
		for(ProjPlatTypeEnum d : ProjPlatTypeEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProjPlatTypeEnum d : ProjPlatTypeEnum.values()){
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
