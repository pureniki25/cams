package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 标的还款计划列表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@ApiModel
@TableName("tb_repayment_proj_plan_list")
public class RepaymentProjPlanList extends Model<RepaymentProjPlanList> {

    private static final long serialVersionUID = 1L;

    /**
     * 标的还款计划列表ID(主键)
     */
    @TableId("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "标的还款计划列表ID(主键)")
	private String projPlanListId;
    /**
     * 所属标的还款计划编号(外键，对应tb_repayment_proj_plan.proj_plan_id)
     */
	@TableField("proj_plan_id")
	@ApiModelProperty(required= true,value = "所属标的还款计划编号(外键，对应tb_repayment_proj_plan.proj_plan_id)")
	private String projPlanId;
    /**
     * 对应业务还款计划列表编号(外键，对应tb_repayment_biz_plan_list.plan_list_id)
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "对应业务还款计划列表编号(外键，对应tb_repayment_biz_plan_list.plan_list_id)")
	private String planListId;
    /**
     * 所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
     */
	@TableField("plan_id")
	@ApiModelProperty(required= true,value = "所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)")
	private String planId;
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
     * 当前还款计划期数，若期数为0，表示为展期还款计划第0期或者线下出款业务的第0期
     */
	@ApiModelProperty(required= true,value = "当前还款计划期数，若期数为0，表示为展期还款计划第0期或者线下出款业务的第0期")
	private Integer period;
    /**
     * 总批次期数，唯一，对应信贷系统的还款计划编号
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "总批次期数，唯一，对应信贷系统的还款计划编号")
	private String afterId;
    /**
     * 应还日期
     */
	@TableField("due_date")
	@ApiModelProperty(required= true,value = "应还日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dueDate;
    /**
     * 总计划应还金额(元)，不含滞纳金
     */
	@TableField("total_borrow_amount")
	@ApiModelProperty(required= true,value = "总计划应还金额(元)，不含滞纳金")
	private BigDecimal totalBorrowAmount;
    /**
     * 总应还滞纳金(元)，每天零点由系统自动计算
     */
	@TableField("overdue_amount")
	@ApiModelProperty(required= true,value = "总应还滞纳金(元)，每天零点由系统自动计算")
	private BigDecimal overdueAmount;
	/**
     * 减免金额
     */
	@TableField("derate_amount")
	@ApiModelProperty(required= true,value = "减免金额")
	private BigDecimal derateAmount;
	
    /**
     * 逾期天数，每天零点由系统自动计算
     */
	@TableField("overdue_days")
	@ApiModelProperty(required= true,value = "逾期天数，每天零点由系统自动计算")
	private BigDecimal overdueDays;
    /**
     * 当前还款状态，目前只有三种，分别为"还款中"，"逾期"，"已还款"
     */
	@TableField("current_status")
	@ApiModelProperty(required= true,value = "当前还款状态，目前只有三种，分别为还款中，逾期，已还款")
	private String currentStatus;
    /**
     * 当前还款子状态
     */
	@TableField("current_sub_status")
	@ApiModelProperty(required= true,value = "当前还款子状态")
	private String currentSubStatus;
    /**
     * 已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清
     */
	@TableField("repay_flag")
	@ApiModelProperty(required= true,value = "已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清")
	private Integer repayFlag;
    /**
     * 客户实还日期
     */
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "客户实还日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date factRepayDate;

    /**
     * 还款备注
     */
	@ApiModelProperty(required= true,value = "还款备注")
	private String remark;

    /**
     * 生成系统类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("creat_sys_type")
	@ApiModelProperty(required= true,value = "生成系统类型：1.信贷生成，2.贷后管理生成")
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
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
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
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


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

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotalBorrowAmount() {
		return totalBorrowAmount;
	}

	public void setTotalBorrowAmount(BigDecimal totalBorrowAmount) {
		this.totalBorrowAmount = totalBorrowAmount;
	}

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public BigDecimal getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(BigDecimal overdueDays) {
		this.overdueDays = overdueDays;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentSubStatus() {
		return currentSubStatus;
	}

	public void setCurrentSubStatus(String currentSubStatus) {
		this.currentSubStatus = currentSubStatus;
	}

	public Integer getRepayFlag() {
		return repayFlag;
	}

	public void setRepayFlag(Integer repayFlag) {
		this.repayFlag = repayFlag;
	}

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return this.projPlanListId;
	}

	@Override
	public String toString() {
		return "RepaymentProjPlanList{" +
			", projPlanListId=" + projPlanListId +
			", projPlanId=" + projPlanId +
			", planListId=" + planListId +
			", planId=" + planId +
			", businessId=" + businessId +
			", origBusinessId=" + origBusinessId +
			", period=" + period +
			", afterId=" + afterId +
			", dueDate=" + dueDate +
			", totalBorrowAmount=" + totalBorrowAmount +
			", overdueAmount=" + overdueAmount +
			", overdueDays=" + overdueDays +
			", currentStatus=" + currentStatus +
			", currentSubStatus=" + currentSubStatus +
			", repayFlag=" + repayFlag +
			", factRepayDate=" + factRepayDate +
			", remark=" + remark +
			", creatSysType=" + creatSysType +
			", plateType=" + plateType +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}

	/**
	 * @return the derateAmount
	 */
	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	/**
	 * @param derateAmount the derateAmount to set
	 */
	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
}
