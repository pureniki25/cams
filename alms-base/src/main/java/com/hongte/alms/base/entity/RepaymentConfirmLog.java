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
 * 还款确认日志记录表
 * </p>
 *
 * @author 王继光
 * @since 2018-05-25
 */
@ApiModel
@TableName("tb_repayment_confirm_log")
public class RepaymentConfirmLog extends Model<RepaymentConfirmLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款确认日志主键ID
     */
    @TableId("confirm_log_id")
	@ApiModelProperty(required= true,value = "还款确认日志主键ID")
	private String confirmLogId;
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
     * 期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "期数")
	private String afterId;
	
	@TableField("period")
	private Integer period;
	
	@TableField("repay_source")
	private Integer repaySource ;
	
    /**
     * 日志序号,表示当前是第几次还款确认
     */
	@TableField("idx")
	@ApiModelProperty(required= true,value = "日志序号,表示当前是第几次还款确认")
	private Integer idx;
    /**
     * 能否撤销,代扣和资金分发后的不能撤销
     */
	@TableField("can_revoke")
	@ApiModelProperty(required= true,value = "能否撤销,代扣和资金分发后的不能撤销")
	private Integer canRevoke;
    /**
     * 标的还款分润后的镜像
     */
	@TableField("proj_plan_json")
	@ApiModelProperty(required= true,value = "标的还款分润后的镜像")
	private String projPlanJson;
	/**
     * 还款时间
     */
	@TableField("repay_date")
	@ApiModelProperty(required= true,value = "还款时间")
	private Date repayDate;
//	`fact_amount` decimal(18,4) DEFAULT NULL COMMENT '实还总金额',
//	  `surplus_amount` decimal(18,4) DEFAULT NULL COMMENT '结余金额',
//	  `surplus_ref_id` varchar(50) DEFAULT NULL COMMENT '结余表关联id',
	@TableField("fact_amount")
	@ApiModelProperty(required= true,value = "实还总金额")
	private BigDecimal factAmount;
	@TableField("surplus_amount")
	@ApiModelProperty(required= true,value = "结余金额")
	private BigDecimal surplusAmount;
	@TableField("surplus_ref_id")
	@ApiModelProperty(required= true,value = "结余表关联id")
	private String surplusRefId;
	@TableField("surplus_use_ref_id")
	private String surplusUseRefId;
	
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
     * 创建人
     */
	@TableField("create_user_name")
	@ApiModelProperty(required= true)
	private String createUserName;
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


	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
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

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}


	public Integer getCanRevoke() {
		return canRevoke;
	}

	public void setCanRevoke(Integer canRevoke) {
		this.canRevoke = canRevoke;
	}

	public String getProjPlanJson() {
		return projPlanJson;
	}

	public void setProjPlanJson(String projPlanJson) {
		this.projPlanJson = projPlanJson;
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
		return this.confirmLogId;
	}

	@Override
	public String toString() {
		return "RepaymentConfirmLog{" +
			", confirmLogId=" + confirmLogId +
			", businessId=" + businessId +
			", orgBusinessId=" + orgBusinessId +
			", afterId=" + afterId +
			", idx=" + getIdx() +
			", canRevoke=" + canRevoke +
			", projPlanJson=" + projPlanJson +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the repayDate
	 */
	public Date getRepayDate() {
		return repayDate;
	}

	/**
	 * @param repayDate the repayDate to set
	 */
	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}

	/**
	 * @return the factAmount
	 */
	public BigDecimal getFactAmount() {
		return factAmount;
	}

	/**
	 * @param factAmount the factAmount to set
	 */
	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	/**
	 * @return the surplusAmount
	 */
	public BigDecimal getSurplusAmount() {
		return surplusAmount;
	}

	/**
	 * @param surplusAmount the surplusAmount to set
	 */
	public void setSurplusAmount(BigDecimal surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	/**
	 * @return the surplusRefId
	 */
	public String getSurplusRefId() {
		return surplusRefId;
	}

	/**
	 * @param surplusRefId the surplusRefId to set
	 */
	public void setSurplusRefId(String surplusRefId) {
		this.surplusRefId = surplusRefId;
	}

	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}

	/**
	 * @return the repaySource
	 */
	public Integer getRepaySource() {
		return repaySource;
	}

	/**
	 * @param repaySource the repaySource to set
	 */
	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	/**
	 * @return the surplusUseRefId
	 */
	public String getSurplusUseRefId() {
		return surplusUseRefId;
	}

	/**
	 * @param surplusUseRefId the surplusUseRefId to set
	 */
	public void setSurplusUseRefId(String surplusUseRefId) {
		this.surplusUseRefId = surplusUseRefId;
	}

	/**
	 * @return the idx
	 */
	public Integer getIdx() {
		return idx;
	}

	/**
	 * @param idx the idx to set
	 */
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
}
