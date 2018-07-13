package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 还款结清日志记录表
 * </p>
 *
 * @author lxq
 * @since 2018-07-11
 */
@ApiModel
@TableName("tb_repayment_settle_log")
public class RepaymentSettleLog extends Model<RepaymentSettleLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款结清日志主键ID
     */
	@TableId(value="settle_log_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "还款结清日志主键ID")
	private Long settleLogId;
    /**
     * 业务id
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务id")
	private String businessId;
    /**
     * 原业务id
     */
	@TableField("org_business_id")
	@ApiModelProperty(required= true,value = "原业务id")
	private String orgBusinessId;
    /**
     * 所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
     */
	@TableField("plan_id")
	@ApiModelProperty(required= true,value = "所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)")
	private String planId;
    /**
     * 还款时间
     */
	@TableField("repay_date")
	@ApiModelProperty(required= true,value = "还款时间")
	private Date repayDate;
    /**
     * 实还总金额
     */
	@TableField("fact_amount")
	@ApiModelProperty(required= true,value = "实还总金额")
	private BigDecimal factAmount;
    /**
     * 还款来源，10：线下转账, 20：线下代扣，30：银行代扣
     */
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "还款来源，10：线下转账, 20：线下代扣，30：银行代扣")
	private Integer repaySource;
    /**
     * 产生的结余金额
     */
	@TableField("surplus_amount")
	@ApiModelProperty(required= true,value = "产生的结余金额")
	private BigDecimal surplusAmount;
    /**
     * 产生结余的结余表关联id
     */
	@TableField("surplus_ref_id")
	@ApiModelProperty(required= true,value = "产生结余的结余表关联id")
	private String surplusRefId;
    /**
     * 使用结余的结余表关联id
     */
	@TableField("surplus_use_ref_id")
	@ApiModelProperty(required= true,value = "使用结余的结余表关联id")
	private String surplusUseRefId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人ID
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人ID")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 创建人名称
     */
	@TableField("create_user_name")
	@ApiModelProperty(required= true,value = "创建人名称")
	private String createUserName;


	public Long getSettleLogId() {
		return settleLogId;
	}

	public void setSettleLogId(Long settleLogId) {
		this.settleLogId = settleLogId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrgBusinessId() {
		return orgBusinessId;
	}

	public void setOrgBusinessId(String orgBusinessId) {
		this.orgBusinessId = orgBusinessId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Date getRepayDate() {
		return repayDate;
	}

	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}

	public BigDecimal getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	public Integer getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	public BigDecimal getSurplusAmount() {
		return surplusAmount;
	}

	public void setSurplusAmount(BigDecimal surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	public String getSurplusRefId() {
		return surplusRefId;
	}

	public void setSurplusRefId(String surplusRefId) {
		this.surplusRefId = surplusRefId;
	}

	public String getSurplusUseRefId() {
		return surplusUseRefId;
	}

	public void setSurplusUseRefId(String surplusUseRefId) {
		this.surplusUseRefId = surplusUseRefId;
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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Override
	protected Serializable pkVal() {
		return this.settleLogId;
	}

	@Override
	public String toString() {
		return "RepaymentSettleLog{" +
			", settleLogId=" + settleLogId +
			", businessId=" + businessId +
			", orgBusinessId=" + orgBusinessId +
			", planId=" + planId +
			", repayDate=" + repayDate +
			", factAmount=" + factAmount +
			", repaySource=" + repaySource +
			", surplusAmount=" + surplusAmount +
			", surplusRefId=" + surplusRefId +
			", surplusUseRefId=" + surplusUseRefId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createUserName=" + createUserName +
			"}";
	}
}
