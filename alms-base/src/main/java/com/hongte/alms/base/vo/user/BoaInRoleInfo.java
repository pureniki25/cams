package com.hongte.alms.base.vo.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoaInRoleInfo {

	long id;
	
    String roleCode;

    String roleName;

    String roleNameCn;

    String rOrgCode;

    String rOrgName;

    String rOrgNameCn;

    String rOrgType;

    String status;

    String createOperator;

    Date createdDatetime;

    String updateOperator;

    Date lastModifiedDatetime;
    
    Integer delFlag;
    
    String app;

    List<HtBoaInUser> users = new ArrayList<>(0);

    List<HtBoaInPosition> positions = new ArrayList<>(0);

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

	public String getCreateOperator() {
		return createOperator;
	}

	public void setCreateOperator(String createOperator) {
		this.createOperator = createOperator;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getUpdateOperator() {
		return updateOperator;
	}

	public void setUpdateOperator(String updateOperator) {
		this.updateOperator = updateOperator;
	}

	public Date getLastModifiedDatetime() {
		return lastModifiedDatetime;
	}

	public void setLastModifiedDatetime(Date lastModifiedDatetime) {
		this.lastModifiedDatetime = lastModifiedDatetime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public List<HtBoaInUser> getUsers() {
		return users;
	}

	public void setUsers(List<HtBoaInUser> users) {
		this.users = users;
	}

	public List<HtBoaInPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<HtBoaInPosition> positions) {
		this.positions = positions;
	}

}
