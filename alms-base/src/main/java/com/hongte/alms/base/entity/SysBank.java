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
 * 注册存管银行配置表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-10
 */
@ApiModel
@TableName("tb_sys_bank")
public class SysBank extends Model<SysBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 银行编码
     */
    @TableId("bank_code")
	@ApiModelProperty(required= true,value = "银行编码")
	private String bankCode;
    /**
     * 银行全称
     */
	@TableField("bank_name")
	@ApiModelProperty(required= true,value = "银行全称")
	private String bankName;
    /**
     * 是否支持企业注册存管，0：否，1：是
     */
	@TableField("enterprise_support")
	@ApiModelProperty(required= true,value = "是否支持企业注册存管，0：否，1：是")
	private Integer enterpriseSupport;
    /**
     * 是否支持个人注册存管，0：否，1：是
     */
	@TableField("personal_support")
	@ApiModelProperty(required= true,value = "是否支持个人注册存管，0：否，1：是")
	private Integer personalSupport;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getEnterpriseSupport() {
		return enterpriseSupport;
	}

	public void setEnterpriseSupport(Integer enterpriseSupport) {
		this.enterpriseSupport = enterpriseSupport;
	}

	public Integer getPersonalSupport() {
		return personalSupport;
	}

	public void setPersonalSupport(Integer personalSupport) {
		this.personalSupport = personalSupport;
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
		return this.bankCode;
	}

	@Override
	public String toString() {
		return "SysBank{" +
			", bankCode=" + bankCode +
			", bankName=" + bankName +
			", enterpriseSupport=" + enterpriseSupport +
			", personalSupport=" + personalSupport +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
