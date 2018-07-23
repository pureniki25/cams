package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 还款结清日志详情记录表
 * </p>
 *
 * @author lxq
 * @since 2018-07-23
 */
@ApiModel
@TableName("tb_repayment_settle_log_detail")
public class RepaymentSettleLogDetail extends Model<RepaymentSettleLogDetail> {

	private static final long serialVersionUID = 1L;

	/**
	 * 还款结清详情日志主键ID
	 */
	@TableField("settle_detail_log_id")
	@ApiModelProperty(required= true,value = "还款结清详情日志主键ID")
	private String settleDetailLogId;
	/**
	 * 还款结清日志主键ID
	 */
	@TableField("settle_log_id")
	@ApiModelProperty(required= true,value = "还款结清日志主键ID")
	private String settleLogId;
	/**
	 * 关联还款计划列表ID(主键)
	 */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "关联还款计划列表ID(主键)")
	private String planListId;
	/**
	 * 关联应还项目明细ID(主键)
	 */
	@TableField("plan_detail_id")
	@ApiModelProperty(required= true,value = "关联应还项目明细ID(主键)")
	private String planDetailId;
	/**
	 * 关联标的还款计划列表ID(主键)
	 */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "关联标的还款计划列表ID(主键)")
	private String projPlanListId;
	/**
	 * 关联标的应还项目明细ID(主键)
	 */
	@TableField("proj_plan_detail_id")
	@ApiModelProperty(required= true,value = "关联标的应还项目明细ID(主键)")
	private String projPlanDetailId;
	/**
	 * 款项池ID，外键，对应tb_money_pool的money_pool_id
	 */
	@TableField("money_pool_id")
	@ApiModelProperty(required= true,value = "款项池ID，外键，对应tb_money_pool的money_pool_id")
	private String moneyPoolId;
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
	 * 应还项目所属分类，10：本金，20：利息，25：利差，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点（返点都是不线上分账的）
	 */
	@TableField("plan_item_type")
	@ApiModelProperty(required= true,value = "应还项目所属分类，10：本金，20：利息，25：利差，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点（返点都是不线上分账的）")
	private Integer planItemType;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public String getSettleDetailLogId() {
		return settleDetailLogId;
	}

	public void setSettleDetailLogId(String settleDetailLogId) {
		this.settleDetailLogId = settleDetailLogId;
	}

	public String getSettleLogId() {
		return settleLogId;
	}

	public void setSettleLogId(String settleLogId) {
		this.settleLogId = settleLogId;
	}

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getPlanDetailId() {
		return planDetailId;
	}

	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
	}

	public String getProjPlanDetailId() {
		return projPlanDetailId;
	}

	public void setProjPlanDetailId(String projPlanDetailId) {
		this.projPlanDetailId = projPlanDetailId;
	}

	public String getMoneyPoolId() {
		return moneyPoolId;
	}

	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
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

	public Integer getPlanItemType() {
		return planItemType;
	}

	public void setPlanItemType(Integer planItemType) {
		this.planItemType = planItemType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.settleDetailLogId;
	}

	@Override
	public String toString() {
		return "RepaymentSettleLogDetail{" +
				", settleDetailLogId=" + settleDetailLogId +
				", settleLogId=" + settleLogId +
				", planListId=" + planListId +
				", planDetailId=" + planDetailId +
				", projPlanListId=" + projPlanListId +
				", projPlanDetailId=" + projPlanDetailId +
				", moneyPoolId=" + moneyPoolId +
				", factAmount=" + factAmount +
				", repaySource=" + repaySource +
				", planItemType=" + planItemType +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				"}";
	}
}
