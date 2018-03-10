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
 * 系统权限表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@ApiModel
@TableName("tb_sys_role")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    @TableId("role_code")
	@ApiModelProperty(required= true,value = "")
	private String roleCode;
	@TableField("role_name")
	@ApiModelProperty(required= true,value = "")
	private String roleName;
    /**
     * 权限区域控制类型
     */
	@TableField("role_area_type")
	@ApiModelProperty(required= true,value = "权限区域控制类型")
	private Integer roleAreaType;


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

	public Integer getRoleAreaType() {
		return roleAreaType;
	}

	public void setRoleAreaType(Integer roleAreaType) {
		this.roleAreaType = roleAreaType;
	}

	@Override
	protected Serializable pkVal() {
		return this.roleCode;
	}

	@Override
	public String toString() {
		return "SysRole{" +
			", roleCode=" + roleCode +
			", roleName=" + roleName +
			", roleAreaType=" + roleAreaType +
			"}";
	}
}
