package com.hongte.alms.base.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时器配置表
 * @todo TODO
 */
public class JobConfigEnums {
	
	/**
	 * @todo TODO
	 * 定时器配置
	 */
	public enum JobConfigType {
		
		SET_USER_PSERMISION("设置用户可访问的业务定时任务","setUserPermissionTask")
		,SET_BUSINESS_COL("设置业务移交催收","setBusinessColTask")
		,ADD_PROJECT_TRACT("添加项目追踪","addProjectTract")
		;
		
		
		private String name;// 用户类型
		private String value;//对应数据库值

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		JobConfigType(String name, String val) {
			this.name = name;
			this.value = val;
		}
		
	    public static Map<String, String> getMap() {
	  		Map<String, String> statusMap = new HashMap<String, String>();
	  		for (JobConfigType c : JobConfigType.values()) {
	  			statusMap.put(c.getValue(), c.getName());
	  		}
	  		return statusMap;
	  	}
	}
	
	/**
	 * @todo TODO
	 * 定时器开关配置
	 */
	public enum JobLockType {
		
		UNLOCK("开",0),
		LOCK("关",1);
		
		private String name;// 用户类型
		private int value;//对应数据库值

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		JobLockType(String name, int val) {
			this.name = name;
			this.value = val;
		}
		
	    public static Map<Integer, String> getMap() {
	  		Map<Integer, String> statusMap = new HashMap<Integer, String>();
	  		for (JobLockType c : JobLockType.values()) {
	  			statusMap.put(c.getValue(), c.getName());
	  		}
	  		return statusMap;
	  	}
	}

	/**
	 * 时间间隔类型
	 */
	public enum TimeIntervalType {

//		0：按小时算，1：按分钟算，2：按秒算

		BY_HOUR("按小时算",0)
		,BY_MINUTE("按分钟算",1)
		,BY_SECOND("按秒算",2)
		,BY_DAY("按填算",3)
		,NONE("不设置时间间隔",4)
		;



		private String name;// 用户类型
		private int value;//对应数据库值

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		TimeIntervalType(String name, int val) {
			this.name = name;
			this.value = val;
		}

		public static Map<Integer, String> getMap() {
			Map<Integer, String> statusMap = new HashMap<Integer, String>();
			for (TimeIntervalType c : TimeIntervalType.values()) {
				statusMap.put(c.getValue(), c.getName());
			}
			return statusMap;
		}
	}


	/**
	 * 时间间隔类型
	 */
	public enum ActiveEnum {

//		0：按小时算，1：按分钟算，2：按秒算

		AVTIVE("有效",1)
		,INACTIVE("无效",0)
		;



		private String name;// 用户类型
		private int value;//对应数据库值

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		ActiveEnum(String name, int val) {
			this.name = name;
			this.value = val;
		}

		public static Map<Integer, String> getMap() {
			Map<Integer, String> statusMap = new HashMap<Integer, String>();
			for (ActiveEnum c : ActiveEnum.values()) {
				statusMap.put(c.getValue(), c.getName());
			}
			return statusMap;
		}
	}



}
