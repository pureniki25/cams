package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 用户管理的区域表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@ApiModel
@TableName("tb_sys_user_area")
public class SysUserArea extends Model<SysUserArea> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(required= true,value = "主键")
	private String id;
    /**
     * 用户编号
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "用户编号")
	private String userId;
    /**
     * 用户管理的组织机构编码
     */
	@TableField("org_code")
	@ApiModelProperty(required= true,value = "用户管理的组织机构编码")
	private String orgCode;
    /**
     * 顺序号
     */
	@ApiModelProperty(required= true,value = "顺序号")
	private Integer sequence;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysUserArea{" +
			", id=" + id +
			", userId=" + userId +
			", orgCode=" + orgCode +
			", sequence=" + sequence +
			"}";
	}
}
