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
 * 减免金额使用记录表
 * </p>
 *
 * @author 王继光
 * @since 2018-07-13
 */
@ApiModel
@TableName("tb_derate_use_log")
public class DerateUseLog extends Model<DerateUseLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("derate_use_log_id")
	@ApiModelProperty(required= true,value = "主键")
	private String derateUseLogId;
    /**
     * 还款记录id,与settle_log_id不共存
     */
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "还款记录id,与settle_log_id不共存")
	private String confirmLogId;
    /**
     * 结清记录id,与confirm_log_id不共存
     */
	@TableField("settle_log_id")
	@ApiModelProperty(required= true,value = "结清记录id,与confirm_log_id不共存")
	private String settleLogId;
    /**
     * 减免类型id
     */
	@TableField("apply_derate_type_id")
	@ApiModelProperty(required= true,value = "减免类型id")
	private String applyDerateTypeId;
    /**
     * 业务明细项id
     */
	@TableField("biz_plan_detail_id")
	@ApiModelProperty(required= true,value = "业务明细项id")
	private String bizPlanDetailId;
    /**
     * 业务减免金额
     */
	@TableField("biz_derate_amount")
	@ApiModelProperty(required= true,value = "业务减免金额")
	private BigDecimal bizDerateAmount;
    /**
     * 标的明细项id
     */
	@TableField("proj_plan_detail_id")
	@ApiModelProperty(required= true,value = "标的明细项id")
	private String projPlanDetailId;
    /**
     * 标的减免金额
     */
	@TableField("proj_derate_amount")
	@ApiModelProperty(required= true,value = "标的减免金额")
	private BigDecimal projDerateAmount;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "")
	private String createUser;
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "")
	private Date updateTime;
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "")
	private Date updateUser;


	public String getDerateUseLogId() {
		return derateUseLogId;
	}

	public void setDerateUseLogId(String derateUseLogId) {
		this.derateUseLogId = derateUseLogId;
	}

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getSettleLogId() {
		return settleLogId;
	}

	public void setSettleLogId(String settleLogId) {
		this.settleLogId = settleLogId;
	}

	public String getApplyDerateTypeId() {
		return applyDerateTypeId;
	}

	public void setApplyDerateTypeId(String applyDerateTypeId) {
		this.applyDerateTypeId = applyDerateTypeId;
	}

	public String getBizPlanDetailId() {
		return bizPlanDetailId;
	}

	public void setBizPlanDetailId(String bizPlanDetailId) {
		this.bizPlanDetailId = bizPlanDetailId;
	}

	public BigDecimal getBizDerateAmount() {
		return bizDerateAmount;
	}

	public void setBizDerateAmount(BigDecimal bizDerateAmount) {
		this.bizDerateAmount = bizDerateAmount;
	}

	public String getProjPlanDetailId() {
		return projPlanDetailId;
	}

	public void setProjPlanDetailId(String projPlanDetailId) {
		this.projPlanDetailId = projPlanDetailId;
	}

	public BigDecimal getProjDerateAmount() {
		return projDerateAmount;
	}

	public void setProjDerateAmount(BigDecimal projDerateAmount) {
		this.projDerateAmount = projDerateAmount;
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

	public Date getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Date updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.derateUseLogId;
	}

	@Override
	public String toString() {
		return "DerateUseLog{" +
			", derateUseLogId=" + derateUseLogId +
			", confirmLogId=" + confirmLogId +
			", settleLogId=" + settleLogId +
			", applyDerateTypeId=" + applyDerateTypeId +
			", bizPlanDetailId=" + bizPlanDetailId +
			", bizDerateAmount=" + bizDerateAmount +
			", projPlanDetailId=" + projPlanDetailId +
			", projDerateAmount=" + projDerateAmount +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
