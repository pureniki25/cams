package com.hongte.alms.base.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-02
 */
@ApiModel
@TableName("tb_sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId("user_id")
	@ApiModelProperty(required= true,value = "用户编号")
	private String userId;
    /**
     * 用户姓名
     */
	@TableField("user_name")
	@ApiModelProperty(required= true,value = "用户姓名")
	private String userName;
    /**
     * 所属机构编码
     */
	@TableField("org_code")
	@ApiModelProperty(required= true,value = "所属机构编码")
	private String orgCode;

	/**
	 * 信贷所属机构编码
	 */
	@TableField("xd_org_code")
	@ApiModelProperty(required= true,value = "信贷所属机构编码")
	private String xdOrgCode;
	
	/**
	 * 该用户权限最后修改时间
	 */
	@TableField("last_permission_time")
	@ApiModelProperty(required= true,value = "该用户权限最后修改时间")
	private Date lastPermissionTime;
	
	/**
	 * 该用户更新后权限数量
	 */
	@TableField("last_permission_rows")
	@ApiModelProperty(required= true,value = "该用户更新后权限数量")
	private Integer lastPermissionRows;
	
	/**
	 * 权限更新状态0成功1异常(异常要重新更新)
	 */
	@TableField("last_permission_status")
	@ApiModelProperty(required= true,value = "权限更新状态0成功1异常(异常要重新更新)")
	private Integer lastPermissionStatus;
	
	/**
	 * 用户状态:'用户状态: 0 正常 1禁用 2离职 4冻结 5锁定
	 */
	@TableField("user_status")
	@ApiModelProperty(required= true,value = "用户状态:'用户状态: 0 正常 1禁用 2离职 4冻结 5锁定")
	private Integer userStatus;
	
	public Date getLastPermissionTime() {
		return lastPermissionTime;
	}

	public void setLastPermissionTime(Date lastPermissionTime) {
		this.lastPermissionTime = lastPermissionTime;
	}

	public Integer getLastPermissionRows() {
		return lastPermissionRows;
	}

	public void setLastPermissionRows(Integer lastPermissionRows) {
		this.lastPermissionRows = lastPermissionRows;
	}

	public Integer getLastPermissionStatus() {
		return lastPermissionStatus;
	}

	public void setLastPermissionStatus(Integer lastPermissionStatus) {
		this.lastPermissionStatus = lastPermissionStatus;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getXdOrgCode() {
		return xdOrgCode;
	}

	public void setXdOrgCode(String xdOrgCode) {
		this.xdOrgCode = xdOrgCode;
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Override
	protected Serializable pkVal() {
		return this.userId;
	}

	@Override
	public String toString() {
		return "SysUser{" +
			", userId=" + userId +
			", userName=" + userName +
			", orgCode=" + orgCode +
			"}";
	}
}
