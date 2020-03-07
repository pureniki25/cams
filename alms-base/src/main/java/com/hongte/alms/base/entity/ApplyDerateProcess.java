package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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
	
//    /**
//     * 申请减免类型
//     */
//	@TableField("derate_type")
//	@ApiModelProperty(required= true,value = "申请减免类型")
//	private String derateType;
//    /**
//     * 申请减免金额
//     */
//	@TableField("derate_money")
//	@ApiModelProperty(required= true,value = "申请减免金额")
//	private BigDecimal derateMoney;
	
	
    /**
     * 应付逾期利息
     */
	@TableField("outside_interest")
	@ApiModelProperty(required= true,value = "应付逾期利息")
	private BigDecimal outsideInterest;
    /**
     * 提前还款违约金
     */
	@TableField("preLate_fees")
	@ApiModelProperty(required= true,value = "提前还款违约金")
	private BigDecimal preLateFees;
	
    /**
    /**
     * 综合收益率
     */
	@TableField("general_return_rate")
	@ApiModelProperty(required= true,value = " 综合收益率")
	private String generalReturnRate;
	
    /**
     * 减免后实收总额
     */
	@TableField("real_receive_money")
	@ApiModelProperty(required= true,value = "减免后实收总额")
	private BigDecimal realReceiveMoney;
	
	   /**
     * 减免后应收总额
     */
	@TableField("should_receive_money")
	@ApiModelProperty(required= true,value = "减免后应收总额")
	private BigDecimal shouldReceiveMoney;
    /**
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
	
	@TableField("derate_money_total")
	@ApiModelProperty(required= false,value = "减免总金额-统计字段")
	private BigDecimal applyDerateMoney;
	
	 /**
     * 上门费
     */
	@TableField("door_fee")
	@ApiModelProperty(required= true,value = "上门费")
	private BigDecimal doorFee;
	
	 /**
     * 拖车费
     */
	@TableField("trailer_fee")
	@ApiModelProperty(required= true,value = "拖车费")
	private BigDecimal trailerFee;
	
	
	 /**
     * Gps费
     */
	@TableField("gps_fee")
	@ApiModelProperty(required= true,value = "Gps费")
	private BigDecimal gpsFee;
	
	 /**
     * 停车费
     */
	@TableField("park_fee")
	@ApiModelProperty(required= true,value = "停车费")
	private BigDecimal parkFee;
	
	 /**
     * 计划还款日期
     */
	@TableField("plan_repay_time")
	@ApiModelProperty(required= true,value = "计划还款日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date planRepayTime;
	
	 /**
     * 是否生效
     */
	@TableField("is_active")
	@ApiModelProperty(required= true,value ="是否生效")
	private Integer isActive;


	/**
	 * 本金违约金
	 */
	@TableField("principle_penalty")
	@ApiModelProperty(required= true,value ="本金违约金")
	private BigDecimal principlePenalty;

	/**
	 * 平台服务费违约金
	 */
	@TableField("plat_service_penalty")
	@ApiModelProperty(required= true,value ="平台服务费违约金")
	private BigDecimal platServicePenalty;

	/**
	 * 分公司服务费违约金
	 */
	@TableField("month_service_penalty")
	@ApiModelProperty(required= true,value ="分公司服务费违约金")
	private BigDecimal monthServicePenalty;


	
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getDoorFee() {
		return doorFee;
	}

	public void setDoorFee(BigDecimal doorFee) {
		this.doorFee = doorFee;
	}

	public BigDecimal getTrailerFee() {
		return trailerFee;
	}

	public void setTrailerFee(BigDecimal trailerFee) {
		this.trailerFee = trailerFee;
	}

	public BigDecimal getGpsFee() {
		return gpsFee;
	}

	public void setGpsFee(BigDecimal gpsFee) {
		this.gpsFee = gpsFee;
	}

	public BigDecimal getParkFee() {
		return parkFee;
	}

	public void setParkFee(BigDecimal parkFee) {
		this.parkFee = parkFee;
	}

	public Date getPlanRepayTime() {
		return planRepayTime;
	}

	public void setPlanRepayTime(Date planRepayTime) {
		this.planRepayTime = planRepayTime;
	}


	public BigDecimal getApplyDerateMoney() {
		return applyDerateMoney;
	}

	public void setApplyDerateMoney(BigDecimal applyDerateMoney) {
		this.applyDerateMoney = applyDerateMoney;
	}

	public BigDecimal getOutsideInterest() {
		return outsideInterest;
	}

	public void setOutsideInterest(BigDecimal outsideInterest) {
		this.outsideInterest = outsideInterest;
	}

	public BigDecimal getPreLateFees() {
		return preLateFees;
	}

	public void setPreLateFees(BigDecimal preLateFees) {
		this.preLateFees = preLateFees;
	}

	public String getGeneralReturnRate() {
		return generalReturnRate;
	}

	public void setGeneralReturnRate(String generalReturnRate) {
		this.generalReturnRate = generalReturnRate;
	}

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
   
	public BigDecimal getShouldReceiveMoney() {
		return shouldReceiveMoney;
	}

	public void setShouldReceiveMoney(BigDecimal shouldReceiveMoney) {
		this.shouldReceiveMoney = shouldReceiveMoney;
	}

	public BigDecimal getPrinciplePenalty() {
		return principlePenalty;
	}

	public void setPrinciplePenalty(BigDecimal principlePenalty) {
		this.principlePenalty = principlePenalty;
	}

	public BigDecimal getPlatServicePenalty() {
		return platServicePenalty;
	}

	public void setPlatServicePenalty(BigDecimal platServicePenalty) {
		this.platServicePenalty = platServicePenalty;
	}

	public BigDecimal getMonthServicePenalty() {
		return monthServicePenalty;
	}

	public void setMonthServicePenalty(BigDecimal monthServicePenalty) {
		this.monthServicePenalty = monthServicePenalty;
	}

	@Override
	public String toString() {
		return "ApplyDerateProcess{" +
				"applyDerateProcessId='" + applyDerateProcessId + '\'' +
				", processId='" + processId + '\'' +
				", businessId='" + businessId + '\'' +
				", crpId='" + crpId + '\'' +
				", outsideInterest=" + outsideInterest +
				", preLateFees=" + preLateFees +
				", generalReturnRate='" + generalReturnRate + '\'' +
				", realReceiveMoney=" + realReceiveMoney +
				", shouldReceiveMoney=" + shouldReceiveMoney +
				", derateReson='" + derateReson + '\'' +
				", createTime=" + createTime +
				", createUser='" + createUser + '\'' +
				", updateTime=" + updateTime +
				", updateUser='" + updateUser + '\'' +
				", title='" + title + '\'' +
				", isSettle=" + isSettle +
				", applyDerateMoney=" + applyDerateMoney +
				", doorFee=" + doorFee +
				", trailerFee=" + trailerFee +
				", gpsFee=" + gpsFee +
				", parkFee=" + parkFee +
				", planRepayTime=" + planRepayTime +
				", isActive=" + isActive +
				", principlePenalty=" + principlePenalty +
				", platServicePenalty=" + platServicePenalty +
				", monthServicePenalty=" + monthServicePenalty +
				'}';
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
