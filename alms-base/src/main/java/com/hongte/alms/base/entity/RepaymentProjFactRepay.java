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
 * 标实还明细表
 * </p>
 *
 * @author 王继光
 * @since 2018-08-23
 */
@ApiModel
@TableName("tb_repayment_proj_fact_repay")
public class RepaymentProjFactRepay extends Model<RepaymentProjFactRepay> {

    private static final long serialVersionUID = 1L;

    /**
     * 标的实还项目明细ID（主键）
     */
    @TableId("proj_plan_detail_repay_id")
	@ApiModelProperty(required= true,value = "标的实还项目明细ID（主键）")
	private String projPlanDetailRepayId;
    /**
     * 标的应还项目明细ID(外键  对应 tb_repayment_proj_plan_list_detail. proj_plan_detail_id)
     */
	@TableField("proj_plan_detail_id")
	@ApiModelProperty(required= true,value = "标的应还项目明细ID(外键  对应 tb_repayment_proj_plan_list_detail. proj_plan_detail_id)")
	private String projPlanDetailId;
    /**
     * 标的还款计划列表ID
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "标的还款计划列表ID")
	private String projPlanListId;
    /**
     * 业务还款计划列表ID
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "业务还款计划列表ID")
	private String planListId;
    /**
     * 上标项目编号
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "上标项目编号")
	private String projectId;
    /**
     * 还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "还款计划所属业务编号(若当前业务为展期，则存展期业务编号)")
	private String businessId;
    /**
     * 还款计划所属原业务编号
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "还款计划所属原业务编号")
	private String origBusinessId;
    /**
     * 所属期数
     */
	@ApiModelProperty(required= true,value = "所属期数")
	private Integer period;
    /**
     * 总批次期数，唯一，对应信贷系统的还款计划编号
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "总批次期数，唯一，对应信贷系统的还款计划编号")
	private String afterId;
    /**
     * 资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空")
	private String feeId;
    /**
     * 应还项目名称
     */
	@TableField("plan_item_name")
	@ApiModelProperty(required= true,value = "应还项目名称")
	private String planItemName;
    /**
     * 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
     */
	@TableField("plan_item_type")
	@ApiModelProperty(required= true,value = "应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收")
	private Integer planItemType;
    /**
     * 实还金额(元)
     */
	@TableField("fact_amount")
	@ApiModelProperty(required= true,value = "实还金额(元)")
	private BigDecimal factAmount;
    /**
     * 还款来源，10：线下转账，11:用往期结余还款,20：线下代扣，30：银行代扣
     */
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "还款来源，10：线下转账，11:用往期结余还款,20：线下代扣，30：银行代扣")
	private Integer repaySource;
    /**
     * 实还日期
     */
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "实还日期")
	private Date factRepayDate;
    /**
     * 还款来源关联的相关记录ID
     */
	@TableField("repay_ref_id")
	@ApiModelProperty(required= true,value = "还款来源关联的相关记录ID")
	private String repayRefId;
    /**
     * 还款确认日志id
     */
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "还款确认日志id")
	private String confirmLogId;
    /**
     * 还款来源id
     */
	@TableField("repay_source_id")
	@ApiModelProperty(required= true,value = "还款来源id")
	private String repaySourceId;
    /**
     * 是否已被撤销,0=未被撤销,1=已被撤销
     */
	@TableField("is_cancelled")
	@ApiModelProperty(required= true,value = "是否已被撤销,0=未被撤销,1=已被撤销")
	private Integer isCancelled;
    /**
     * 创建日期
     */
	@TableField("create_date")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createDate;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_date")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateDate;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
    /**
     * 结清关联id
     */
	@TableField("settle_log_id")
	@ApiModelProperty(required= true,value = "结清关联id")
	private String settleLogId;


	public String getProjPlanDetailRepayId() {
		return projPlanDetailRepayId;
	}

	public void setProjPlanDetailRepayId(String projPlanDetailRepayId) {
		this.projPlanDetailRepayId = projPlanDetailRepayId;
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

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getPlanItemName() {
		return planItemName;
	}

	public void setPlanItemName(String planItemName) {
		this.planItemName = planItemName;
	}

	public Integer getPlanItemType() {
		return planItemType;
	}

	public void setPlanItemType(Integer planItemType) {
		this.planItemType = planItemType;
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

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public String getRepayRefId() {
		return repayRefId;
	}

	public void setRepayRefId(String repayRefId) {
		this.repayRefId = repayRefId;
	}

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getRepaySourceId() {
		return repaySourceId;
	}

	public void setRepaySourceId(String repaySourceId) {
		this.repaySourceId = repaySourceId;
	}

	public Integer getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Integer isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getSettleLogId() {
		return settleLogId;
	}

	public void setSettleLogId(String settleLogId) {
		this.settleLogId = settleLogId;
	}

	@Override
	protected Serializable pkVal() {
		return this.projPlanDetailRepayId;
	}

	@Override
	public String toString() {
		return "RepaymentProjFactRepay{" +
			", projPlanDetailRepayId=" + projPlanDetailRepayId +
			", projPlanDetailId=" + projPlanDetailId +
			", projPlanListId=" + projPlanListId +
			", planListId=" + planListId +
			", projectId=" + projectId +
			", businessId=" + businessId +
			", origBusinessId=" + origBusinessId +
			", period=" + period +
			", afterId=" + afterId +
			", feeId=" + feeId +
			", planItemName=" + planItemName +
			", planItemType=" + planItemType +
			", factAmount=" + factAmount +
			", repaySource=" + repaySource +
			", factRepayDate=" + factRepayDate +
			", repayRefId=" + repayRefId +
			", confirmLogId=" + confirmLogId +
			", repaySourceId=" + repaySourceId +
			", isCancelled=" + isCancelled +
			", createDate=" + createDate +
			", createUser=" + createUser +
			", updateDate=" + updateDate +
			", updateUser=" + updateUser +
			", settleLogId=" + settleLogId +
			"}";
	}
}
