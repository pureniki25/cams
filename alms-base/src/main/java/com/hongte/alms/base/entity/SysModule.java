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
 * 菜单信息表
 * </p>
 *
 * @author 黄咏康
 * @since 2018-01-18
 */
@ApiModel
@TableName("tb_sys_module")
public class SysModule extends Model<SysModule> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId("module_id")
	@ApiModelProperty(required= true,value = "菜单ID")
	private String moduleId;
    /**
     * 菜单名称
     */
	@TableField("module_name")
	@ApiModelProperty(required= true,value = "菜单名称")
	private String moduleName;
    /**
     * 菜单URL地址
     */
	@TableField("module_url")
	@ApiModelProperty(required= true,value = "菜单URL地址")
	private String moduleUrl;
    /**
     * 父级菜单ID
     */
	@TableField("module_pid")
	@ApiModelProperty(required= true,value = "父级菜单ID")
	private String modulePid;
    /**
     * 菜单级别
     */
	@TableField("module_level")
	@ApiModelProperty(required= true,value = "菜单级别")
	private Integer moduleLevel;
    /**
     * 菜单描述
     */
	@TableField("module_desc")
	@ApiModelProperty(required= true,value = "菜单描述")
	private String moduleDesc;
    /**
     * 是否启用，0：禁用，1：启用
     */
	@TableField("module_status")
	@ApiModelProperty(required= true,value = "是否启用，0：禁用，1：启用")
	private Boolean moduleStatus;
    /**
     * 菜单设备标识：PC，APP
     */
	@TableField("device_type")
	@ApiModelProperty(required= true,value = "菜单设备标识：PC，APP")
	private String deviceType;
    /**
     * 菜单排序
     */
	@TableField("module_order")
	@ApiModelProperty(required= true,value = "菜单排序")
	private Integer moduleOrder;
    /**
     * 菜单图标(layerui图标)
     */
	@TableField("module_icon")
	@ApiModelProperty(required= true,value = "菜单图标(layerui图标)")
	private String moduleIcon;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;


	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public String getModulePid() {
		return modulePid;
	}

	public void setModulePid(String modulePid) {
		this.modulePid = modulePid;
	}

	public Integer getModuleLevel() {
		return moduleLevel;
	}

	public void setModuleLevel(Integer moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	public Boolean getModuleStatus() {
		return moduleStatus;
	}

	public void setModuleStatus(Boolean moduleStatus) {
		this.moduleStatus = moduleStatus;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getModuleOrder() {
		return moduleOrder;
	}

	public void setModuleOrder(Integer moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	public String getModuleIcon() {
		return moduleIcon;
	}

	public void setModuleIcon(String moduleIcon) {
		this.moduleIcon = moduleIcon;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.moduleId;
	}

	@Override
	public String toString() {
		return "SysModule{" +
			", moduleId=" + moduleId +
			", moduleName=" + moduleName +
			", moduleUrl=" + moduleUrl +
			", modulePid=" + modulePid +
			", moduleLevel=" + moduleLevel +
			", moduleDesc=" + moduleDesc +
			", moduleStatus=" + moduleStatus +
			", deviceType=" + deviceType +
			", moduleOrder=" + moduleOrder +
			", moduleIcon=" + moduleIcon +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
