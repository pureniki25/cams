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
		,Cal_OVERDAYS_LATEFEE("计算还款计划表的逾期天数和滞纳金","calOverdaysLatefee")
		,TRANS_COL_TRACK_LOG("同步历史贷后跟踪记录","transColTrackLog")
		,TRANS_STAFF_SET("同步电催/催收信息","transStaffSet")
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


		public static String nameOf(String name){
			for(JobConfigType d : JobConfigType.values()){
				if(d.value.equals(name)){
					return d.name;
				}
			}
			return null;

		}

		public static JobConfigType getByKey(Integer key){
			for(JobConfigType d : JobConfigType.values()){
				if(d.value.equals(key)){
					return d;
				}
			}
			return null;

		}

		public static String keyOf(String name){
			for(JobConfigType d : JobConfigType.values()){
				if(d.name.equals(name)){
					return d.value;
				}
			}
			return null;

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
		private Integer value;//对应数据库值

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

		public static String nameOf(String name){
			for(JobLockType d : JobLockType.values()){
				if(d.value.equals(name)){
					return d.name;
				}
			}
			return null;

		}

		public static JobLockType getByKey(Integer key){
			for(JobLockType d : JobLockType.values()){
				if(d.value.equals(key)){
					return d;
				}
			}
			return null;

		}

		public static Integer keyOf(String name){
			for(JobLockType d : JobLockType.values()){
				if(d.name.equals(name)){
					return d.value;
				}
			}
			return null;

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

		BY_HOUR("按小时",0)
		,BY_MINUTE("按分钟",1)
		,BY_SECOND("按秒",2)
		,BY_DAY("按天",3)
		,NONE("无限制",4)
		;



		private String name;// 用户类型
		private Integer value;//对应数据库值

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


		public static String nameOf(Integer key){
			for(TimeIntervalType d : TimeIntervalType.values()){
				if(d.value.equals(key)){
					return d.name;
				}
			}
			return null;

		}

		public static TimeIntervalType getByKey(Integer key){
			for(TimeIntervalType d : TimeIntervalType.values()){
				if(d.value.equals(key)){
					return d;
				}
			}
			return null;

		}

		public static Integer keyOf(String name){
			for(TimeIntervalType d : TimeIntervalType.values()){
				if(d.name.equals(name)){
					return d.value;
				}
			}
			return null;

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
	 * 定时器设置，是否有效枚举
	 */
	public enum ActiveEnum {

//		1：有效，0：无效

		AVTIVE("有效",1)
		,INACTIVE("无效",0)
		;



		private String name;// 用户类型
		private Integer value;//对应数据库值

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



		public static String nameOf(Integer key){
			for(ActiveEnum d : ActiveEnum.values()){
				if(d.value.equals(key)){
					return d.name;
				}
			}
			return null;

		}

		public static ActiveEnum getByKey(Integer key){
			for(ActiveEnum d : ActiveEnum.values()){
				if(d.value.equals(key)){
					return d;
				}
			}
			return null;

		}

		public static Integer keyOf(String name){
			for(ActiveEnum d : ActiveEnum.values()){
				if(d.name.equals(name)){
					return d.value;
				}
			}
			return null;

		}

	}



}
