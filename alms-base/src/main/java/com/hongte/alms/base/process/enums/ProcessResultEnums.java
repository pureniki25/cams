package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/2/9
 * 流程结果枚举
 */
public enum ProcessResultEnums {


//	流程结果，1：通过， 2：不通过',
	PASS(1,"通过"),
	REFUSE(2,"不通过");


//	(0:运行中,1:开始,2:结束,3:注销)

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessResultEnums(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessResultEnums d : ProcessResultEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessResultEnums getByKey(String key){
		for(ProcessResultEnums d : ProcessResultEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessResultEnums d : ProcessResultEnums.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public String getName() {
		return name;
	}



	public int getKey() {
		return key;
	}


}
