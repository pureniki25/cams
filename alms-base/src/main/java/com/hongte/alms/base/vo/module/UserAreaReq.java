/**
 * 
 */
package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;

/**
 * @author 王继光
 * 2018年3月20日 上午10:08:45
 */
public class UserAreaReq extends PageRequest{

	private String userId ;
	private String userName ;
	private String areaId ;
	private String areaLevel ;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the areaId
	 */
	public String getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the areaLevel
	 */
	public String getAreaLevel() {
		return areaLevel;
	}
	/**
	 * @param areaLevel the areaLevel to set
	 */
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
}
