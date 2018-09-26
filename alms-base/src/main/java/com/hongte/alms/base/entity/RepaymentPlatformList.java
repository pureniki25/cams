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
 * 平台还款流水表
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-25
 */
@ApiModel
@TableName("tb_repayment_platform_list")
public class RepaymentPlatformList extends Model<RepaymentPlatformList> {

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
     * 还款日期
     */
	@TableField("add_date")
	@ApiModelProperty(required= true,value = "还款日期")
	private String addDate;
    /**
     * 实还总金额
     */
	@TableField("total_amount")
	@ApiModelProperty(required= true,value = "实还总金额")
	private BigDecimal totalAmount;
    /**
     * 实还本息
     */
	@TableField("principal_andinterest")
	@ApiModelProperty(required= true,value = "实还本息")
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
     * 实还中介服务费
     */
	@TableField("agency_amount")
	@ApiModelProperty(required= true,value = "实还中介服务费")
	private BigDecimal agencyAmount;
    /**
     * 保险费
     */
	@TableField("insurance_amount")
	@ApiModelProperty(required= true,value = "保险费")
	private BigDecimal insuranceAmount;
    /**
     * 特权包
     */
	@TableField("privilege_package_amount")
	@ApiModelProperty(required= true,value = "特权包")
	private BigDecimal privilegePackageAmount;
    /**
     * 滞纳金
     */
	@TableField("penalty_amount")
	@ApiModelProperty(required= true,value = "滞纳金")
	private BigDecimal penaltyAmount;
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

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
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

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(BigDecimal insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public BigDecimal getPrivilegePackageAmount() {
		return privilegePackageAmount;
	}

	public void setPrivilegePackageAmount(BigDecimal privilegePackageAmount) {
		this.privilegePackageAmount = privilegePackageAmount;
	}

	public BigDecimal getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
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
		return "RepaymentPlatformList{" +
			", id=" + id +
			", projectId=" + projectId +
			", period=" + period +
			", repayStatus=" + repayStatus +
			", addDate=" + addDate +
			", totalAmount=" + totalAmount +
			", principalAndinterest=" + principalAndinterest +
			", tuandaiAmount=" + tuandaiAmount +
			", orgAmount=" + orgAmount +
			", guaranteeAmount=" + guaranteeAmount +
			", arbitrationAmount=" + arbitrationAmount +
			", agencyAmount=" + agencyAmount +
			", insuranceAmount=" + insuranceAmount +
			", privilegePackageAmount=" + privilegePackageAmount +
			", penaltyAmount=" + penaltyAmount +
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
