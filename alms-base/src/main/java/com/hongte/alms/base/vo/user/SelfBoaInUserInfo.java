package com.hongte.alms.base.vo.user;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: SelfBoaInUserInfo  
 * @Description: 用户权限dto类
 * @author liuzq  
 * @date 2018年7月12日    
 */
@ApiModel(value = "SelfBoaInUserInfo", description = "用户个人信息")
public class SelfBoaInUserInfo {

    public SelfBoaInUserInfo() {
	}

	public SelfBoaInUserInfo(String userId, String userName, String email, String idNo, String mobile, String orgCode,
			String orgName, String orgNameCh, String rootOrgCode, String rootOrgName, String rootOrgNameCh,
			String orgPath, String orgType, List<String> positionCodes, List<String> positionNames,
			List<String> positionNameChs, List<String> roleCodes, List<String> roleNames, List<String> roleNameChs,
			String jobNumber, String appCode, String appName, String status) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.idNo = idNo;
		this.mobile = mobile;
		this.orgCode = orgCode;
		this.orgName = orgName;
		this.orgNameCh = orgNameCh;
		this.rootOrgCode = rootOrgCode;
		this.rootOrgName = rootOrgName;
		this.rootOrgNameCh = rootOrgNameCh;
		this.orgPath = orgPath;
		this.orgType = orgType;
		this.positionCodes = positionCodes;
		this.positionNames = positionNames;
		this.positionNameChs = positionNameChs;
		this.roleCodes = roleCodes;
		this.roleNames = roleNames;
		this.roleNameChs = roleNameChs;
		this.jobNumber = jobNumber;
		this.appCode = appCode;
		this.appName = appName;
		this.status = status;
	}

	@ApiModelProperty(value = "用户ID", dataType = "string")
    String userId;

    @ApiModelProperty(value = "用户名", dataType = "string")
    String userName;

    @ApiModelProperty(value = "电子邮箱", dataType = "string")
    String email;

    @ApiModelProperty(value = "身份证号", dataType = "string")
    String idNo;

    @ApiModelProperty(value = "手机号", dataType = "string")
    String mobile;

    @ApiModelProperty(value = "机构编码", dataType = "string")
    String orgCode;

    @ApiModelProperty(value = "机构名称", dataType = "string")
    String orgName;

    @ApiModelProperty(value = "机构中文名称", dataType = "string")
    String orgNameCh;

    @ApiModelProperty(value = "根机构编码", dataType = "string")
    String rootOrgCode;

    @ApiModelProperty(value = "根机构名称", dataType = "string")
    String rootOrgName;

    @ApiModelProperty(value = "根机构中文名称", dataType = "string")
    String rootOrgNameCh;

    @ApiModelProperty(value = "机构路径", dataType = "string")
    String orgPath;

    @ApiModelProperty(value = "机构类型", dataType = "string")
    String orgType;

    @ApiModelProperty(value = "岗位编码", dataType = "string")
    List<String> positionCodes = new ArrayList<String>();

    @ApiModelProperty(value = "岗位名称", dataType = "string")
    List<String> positionNames = new ArrayList<String>();

    @ApiModelProperty(value = "岗位名称中文", dataType = "string")
    List<String> positionNameChs = new ArrayList<String>();

    @ApiModelProperty(value = "角色编码", dataType = "string")
    List<String> roleCodes = new ArrayList<String>();

    @ApiModelProperty(value = "角色英文名", dataType = "string")
    List<String> roleNames = new ArrayList<String>();

    @ApiModelProperty(value = "角色中文名", dataType = "string")
    List<String> roleNameChs = new ArrayList<String>();

    @ApiModelProperty(value = "工号", dataType = "string")
    String jobNumber;
    
    @ApiModelProperty(value = "系统编码", dataType = "string")
    String appCode;
    
    @ApiModelProperty(value = "系统名称", dataType = "string")
    String appName;
    
  //状态: 0 正常  2离职
    @ApiModelProperty(value = "用户状态", dataType = "string")
    String status;
    public SelfBoaInUserInfo(String userId, String userName, String email,
            String idNo, String mobile, String orgCode, String orgName, String orgNameCh,
            String rootOrgCode, String rootOrgName, String rootOrgNameCh, String orgPath,
            String orgType,String jobNumber) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.idNo = idNo;
        this.mobile = mobile;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.orgNameCh = orgNameCh;
        this.rootOrgCode = rootOrgCode;
        this.rootOrgName = rootOrgName;
        this.rootOrgNameCh = rootOrgNameCh;
        this.orgPath = orgPath;
        this.orgType = orgType;
        this.jobNumber = jobNumber;
    }
    
    public SelfBoaInUserInfo(String userId, String userName, String email,
            String idNo, String mobile, String orgCode, String jobNumber,String appCode,String status ) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.idNo = idNo;
        this.mobile = mobile;
        this.orgCode = orgCode;
        this.jobNumber = jobNumber;
        this.appCode = appCode;
        this.status = status;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgNameCh() {
		return orgNameCh;
	}

	public void setOrgNameCh(String orgNameCh) {
		this.orgNameCh = orgNameCh;
	}

	public String getRootOrgCode() {
		return rootOrgCode;
	}

	public void setRootOrgCode(String rootOrgCode) {
		this.rootOrgCode = rootOrgCode;
	}

	public String getRootOrgName() {
		return rootOrgName;
	}

	public void setRootOrgName(String rootOrgName) {
		this.rootOrgName = rootOrgName;
	}

	public String getRootOrgNameCh() {
		return rootOrgNameCh;
	}

	public void setRootOrgNameCh(String rootOrgNameCh) {
		this.rootOrgNameCh = rootOrgNameCh;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public List<String> getPositionCodes() {
		return positionCodes;
	}

	public void setPositionCodes(List<String> positionCodes) {
		this.positionCodes = positionCodes;
	}

	public List<String> getPositionNames() {
		return positionNames;
	}

	public void setPositionNames(List<String> positionNames) {
		this.positionNames = positionNames;
	}

	public List<String> getPositionNameChs() {
		return positionNameChs;
	}

	public void setPositionNameChs(List<String> positionNameChs) {
		this.positionNameChs = positionNameChs;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public List<String> getRoleNameChs() {
		return roleNameChs;
	}

	public void setRoleNameChs(List<String> roleNameChs) {
		this.roleNameChs = roleNameChs;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
