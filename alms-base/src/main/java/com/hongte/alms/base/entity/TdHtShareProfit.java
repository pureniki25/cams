package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 平台应还计划表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-12-09
 */
@ApiModel
@TableName("tb_td_ht_share_profit")
public class TdHtShareProfit extends Model<TdHtShareProfit> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键")
	private Integer id;
    /**
     * 标的id
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的id")
	private String projectId;
    /**
     * 期次
     */
	@ApiModelProperty(required= true,value = "期次")
	private Integer period;
    /**
     * 还款日期
     */
	@TableField("cyc_date")
	@ApiModelProperty(required= true,value = "还款日期")
	private String cycDate;
    /**
     * 本金
     */
	@ApiModelProperty(required= true,value = "本金")
	private BigDecimal amount;
    /**
     * 利息
     */
	@TableField("interest_amount")
	@ApiModelProperty(required= true,value = "利息")
	private BigDecimal interestAmount;
    /**
     * 保证金
     */
	@TableField("deposit_amount")
	@ApiModelProperty(required= true,value = "保证金")
	private BigDecimal depositAmount;
    /**
     * 担保费
     */
	@TableField("guarantee_amount")
	@ApiModelProperty(required= true,value = "担保费")
	private BigDecimal guaranteeAmount;
    /**
     * 仲裁费
     */
	@TableField("arbitration_amount")
	@ApiModelProperty(required= true,value = "仲裁费")
	private BigDecimal arbitrationAmount;
    /**
     * 资产端服务费
     */
	@TableField("org_amount")
	@ApiModelProperty(required= true,value = "资产端服务费")
	private BigDecimal orgAmount;
    /**
     * 平台服务费
     */
	@TableField("tuandai_amount")
	@ApiModelProperty(required= true,value = "平台服务费")
	private BigDecimal tuandaiAmount;
    /**
     * 中介服务费
     */
	@TableField("agency_amount")
	@ApiModelProperty(required= true,value = "中介服务费")
	private BigDecimal agencyAmount;
    /**
     * 其他费用
     */
	@TableField("other_amount")
	@ApiModelProperty(required= true,value = "其他费用")
	private BigDecimal otherAmount;
    /**
     * 总金额
     */
	@TableField("total_amount")
	@ApiModelProperty(required= true,value = "总金额")
	private BigDecimal totalAmount;
    /**
     * .net接口还款状态
     */
	@TableField("repayment_status")
	@ApiModelProperty(required= true,value = ".net接口还款状态")
	private String repaymentStatus;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getCycDate() {
		return cycDate;
	}

	public void setCycDate(String cycDate) {
		this.cycDate = cycDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public BigDecimal getGuaranteeAmount() {
		return guaranteeAmount;
	}

	public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
		this.guaranteeAmount = guaranteeAmount;
	}

	public BigDecimal getArbitrationAmount() {
		return arbitrationAmount;
	}

	public void setArbitrationAmount(BigDecimal arbitrationAmount) {
		this.arbitrationAmount = arbitrationAmount;
	}

	public BigDecimal getOrgAmount() {
		return orgAmount;
	}

	public void setOrgAmount(BigDecimal orgAmount) {
		this.orgAmount = orgAmount;
	}

	public BigDecimal getTuandaiAmount() {
		return tuandaiAmount;
	}

	public void setTuandaiAmount(BigDecimal tuandaiAmount) {
		this.tuandaiAmount = tuandaiAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRepaymentStatus() {
		return repaymentStatus;
	}

	public void setRepaymentStatus(String repaymentStatus) {
		this.repaymentStatus = repaymentStatus;
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
		return this.id;
	}

	@Override
	public String toString() {
		return "TdHtShareProfit{" +
			", id=" + id +
			", projectId=" + projectId +
			", period=" + period +
			", cycDate=" + cycDate +
			", amount=" + amount +
			", interestAmount=" + interestAmount +
			", depositAmount=" + depositAmount +
			", guaranteeAmount=" + guaranteeAmount +
			", arbitrationAmount=" + arbitrationAmount +
			", orgAmount=" + orgAmount +
			", tuandaiAmount=" + tuandaiAmount +
			", agencyAmount=" + agencyAmount +
			", otherAmount=" + otherAmount +
			", totalAmount=" + totalAmount +
			", repaymentStatus=" + repaymentStatus +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
