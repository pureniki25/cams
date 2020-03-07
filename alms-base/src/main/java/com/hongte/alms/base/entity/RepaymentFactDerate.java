package com.hongte.alms.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 实际减免表(根据核销时实际发生的减免创建)
 * </p>
 *
 * @author wjg
 * @since 2018-11-29
 */
@ApiModel
@TableName("tb_repayment_fact_derate")
public class RepaymentFactDerate extends Model<RepaymentFactDerate> {

    private static final long serialVersionUID = 1L;

    /**
     * 实际减免id
     */
    @TableId("fact_derate_id")
	@ApiModelProperty(required= true,value = "实际减免id")
	private String factDerateId;
    /**
     * 核销记录id
     */
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "核销记录id")
	private String confirmLogId;
    /**
     * 申请减免信息表id
     */
	@TableField("apply_derate_process_id")
	@ApiModelProperty(required= true,value = "申请减免信息表id")
	private String applyDerateProcessId;
    /**
     * 减免费用项id
     */
	@TableField("apply_derate_type_id")
	@ApiModelProperty(required= true,value = "减免费用项id")
	private String applyDerateTypeId;
    /**
     * 应减免金额
     */
	@TableField("plan_derate")
	@ApiModelProperty(required= true,value = "应减免金额")
	private BigDecimal planDerate;
    /**
     * 此次核销实际减免金额
     */
	@TableField("fact_derate")
	@ApiModelProperty(required= true,value = "此次核销实际减免金额")
	private BigDecimal factDerate;
    /**
     * 是否已被撤销,0=未被撤销,1=已被撤销
     */
	@TableField("is_cancelled")
	@ApiModelProperty(required= true,value = "是否已被撤销,0=未被撤销,1=已被撤销")
	private Integer isCancelled;
    /**
     * tb_repayment_proj_plan_list_detail的proj_plan_detail_id
     */
	@TableField("proj_plan_detail_id")
	@ApiModelProperty(required= true,value = "tb_repayment_proj_plan_list_detail的proj_plan_detail_id")
	private String projPlanDetailId;
    /**
     * tb_repayment_proj_plan_list的proj_plan_list_id
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "tb_repayment_proj_plan_list的proj_plan_list_id")
	private String projPlanListId;
    /**
     * tb_repayment_proj_plan的proj_plan_id
     */
	@TableField("proj_plan_id")
	@ApiModelProperty(required= true,value = "tb_repayment_proj_plan的proj_plan_id")
	private String projPlanId;
    /**
     * 标的id
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的id")
	private String projectId;
    /**
     * tb_repayment_biz_plan的plan_id
     */
	@TableField("plan_id")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan的plan_id")
	private String planId;
    /**
     * tb_repayment_biz_plan_list的plan_list_id
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan_list的plan_list_id")
	private String planListId;
    /**
     * tb_repayment_biz_plan_list_detail的plan_list_detail_id
     */
	@TableField("plan_list_detail_id")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan_list_detail的plan_list_detail_id")
	private String planListDetailId;
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
     * 是否同步过 0没同步 1已同步
     */
	@TableField("is_sync")
	@ApiModelProperty(required= true,value = "是否同步过 0没同步 1已同步")
	private Integer isSync;


	public String getFactDerateId() {
		return factDerateId;
	}

	public void setFactDerateId(String factDerateId) {
		this.factDerateId = factDerateId;
	}

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getApplyDerateProcessId() {
		return applyDerateProcessId;
	}

	public void setApplyDerateProcessId(String applyDerateProcessId) {
		this.applyDerateProcessId = applyDerateProcessId;
	}

	public String getApplyDerateTypeId() {
		return applyDerateTypeId;
	}

	public void setApplyDerateTypeId(String applyDerateTypeId) {
		this.applyDerateTypeId = applyDerateTypeId;
	}

	public BigDecimal getPlanDerate() {
		return planDerate;
	}

	public void setPlanDerate(BigDecimal planDerate) {
		this.planDerate = planDerate;
	}

	public BigDecimal getFactDerate() {
		return factDerate;
	}

	public void setFactDerate(BigDecimal factDerate) {
		this.factDerate = factDerate;
	}

	public Integer getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Integer isCancelled) {
		this.isCancelled = isCancelled;
	}

	public String getProjPlanDetailId() {
		return projPlanDetailId;
	}

	public void setProjPlanDetailId(String projPlanDetailId) {
		this.projPlanDetailId = projPlanDetailId;
	}

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
	}

	public String getProjPlanId() {
		return projPlanId;
	}

	public void setProjPlanId(String projPlanId) {
		this.projPlanId = projPlanId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getPlanListDetailId() {
		return planListDetailId;
	}

	public void setPlanListDetailId(String planListDetailId) {
		this.planListDetailId = planListDetailId;
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

	public Integer getIsSync() {
		return isSync;
	}

	public void setIsSync(Integer isSync) {
		this.isSync = isSync;
	}

	@Override
	protected Serializable pkVal() {
		return this.factDerateId;
	}

	@Override
	public String toString() {
		return "RepaymentFactDerate{" +
			", factDerateId=" + factDerateId +
			", confirmLogId=" + confirmLogId +
			", applyDerateProcessId=" + applyDerateProcessId +
			", applyDerateTypeId=" + applyDerateTypeId +
			", planDerate=" + planDerate +
			", factDerate=" + factDerate +
			", isCancelled=" + isCancelled +
			", projPlanDetailId=" + projPlanDetailId +
			", projPlanListId=" + projPlanListId +
			", projPlanId=" + projPlanId +
			", projectId=" + projectId +
			", planId=" + planId +
			", planListId=" + planListId +
			", planListDetailId=" + planListDetailId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", isSync=" + isSync +
			"}";
	}
}
