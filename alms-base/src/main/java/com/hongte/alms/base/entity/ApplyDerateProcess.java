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
 * 
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@ApiModel
@TableName("tb_apply_derate_process")
public class ApplyDerateProcess extends Model<ApplyDerateProcess> {

    private static final long serialVersionUID = 1L;



    /**
     * 减免申请l流程ID
     */
    @TableId("apply_derate_process_id")
	@ApiModelProperty(required= true,value = "减免申请l流程ID")
	private String applyDerateProcessId;
    /**
     * 流程ID
     */
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "流程ID")
	private String processId;
    /**
     * 业务ID
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务ID")
	private String businessId;
    /**
     * 用户还款计划ID
     */
	@TableField("crp_id")
	@ApiModelProperty(required= true,value = "用户还款计划ID")
	private String crpId;
    /**
     * 申请减免类型
     */
	@TableField("derate_type")
	@ApiModelProperty(required= true,value = "申请减免类型")
	private String derateType;
    /**
     * 申请减免金额
     */
	@TableField("derate_money")
	@ApiModelProperty(required= true,value = "申请减免金额")
	private BigDecimal derateMoney;
    /**
     * 减免后实收总额
     */
	@TableField("real_receive_money")
	@ApiModelProperty(required= true,value = "减免后实收总额")
	private BigDecimal realReceiveMoney;
    /**
     * 减免原因
     */
	@TableField("derate_reson")
	@ApiModelProperty(required= true,value = "减免原因")
	private String derateReson;
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
	 * 减免申请的标题
	 */
	@ApiModelProperty(required= true,value = "减免申请的标题")
	private String title;
	/**
	 * 是否结清标志位: 0 不结清， 1结清
	 */
	@TableField("is_settle")
	@ApiModelProperty(required= true,value = "是否结清标志位: 0 不结清， 1结清")
	private Integer isSettle;

	public String getApplyDerateProcessId() {
		return applyDerateProcessId;
	}

	public void setApplyDerateProcessId(String applyDerateProcessId) {
		this.applyDerateProcessId = applyDerateProcessId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	public String getDerateType() {
		return derateType;
	}

	public void setDerateType(String derateType) {
		this.derateType = derateType;
	}

	public BigDecimal getDerateMoney() {
		return derateMoney;
	}

	public void setDerateMoney(BigDecimal derateMoney) {
		this.derateMoney = derateMoney;
	}

	public BigDecimal getRealReceiveMoney() {
		return realReceiveMoney;
	}

	public void setRealReceiveMoney(BigDecimal realReceiveMoney) {
		this.realReceiveMoney = realReceiveMoney;
	}

	public String getDerateReson() {
		return derateReson;
	}

	public void setDerateReson(String derateReson) {
		this.derateReson = derateReson;
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
		return this.applyDerateProcessId;
	}

	@Override
	public String toString() {
		return "ApplyDerateProcess{" +
			", applyDerateProcessId=" + applyDerateProcessId +
			", processId=" + processId +
			", businessId=" + businessId +
			", crpId=" + crpId +
			", derateType=" + derateType +
			", derateMoney=" + derateMoney +
			", realReceiveMoney=" + realReceiveMoney +
			", derateReson=" + derateReson +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Integer isSettle) {
		this.isSettle = isSettle;
	}
}
