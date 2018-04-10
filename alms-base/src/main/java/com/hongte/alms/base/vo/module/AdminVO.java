package com.hongte.alms.base.vo.module;

import java.io.Serializable;

public class AdminVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String username;
	private String businessId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return "AdminVO [userId=" + userId + ", username=" + username + ", businessId=" + businessId + "]";
	}

}
