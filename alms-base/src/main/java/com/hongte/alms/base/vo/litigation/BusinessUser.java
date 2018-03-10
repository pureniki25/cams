package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户信息<br>
 * <br>
 *
 * @author huweiqian
 */
public class BusinessUser implements Serializable{
	private static final long serialVersionUID = 2346818212775339799L;
	@ApiModelProperty(required = true, value = "用户ID")
	private String userId;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(required = true, value = "用户名称")
	private String name;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessUser other = (BusinessUser) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessUser [userId=" + userId + ", name=" + name + "]";
	}
}
