package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 部门信息
 * @author huweiqian
 *
 */
public class BusinessCompany implements Serializable{
	private static final long serialVersionUID = -8351731953764736955L;

	/**
     *部门Id
     */
    @ApiModelProperty(required= true,value = "部门Id")
    private String deptId;

    /**
     *部门名称
     */
    @ApiModelProperty(required= true,value = "部门名称")
    private String deptName;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
		result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
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
		BusinessCompany other = (BusinessCompany) obj;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		if (deptName == null) {
			if (other.deptName != null)
				return false;
		} else if (!deptName.equals(other.deptName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessCompany [deptId=" + deptId + ", deptName=" + deptName + "]";
	}

}
