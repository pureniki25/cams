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
    List  positionCodes = new ArrayList();

    @ApiModelProperty(value = "角色编码", dataType = "string")
    List<BoaInRoleInfo>  roleCodes = new ArrayList<>();

    @ApiModelProperty(value = "工号", dataType = "string")
    String jobNumber;
    
    @ApiModelProperty(value = "系统编码", dataType = "string")
    String appCode;
    
    @ApiModelProperty(value = "系统名称", dataType = "string")
    String appName;
    
    //状态: 0 正常  2离职
    @ApiModelProperty(value = "用户状态", dataType = "string")
    String status;

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

	public List getPositionCodes() {
		return positionCodes;
	}

	public void setPositionCodes(List positionCodes) {
		this.positionCodes = positionCodes;
	}

	public List<BoaInRoleInfo> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<BoaInRoleInfo> roleCodes) {
		this.roleCodes = roleCodes;
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
