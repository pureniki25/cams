package com.hongte.alms.base.collection.enums;

/**
 * @author zengkun
 * @since 2018/1/24
 * 催收状态枚举定义
 */
public enum CollectionStatusEnum {


//	贷后状态，1：电催，50：催收中，100：已移交法务

	PHONE_STAFF(1,"电催"),
	COLLECTING(50,"催收中")
	,TO_LAW_WORK(100,"已移交法务")
	,COLSED(200,"已关闭")  //用户已还款则这个催收关闭, 这个关闭催收的触发时机，需要思考
	;

	private Integer key; // 数据保存的值
	private String name; // 名称

	private CollectionStatusEnum(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(CollectionStatusEnum d : CollectionStatusEnum.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static CollectionStatusEnum getByKey(String key){
		for(CollectionStatusEnum d : CollectionStatusEnum.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(CollectionStatusEnum d : CollectionStatusEnum.values()){
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
