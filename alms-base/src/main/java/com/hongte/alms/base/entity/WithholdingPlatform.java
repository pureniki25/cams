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
 * 代扣平台表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
@ApiModel
@TableName("tb_withholding_platform")
public class WithholdingPlatform extends Model<WithholdingPlatform> {

    private static final long serialVersionUID = 1L;

    /**
     * 代扣平台ID
     */
    @TableId("platform_id")
	@ApiModelProperty(required= true,value = "代扣平台ID")
	private Integer platformId;
    /**
     * 代扣平台名称
     */
	@TableField("platform_name")
	@ApiModelProperty(required= true,value = "代扣平台名称")
	private String platformName;
    /**
     * 代扣平台状态，0：禁用，1：启用
     */
	@TableField("platform_status")
	@ApiModelProperty(required= true,value = "代扣平台状态，0：禁用，1：启用")
	private Integer platformStatus;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
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


	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Integer getPlatformStatus() {
		return platformStatus;
	}

	public void setPlatformStatus(Integer platformStatus) {
		this.platformStatus = platformStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return this.platformId;
	}

	@Override
	public String toString() {
		return "WithholdingPlatform{" +
			", platformId=" + platformId +
			", platformName=" + platformName +
			", platformStatus=" + platformStatus +
			", remark=" + remark +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
