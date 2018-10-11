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
 * 业务还款计划列表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@ApiModel
@TableName("tb_repayment_biz_plan_list")
public class RepaymentBizPlanList extends Model<RepaymentBizPlanList> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款计划列表ID(主键)
     */
    @TableId("plan_list_id")
	@ApiModelProperty(required= true,value = "还款计划列表ID(主键)")
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
     * 部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
     */
	@TableField("repay_status")
	@ApiModelProperty(required= true,value = "部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款")
	private Integer repayStatus;
	
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
     * 财务确认还款操作日期
     */
	@TableField("finance_comfirm_date")
	@ApiModelProperty(required= true,value = "财务确认还款操作日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date financeComfirmDate;
    /**
     * 财务还款确认人ID
     */
	@TableField("finance_confirm_user")
	@ApiModelProperty(required= true,value = "财务还款确认人ID")
	private String financeConfirmUser;
    /**
     * 财务还款确认人名称
     */
	@TableField("finance_confirm_user_name")
	@ApiModelProperty(required= true,value = "财务还款确认人名称")
	private String financeConfirmUserName;
    /**
     * 财务还款金额确认(1:已确认,0:未确认)
     */
	@TableField("confirm_flag")
	@ApiModelProperty(required= true,value = "财务还款金额确认(1:已确认,0:未确认)")
	private Integer confirmFlag;
    /**
     * 财务确认自动代扣日期
     */
	@TableField("auto_withholding_confirmed_date")
	@ApiModelProperty(required= true,value = "财务确认自动代扣日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date autoWithholdingConfirmedDate;
    /**
     * 确认自动代扣的确认者ID
     */
	@TableField("auto_withholding_confirmed_user")
	@ApiModelProperty(required= true,value = "确认自动代扣的确认者ID")
	private String autoWithholdingConfirmedUser;
    /**
     * 确认自动代扣的确认者姓名
     */
	@TableField("auto_withholding_confirmed_user_name")
	@ApiModelProperty(required= true,value = "确认自动代扣的确认者姓名")
	private String autoWithholdingConfirmedUserName;
    /**
     * 会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
     */
	@TableField("accountant_confirm_status")
	@ApiModelProperty(required= true,value = "会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;")
	private Integer accountantConfirmStatus;
    /**
     * 会计确认人ID
     */
	@TableField("accountant_confirm_user")
	@ApiModelProperty(required= true,value = "会计确认人ID")
	private String accountantConfirmUser;
    /**
     * 会计确认人姓名
     */
	@TableField("accountant_confirm_user_name")
	@ApiModelProperty(required= true,value = "会计确认人姓名")
	private String accountantConfirmUserName;
    /**
     * 会计确认日期
     */
	@TableField("accountant_confirm_date")
	@ApiModelProperty(required= true,value = "会计确认日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date accountantConfirmDate;
    /**
     * 还款备注
     */
	@ApiModelProperty(required= true,value = "还款备注")
	private String remark;

    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
    /**
     * 来源类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("src_type")
	@ApiModelProperty(required= true,value = "来源类型：1.信贷生成，2.贷后管理生成")
	private Integer srcType;
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
	
    /**
     * 最后推送状态0未推送1流水推送成功2推送失败3不需要推送4撤销推送成功5撤销推送失败
     */
	@TableField("last_push_status")
	@ApiModelProperty(required= true,value = "最后推送状态0未推送1流水推送成功2推送失败3不需要推送4撤销推送成功5撤销推送失败")
	private Integer lastPushStatus;
    /**
     * 最后推送时间
     */
	@TableField("last_push_datetime")
	@ApiModelProperty(required= true,value = "最后推送时间")
	private Date lastPushDatetime;
    /**
     * 最后推送备注
     */
	@TableField("last_push_remark")
	@ApiModelProperty(required= true,value = "最后推送备注")
	private String lastPushRemark;
	/**
     * 更新用户
     */
	@TableField("is_running")
	@ApiModelProperty(required= true,value = "是否正在被执行 0：否  1：是")
	private Integer isRunning;
     


	public Integer getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Integer isRunning) {
		this.isRunning = isRunning;
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

	public Date getFinanceComfirmDate() {
		return financeComfirmDate;
	}

	public void setFinanceComfirmDate(Date financeComfirmDate) {
		this.financeComfirmDate = financeComfirmDate;
	}

	public String getFinanceConfirmUser() {
		return financeConfirmUser;
	}

	public void setFinanceConfirmUser(String financeConfirmUser) {
		this.financeConfirmUser = financeConfirmUser;
	}

	public String getFinanceConfirmUserName() {
		return financeConfirmUserName;
	}

	public void setFinanceConfirmUserName(String financeConfirmUserName) {
		this.financeConfirmUserName = financeConfirmUserName;
	}

	public Integer getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public Date getAutoWithholdingConfirmedDate() {
		return autoWithholdingConfirmedDate;
	}

	public void setAutoWithholdingConfirmedDate(Date autoWithholdingConfirmedDate) {
		this.autoWithholdingConfirmedDate = autoWithholdingConfirmedDate;
	}

	public String getAutoWithholdingConfirmedUser() {
		return autoWithholdingConfirmedUser;
	}

	public void setAutoWithholdingConfirmedUser(String autoWithholdingConfirmedUser) {
		this.autoWithholdingConfirmedUser = autoWithholdingConfirmedUser;
	}

	public String getAutoWithholdingConfirmedUserName() {
		return autoWithholdingConfirmedUserName;
	}

	public void setAutoWithholdingConfirmedUserName(String autoWithholdingConfirmedUserName) {
		this.autoWithholdingConfirmedUserName = autoWithholdingConfirmedUserName;
	}

	public Integer getAccountantConfirmStatus() {
		return accountantConfirmStatus;
	}

	public void setAccountantConfirmStatus(Integer accountantConfirmStatus) {
		this.accountantConfirmStatus = accountantConfirmStatus;
	}

	public String getAccountantConfirmUser() {
		return accountantConfirmUser;
	}

	public void setAccountantConfirmUser(String accountantConfirmUser) {
		this.accountantConfirmUser = accountantConfirmUser;
	}

	public String getAccountantConfirmUserName() {
		return accountantConfirmUserName;
	}

	public void setAccountantConfirmUserName(String accountantConfirmUserName) {
		this.accountantConfirmUserName = accountantConfirmUserName;
	}

	public Date getAccountantConfirmDate() {
		return accountantConfirmDate;
	}

	public void setAccountantConfirmDate(Date accountantConfirmDate) {
		this.accountantConfirmDate = accountantConfirmDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSrcType() {
		return srcType;
	}

	public void setSrcType(Integer srcType) {
		this.srcType = srcType;
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
		return this.planListId;
	}

	@Override
	public String toString() {
		return "RepaymentBizPlanList{" +
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
			", financeComfirmDate=" + financeComfirmDate +
			", financeConfirmUser=" + financeConfirmUser +
			", financeConfirmUserName=" + financeConfirmUserName +
			", confirmFlag=" + confirmFlag +
			", autoWithholdingConfirmedDate=" + autoWithholdingConfirmedDate +
			", autoWithholdingConfirmedUser=" + autoWithholdingConfirmedUser +
			", autoWithholdingConfirmedUserName=" + autoWithholdingConfirmedUserName +
			", accountantConfirmStatus=" + accountantConfirmStatus +
			", accountantConfirmUser=" + accountantConfirmUser +
			", accountantConfirmUserName=" + accountantConfirmUserName +
			", accountantConfirmDate=" + accountantConfirmDate +
			", remark=" + remark +
			", createTime=" + createTime +
			", srcType=" + srcType +
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

	/**
	 * @return the repayStatus
	 */
	public Integer getRepayStatus() {
		return repayStatus;
	}

	/**
	 * @param repayStatus the repayStatus to set
	 */
	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}

	public Integer getLastPushStatus() {
		return lastPushStatus;
	}

	public void setLastPushStatus(Integer lastPushStatus) {
		this.lastPushStatus = lastPushStatus;
	}

	public Date getLastPushDatetime() {
		return lastPushDatetime;
	}

	public void setLastPushDatetime(Date lastPushDatetime) {
		this.lastPushDatetime = lastPushDatetime;
	}

	public String getLastPushRemark() {
		return lastPushRemark;
	}

	public void setLastPushRemark(String lastPushRemark) {
		this.lastPushRemark = lastPushRemark;
	}

	/**
	 * 
	 */
	public RepaymentBizPlanList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public RepaymentBizPlanList(RepaymentBizPlanListBak pl) {
		super();
		setAccountantConfirmDate(pl.getAccountantConfirmDate());
		setAccountantConfirmStatus(pl.getAccountantConfirmStatus());
		setAccountantConfirmUser(pl.getAccountantConfirmUser());
		setAccountantConfirmUserName(pl.getAccountantConfirmUserName());
		setAfterId(pl.getAfterId());
		setAutoWithholdingConfirmedDate(pl.getAutoWithholdingConfirmedDate());
		setAutoWithholdingConfirmedUser(pl.getAutoWithholdingConfirmedUser());
		setAutoWithholdingConfirmedUserName(pl.getAutoWithholdingConfirmedUserName());
		setBusinessId(pl.getBusinessId());
		setConfirmFlag(pl.getConfirmFlag());
		setCreateTime(pl.getCreateTime());
		setCreateUser(pl.getCreateUser());
		setCurrentStatus(pl.getCurrentStatus());
		setCurrentSubStatus(pl.getCurrentSubStatus());
		setDerateAmount(pl.getDerateAmount());
		setDueDate(pl.getDueDate());
		setFactRepayDate(pl.getFactRepayDate());
		setFinanceComfirmDate(pl.getFinanceComfirmDate());
		setFinanceConfirmUser(pl.getFinanceConfirmUser());
		setFinanceConfirmUserName(pl.getFinanceConfirmUserName());
		setOrigBusinessId(pl.getOrigBusinessId());
		setOverdueAmount(pl.getOverdueAmount());
		setOverdueDays(pl.getOverdueDays());
		setPeriod(pl.getPeriod());
		setPlanId(pl.getPlanId());
		setPlanListId(pl.getPlanListId());
		setRemark(pl.getRemark());
		setRepayFlag(pl.getRepayFlag());
		setRepayStatus(pl.getRepayStatus());
		setSrcType(pl.getSrcType());
		setTotalBorrowAmount(pl.getTotalBorrowAmount());
		setUpdateTime(pl.getUpdateTime());
		setUpdateUser(pl.getUpdateUser());
	}
}
