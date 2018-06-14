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
 * 调用团贷网偿还垫付接口记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-12
 */
@ApiModel
@TableName("tb_tdrepay_Advance_log")
public class TdrepayAdvanceLog extends Model<TdrepayAdvanceLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * 标的ID
     */
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 本次还款对应团贷网的期次
     */
	@ApiModelProperty(required= true,value = "本次还款对应团贷网的期次")
	private Integer period;
    /**
     * 结清状态(1:已结清;0:未结清)
     */
	@ApiModelProperty(required= true,value = "结清状态(1:已结清;0:未结清)")
	private Integer status;
    /**
     * 还款总金额
     */
	@TableField("total_amount")
	@ApiModelProperty(required= true,value = "还款总金额")
	private BigDecimal totalAmount;
    /**
     * 本金+利息
     */
	@TableField("principal_and_interest")
	@ApiModelProperty(required= true,value = "本金+利息")
	private BigDecimal principalAndInterest;
    /**
     * 平台服务费
     */
	@TableField("tuandai_amount")
	@ApiModelProperty(required= true,value = "平台服务费")
	private BigDecimal tuandaiAmount;
    /**
     * 资产端服务费
     */
	@TableField("org_amount")
	@ApiModelProperty(required= true,value = "资产端服务费")
	private BigDecimal orgAmount;
    /**
     * 担保公司服务费
     */
	@TableField("guarantee_amount")
	@ApiModelProperty(required= true,value = "担保公司服务费")
	private BigDecimal guaranteeAmount;
    /**
     * 仲裁服务费
     */
	@TableField("arbitration_amount")
	@ApiModelProperty(required= true,value = "仲裁服务费")
	private BigDecimal arbitrationAmount;
    /**
     * 逾期费用（罚息）
     */
	@TableField("over_due_amount")
	@ApiModelProperty(required= true,value = "逾期费用（罚息）")
	private BigDecimal overDueAmount;
    /**
     * 偿还垫付状态：1、成功，2：失败
     */
	@TableField("advance_status")
	@ApiModelProperty(required= true,value = "偿还垫付状态：1、成功，2：失败")
	private Integer advanceStatus;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPrincipalAndInterest() {
		return principalAndInterest;
	}

	public void setPrincipalAndInterest(BigDecimal principalAndInterest) {
		this.principalAndInterest = principalAndInterest;
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

	public BigDecimal getOverDueAmount() {
		return overDueAmount;
	}

	public void setOverDueAmount(BigDecimal overDueAmount) {
		this.overDueAmount = overDueAmount;
	}

	public Integer getAdvanceStatus() {
		return advanceStatus;
	}

	public void setAdvanceStatus(Integer advanceStatus) {
		this.advanceStatus = advanceStatus;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TdrepayAdvanceLog{" +
			", id=" + id +
			", projectId=" + projectId +
			", period=" + period +
			", status=" + status +
			", totalAmount=" + totalAmount +
			", principalAndInterest=" + principalAndInterest +
			", tuandaiAmount=" + tuandaiAmount +
			", orgAmount=" + orgAmount +
			", guaranteeAmount=" + guaranteeAmount +
			", arbitrationAmount=" + arbitrationAmount +
			", overDueAmount=" + overDueAmount +
			", advanceStatus=" + advanceStatus +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
