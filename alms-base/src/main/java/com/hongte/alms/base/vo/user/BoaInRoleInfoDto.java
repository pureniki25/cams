package com.hongte.alms.base.vo.user;

import java.io.Serializable;

public class BoaInRoleInfoDto implements Serializable{

	private static final long serialVersionUID = 6974230573647549558L;

	long id;
	
    String roleCode;

    String roleName;

    String roleNameCn;

    String rOrgCode;

    String rOrgName;

    String rOrgNameCn;

    String rOrgType;

    String status;
    
    int delFlag;
    
    String app;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleNameCn() {
		return roleNameCn;
	}

	public void setRoleNameCn(String roleNameCn) {
		this.roleNameCn = roleNameCn;
	}

	public String getrOrgCode() {
		return rOrgCode;
	}

	public void setrOrgCode(String rOrgCode) {
		this.rOrgCode = rOrgCode;
	}

	public String getrOrgName() {
		return rOrgName;
	}

	public void setrOrgName(String rOrgName) {
		this.rOrgName = rOrgName;
	}

	public String getrOrgNameCn() {
		return rOrgNameCn;
	}

	public void setrOrgNameCn(String rOrgNameCn) {
		this.rOrgNameCn = rOrgNameCn;
	}

	public String getrOrgType() {
		return rOrgType;
	}

	public void setrOrgType(String rOrgType) {
		this.rOrgType = rOrgType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}
 
}
