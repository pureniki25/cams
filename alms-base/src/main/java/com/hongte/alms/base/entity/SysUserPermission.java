package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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

	/**
	 * 页面类型：1.贷后管理页，2.财务管理页面，3.减免管理页面
	 */
	@TableField("page_type")
	@ApiModelProperty(required= true,value = "页面类型：1.贷后管理页，2.财务管理页面，3.减免管理页面")
	private Integer pageType;
	/**
	 * 自增主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "自增主键")
	private Integer id;


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

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
