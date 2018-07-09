package com.hongte.alms.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
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
    @TableField("role_area_method")
    @ApiModelProperty(required = true, value = "区域性取值方式")
    private Integer roleAreaMethod;


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

    public Integer getRoleAreaMethod() {
        return roleAreaMethod;
    }

    public void setRoleAreaMethod(Integer roleAreaMethod) {
        this.roleAreaMethod = roleAreaMethod;
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
