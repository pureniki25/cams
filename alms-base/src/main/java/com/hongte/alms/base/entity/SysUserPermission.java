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
 * 用户业务权限表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-05
 */
@ApiModel
@TableName("tb_sys_user_permission")
public class SysUserPermission extends Model<SysUserPermission> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务编号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 用户编号
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "用户编号")
	private String userId;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "SysUserPermission{" +
			", businessId=" + businessId +
			", userId=" + userId +
			"}";
	}
}
