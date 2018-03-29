package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/2/9
 * 流程 单步审核结果枚举
 */
public enum ProcessApproveResult {


//	流程结果，1：通过， 0：不通过',
	PASS(1,"通过"),
	REFUSE(0,"不通过");


//	(0:运行中,1:开始,2:结束,3:注销)

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessApproveResult(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessApproveResult d : ProcessApproveResult.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessApproveResult getByKey(String key){
		for(ProcessApproveResult d : ProcessApproveResult.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessApproveResult d : ProcessApproveResult.values()){
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
