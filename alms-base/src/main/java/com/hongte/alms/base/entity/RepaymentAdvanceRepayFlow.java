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
 * 还垫付流水表
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-26
 */
@ApiModel
@TableName("tb_repayment_advance_repay_flow")
public class RepaymentAdvanceRepayFlow extends Model<RepaymentAdvanceRepayFlow> {

    private static final long serialVersionUID = 1L;

    /**
     * Id,自增
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "Id,自增")
	private Long id;
    /**
     * 标号
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标号")
	private String projectId;
    /**
     * 期数
     */
	@ApiModelProperty(required= true,value = "期数")
	private Integer period;
    /**
     * 还款状态1 已结清 0逾期
     */
	@TableField("repay_status")
	@ApiModelProperty(required= true,value = "还款状态1 已结清 0逾期")
	private Integer repayStatus;
    /**
     * 还款id
     */
	@TableField("repay_id")
	@ApiModelProperty(required= true,value = "还款id")
	private Long repayId;
    /**
     * 还款日期yyyy-MM-dd HH:mm:ss
     */
	@TableField("refund_date")
	@ApiModelProperty(required= true,value = "还款日期yyyy-MM-dd HH:mm:ss")
	private String refundDate;
    /**
     * 还款总金额
     */
	@TableField("total_amount")
	@ApiModelProperty(required= true,value = "还款总金额")
	private BigDecimal totalAmount;
    /**
     * 本金利息
     */
	@TableField("principal_andinterest")
	@ApiModelProperty(required= true,value = "本金利息")
	private BigDecimal principalAndinterest;
    /**
     * 实还平台服务费
     */
	@TableField("tuandai_amount")
	@ApiModelProperty(required= true,value = "实还平台服务费")
	private BigDecimal tuandaiAmount;
    /**
     * 实还资产端服务费
     */
	@TableField("org_amount")
	@ApiModelProperty(required= true,value = "实还资产端服务费")
	private BigDecimal orgAmount;
    /**
     * 实还担保公司服务费
     */
	@TableField("guarantee_amount")
	@ApiModelProperty(required= true,value = "实还担保公司服务费")
	private BigDecimal guaranteeAmount;
    /**
     * 实还仲裁服务费
     */
	@TableField("arbitration_amount")
	@ApiModelProperty(required= true,value = "实还仲裁服务费")
	private BigDecimal arbitrationAmount;
    /**
     * 逾期费用（罚息）
     */
	@TableField("overdue_amount")
	@ApiModelProperty(required= true,value = "逾期费用（罚息）")
	private BigDecimal overdueAmount;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_man")
	@ApiModelProperty(required= true,value = "创建人")
	private String createMan;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_man")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateMan;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}

	public Long getRepayId() {
		return repayId;
	}

	public void setRepayId(Long repayId) {
		this.repayId = repayId;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPrincipalAndinterest() {
		return principalAndinterest;
	}

	public void setPrincipalAndinterest(BigDecimal principalAndinterest) {
		this.principalAndinterest = principalAndinterest;
	}

	public BigDecimal getTuandaiAmount() {
		return tuandaiAmount;
	}

	public void setTuandaiAmount(BigDecimal tuandaiAmount) {
		this.tuandaiAmount = tuandaiAmount;
	}

	public BigDecimal getOrgAmount() {
		return orgAmount;
	}

	public void setOrgAmount(BigDecimal orgAmount) {
		this.orgAmount = orgAmount;
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

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "RepaymentAdvanceRepayFlow{" +
			", id=" + id +
			", projectId=" + projectId +
			", period=" + period +
			", repayStatus=" + repayStatus +
			", repayId=" + repayId +
			", refundDate=" + refundDate +
			", totalAmount=" + totalAmount +
			", principalAndinterest=" + principalAndinterest +
			", tuandaiAmount=" + tuandaiAmount +
			", orgAmount=" + orgAmount +
			", guaranteeAmount=" + guaranteeAmount +
			", arbitrationAmount=" + arbitrationAmount +
			", overdueAmount=" + overdueAmount +
			", createTime=" + createTime +
			", createMan=" + createMan +
			", updateTime=" + updateTime +
			", updateMan=" + updateMan +
			", lastPushStatus=" + lastPushStatus +
			", lastPushDatetime=" + lastPushDatetime +
			", lastPushRemark=" + lastPushRemark +
			"}";
	}
}
