package com.hongte.alms.base.process.enums;

import com.hongte.alms.base.process.vo.ProcessStatusVo;

import java.util.*;

/**
 * @author zengkun
 * @since 2018/2/6
 * 流程状态枚举
 */
public enum ProcessStatusEnums {

	NEW(-1,"新建"),
	RUNNING(0,"运行中"),
	BEGIN(1,"开始"),
	END(2,"结束"),
	CNACL(3,"注销");

//	(0:运行中,1:开始,2:结束,3:注销)

	private Integer key; // 数据保存的值
	private String name; // 名称

	private ProcessStatusEnums(int key, String name) {
		this.name = name;
		this.key = key;
	}


	public static String nameOf(Integer key){
		for(ProcessStatusEnums d : ProcessStatusEnums.values()){
			if(d.key.equals(key)){
				return d.name;
			}
		}
		return null;

	}

	public static ProcessStatusEnums getByKey(String key){
		for(ProcessStatusEnums d : ProcessStatusEnums.values()){
			if(d.key.equals(key)){
				return d;
			}
		}
		return null;

	}

	public static Integer keyOf(String name){
		for(ProcessStatusEnums d : ProcessStatusEnums.values()){
			if(d.name.equals(name)){
				return d.key;
			}
		}
		return null;

	}

	public static List<ProcessStatusVo> getProcessStatusList(){

		List<ProcessStatusVo> list = new LinkedList<>();
		for(ProcessStatusEnums d : ProcessStatusEnums.values()){
			ProcessStatusVo vo = new ProcessStatusVo();
			vo.setStatusKey(d.getKey());
			vo.setStatusName(d.getName());
			list.add(vo);
		}
		return list;
	}

	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}

	public int getKey() {
		return key;
	}

//	public void setKey(int key) {
//		this.key = key;
//	}




}
