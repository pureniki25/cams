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
 * 标的还款计划信息
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@ApiModel
@TableName("tb_repayment_proj_plan")
public class RepaymentProjPlan extends Model<RepaymentProjPlan> {

    private static final long serialVersionUID = 1L;

    /**
     * 标的还款计划编号(主键)
     */
    @TableId("proj_plan_id")
	@ApiModelProperty(required= true,value = "标的还款计划编号(主键)")
	private String projPlanId;
    /**
     * 上标项目编号
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "上标项目编号")
	private String projectId;
    /**
     * 业务编号(若当前业务为展期，则存展期业务编号)
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号(若当前业务为展期，则存展期业务编号)")
	private String businessId;
    /**
     * 当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号）
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号）")
	private String originalBusinessId;
    /**
     * 还款计划批次号(展期与原业务同属一个还款计划批次号，多次出款的还款计划批次号可能不同)
     */
	@TableField("repayment_batch_id")
	@ApiModelProperty(required= true,value = "还款计划批次号(展期与原业务同属一个还款计划批次号，多次出款的还款计划批次号可能不同)")
	private String repaymentBatchId;
    /**
     * 标所属业务还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
     */
	@TableField("plan_id")
	@ApiModelProperty(required= true,value = "标所属业务还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)")
	private String planId;
    /**
     * 生成还款计划对应的借款总额(元)
     */
	@TableField("borrow_money")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款总额(元)")
	private BigDecimal borrowMoney;
    /**
     * 生成还款计划对应的借款利率(%)，如11%则存11.0
     */
	@TableField("borrow_rate")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款利率(%)，如11%则存11.0")
	private BigDecimal borrowRate;
    /**
     * 生成还款计划对应的借款利率类型，1：年利率，2：月利率，3：日利率
     */
	@TableField("borrow_rate_unit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款利率类型，1：年利率，2：月利率，3：日利率")
	private Integer borrowRateUnit;
	/**
	 * 线上逾期滞纳金费率(%)
	 */
	@TableField("on_line_over_due_rate")
	@ApiModelProperty(required= true,value = "线上逾期滞纳金费率(%)")
	private BigDecimal onLineOverDueRate;
	/**
	 * 线上逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
	 */
	@TableField("on_line_over_due_rate_unit")
	@ApiModelProperty(required= true,value = "线上逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
	private Integer onLineOverDueRateUnit;
	/**
	 * 线下期外逾期滞纳金费率(%)
	 */
	@TableField("off_line_out_over_due_rate")
	@ApiModelProperty(required= true,value = "线下期外逾期滞纳金费率(%)")
	private BigDecimal offLineOutOverDueRate;
	/**
	 * 线下期外逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
	 */
	@TableField("off_line_out_over_due_rate_unit")
	@ApiModelProperty(required= true,value = "线下期外逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
	private Integer offLineOutOverDueRateUnit;
	/**
	 * 线下期内逾期滞纳金费率(%)
	 */
	@TableField("off_line_in_over_due_rate")
	@ApiModelProperty(required= true,value = "线下期内逾期滞纳金费率(%)")
	private BigDecimal offLineInOverDueRate;
	/**
	 * 线下期内逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
	 */
	@TableField("off_line_in_over_due_rate_unit")
	@ApiModelProperty(required= true,value = "线下期内逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
	private Integer offLineInOverDueRateUnit;



	/**
     * 生成还款计划对应的借款期限
     */
	@TableField("borrow_limit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款期限")
	private Integer borrowLimit;
    /**
     * 生成还款计划对应的借款期限单位，1：月，2：天
     */
	@TableField("borrow_limit_unit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款期限单位，1：月，2：天")
	private Integer borrowLimitUnit;
    /**
     * 还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期
     */
	@TableField("plan_status")
	@ApiModelProperty(required= true,value = "还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期")
	private Integer planStatus;
    /**
     * 是否展期还款计划,0:否，1:是
     */
	@TableField("is_defer")
	@ApiModelProperty(required= true,value = "是否展期还款计划,0:否，1:是")
	private Integer isDefer;
    /**
     * 是否有效状态：1 有效 ，0 无效
     */
	@ApiModelProperty(required= true,value = "是否有效状态：1 有效 ，0 无效")
	private Integer active;
    /**
     * 创建系统类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("creat_sys_type")
	@ApiModelProperty(required= true,value = "创建系统类型：1.信贷生成，2.贷后管理生成")
	private Integer creatSysType;
    /**
     * 平台标志：1.团贷网，2.你我金融
     */
	@TableField("plate_type")
	@ApiModelProperty(required= true,value = "平台标志：1.团贷网，2.你我金融")
	private Integer plateType;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;

	/**
	 * 满标时间
	 */
	@TableField("query_full_success_date")
	@ApiModelProperty(required= true,value = "满标时间")
	private Date queryFullSuccessDate;


	public BigDecimal getOnLineOverDueRate() {
		return onLineOverDueRate;
	}

	public void setOnLineOverDueRate(BigDecimal onLineOverDueRate) {
		this.onLineOverDueRate = onLineOverDueRate;
	}

	public Integer getOnLineOverDueRateUnit() {
		return onLineOverDueRateUnit;
	}

	public void setOnLineOverDueRateUnit(Integer onLineOverDueRateUnit) {
		this.onLineOverDueRateUnit = onLineOverDueRateUnit;
	}

	public BigDecimal getOffLineOutOverDueRate() {
		return offLineOutOverDueRate;
	}

	public void setOffLineOutOverDueRate(BigDecimal offLineOutOverDueRate) {
		this.offLineOutOverDueRate = offLineOutOverDueRate;
	}

	public Integer getOffLineOutOverDueRateUnit() {
		return offLineOutOverDueRateUnit;
	}

	public void setOffLineOutOverDueRateUnit(Integer offLineOutOverDueRateUnit) {
		this.offLineOutOverDueRateUnit = offLineOutOverDueRateUnit;
	}

	public BigDecimal getOffLineInOverDueRate() {
		return offLineInOverDueRate;
	}

	public void setOffLineInOverDueRate(BigDecimal offLineInOverDueRate) {
		this.offLineInOverDueRate = offLineInOverDueRate;
	}

	public Integer getOffLineInOverDueRateUnit() {
		return offLineInOverDueRateUnit;
	}

	public void setOffLineInOverDueRateUnit(Integer offLineInOverDueRateUnit) {
		this.offLineInOverDueRateUnit = offLineInOverDueRateUnit;
	}

	public Date getQueryFullSuccessDate() {
		return queryFullSuccessDate;
	}

	public void setQueryFullSuccessDate(Date queryFullSuccessDate) {
		this.queryFullSuccessDate = queryFullSuccessDate;
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

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getRepaymentBatchId() {
		return repaymentBatchId;
	}

	public void setRepaymentBatchId(String repaymentBatchId) {
		this.repaymentBatchId = repaymentBatchId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public BigDecimal getBorrowRate() {
		return borrowRate;
	}

	public void setBorrowRate(BigDecimal borrowRate) {
		this.borrowRate = borrowRate;
	}

	public Integer getBorrowRateUnit() {
		return borrowRateUnit;
	}

	public void setBorrowRateUnit(Integer borrowRateUnit) {
		this.borrowRateUnit = borrowRateUnit;
	}

	public Integer getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public Integer getBorrowLimitUnit() {
		return borrowLimitUnit;
	}

	public void setBorrowLimitUnit(Integer borrowLimitUnit) {
		this.borrowLimitUnit = borrowLimitUnit;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public Integer getIsDefer() {
		return isDefer;
	}

	public void setIsDefer(Integer isDefer) {
		this.isDefer = isDefer;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getCreatSysType() {
		return creatSysType;
	}

	public void setCreatSysType(Integer creatSysType) {
		this.creatSysType = creatSysType;
	}

	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
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
		return this.projPlanId;
	}

	@Override
	public String toString() {
		return "RepaymentProjPlan{" +
			", projPlanId=" + projPlanId +
			", projectId=" + projectId +
			", businessId=" + businessId +
			", originalBusinessId=" + originalBusinessId +
			", repaymentBatchId=" + repaymentBatchId +
			", planId=" + planId +
			", borrowMoney=" + borrowMoney +
			", borrowRate=" + borrowRate +
			", borrowRateUnit=" + borrowRateUnit +
			", onLineOverDueRate=" + onLineOverDueRate +
			", onLineOverDueRateUnit=" + onLineOverDueRateUnit +
			", offLineOutOverDueRate=" + offLineOutOverDueRate +
			", offLineOutOverDueRateUnit=" + offLineOutOverDueRateUnit +
			", offLineInOverDueRate=" + offLineInOverDueRate +
			", offLineInOverDueRateUnit=" + offLineInOverDueRateUnit +
			", borrowLimit=" + borrowLimit +
			", borrowLimitUnit=" + borrowLimitUnit +
			", planStatus=" + planStatus +
			", isDefer=" + isDefer +
			", active=" + active +
			", creatSysType=" + creatSysType +
			", plateType=" + plateType +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", queryFullSuccessDate=" + queryFullSuccessDate +
			"}";
	}
}
