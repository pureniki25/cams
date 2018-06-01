package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
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
 * 代扣平台银行限额表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-29
 */
@ApiModel
@TableName("tb_sys_bank_limit")
public class SysBankLimit extends Model<SysBankLimit> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，GUID
     */
    @TableId("limit_id")
	@ApiModelProperty(required= true,value = "主键ID，GUID")
	private String limitId;
    /**
     * 银行编码，tb_sys_bank表的外键
     */
	@TableField("bank_code")
	@ApiModelProperty(required= true,value = "银行编码，tb_sys_bank表的外键")
	private String bankCode;
    /**
     * 代扣平台ID，tb_withholding_platform表的外键
     */
	@TableField("platform_id")
	@ApiModelProperty(required= true,value = "代扣平台ID，tb_withholding_platform表的外键")
	private Integer platformId;
    /**
     * 是否存在单笔代扣限额，0：否，1：是
     */
	@TableField("has_once_limit")
	@ApiModelProperty(required= true,value = "是否存在单笔代扣限额，0：否，1：是")
	private Integer hasOnceLimit;
    /**
     * 单笔代扣限额(元)，若不存在单笔代扣限额，则为空
     */
	@TableField("once_limit")
	@ApiModelProperty(required= true,value = "单笔代扣限额(元)，若不存在单笔代扣限额，则为空")
	private BigDecimal onceLimit;
    /**
     * 是否存在单日代扣限额，0：否，1：是
     */
	@TableField("has_day_limit")
	@ApiModelProperty(required= true,value = "是否存在单日代扣限额，0：否，1：是")
	private Integer hasDayLimit;
    /**
     * 单日代扣限额(元)，若不存在单日代扣限额，则为空
     */
	@TableField("day_limit")
	@ApiModelProperty(required= true,value = "单日代扣限额(元)，若不存在单日代扣限额，则为空")
	private BigDecimal dayLimit;
    /**
     * 是否启用此代扣通道，0：关闭，1：启用
     */
	@ApiModelProperty(required= true,value = "是否启用此代扣通道，0：关闭，1：启用")
	private Integer status;
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
    /**
     * 是否存在单月代扣限额，0：否，1：是
     */
	@TableField("has_month_limit")
	@ApiModelProperty(required= true,value = "是否存在单月代扣限额，0：否，1：是")
	private Integer hasMonthLimit;
    /**
     * 单月代扣限额(元)，若不存在单月代扣限额，则为空
     */
	@TableField("month_limit")
	@ApiModelProperty(required= true,value = "单月代扣限额(元)，若不存在单月代扣限额，则为空")
	private BigDecimal monthLimit;


	public String getLimitId() {
		return limitId;
	}

	public void setLimitId(String limitId) {
		this.limitId = limitId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getHasOnceLimit() {
		return hasOnceLimit;
	}

	public void setHasOnceLimit(Integer hasOnceLimit) {
		this.hasOnceLimit = hasOnceLimit;
	}

	public BigDecimal getOnceLimit() {
		return onceLimit;
	}

	public void setOnceLimit(BigDecimal onceLimit) {
		this.onceLimit = onceLimit;
	}

	public Integer getHasDayLimit() {
		return hasDayLimit;
	}

	public void setHasDayLimit(Integer hasDayLimit) {
		this.hasDayLimit = hasDayLimit;
	}

	public BigDecimal getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(BigDecimal dayLimit) {
		this.dayLimit = dayLimit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getHasMonthLimit() {
		return hasMonthLimit;
	}

	public void setHasMonthLimit(Integer hasMonthLimit) {
		this.hasMonthLimit = hasMonthLimit;
	}

	public BigDecimal getMonthLimit() {
		return monthLimit;
	}

	public void setMonthLimit(BigDecimal monthLimit) {
		this.monthLimit = monthLimit;
	}

	@Override
	protected Serializable pkVal() {
		return this.limitId;
	}

	@Override
	public String toString() {
		return "SysBankLimit{" +
			", limitId=" + limitId +
			", bankCode=" + bankCode +
			", platformId=" + platformId +
			", hasOnceLimit=" + hasOnceLimit +
			", onceLimit=" + onceLimit +
			", hasDayLimit=" + hasDayLimit +
			", dayLimit=" + dayLimit +
			", status=" + status +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", hasMonthLimit=" + hasMonthLimit +
			", monthLimit=" + monthLimit +
			"}";
	}
}
