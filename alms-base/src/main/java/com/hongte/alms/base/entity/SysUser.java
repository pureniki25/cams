package com.hongte.alms.base.entity;

import java.io.Serializable;

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
