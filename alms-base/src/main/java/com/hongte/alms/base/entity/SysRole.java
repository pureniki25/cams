package com.hongte.alms.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

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

    @TableField("role_code")
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
    @TableField("page_type")
    @ApiModelProperty(required = true, value = "页面类型")
    private Integer pageType;
    
    @TableField("data_type")
    @ApiModelProperty(required = true, value = "数据权限类型:0所有1正在逾期中")
    private Integer dataType;
    
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(required = true, value = "页面类型")
    private Integer id;

	public String getRoleCode() {
		return roleCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
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

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
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
