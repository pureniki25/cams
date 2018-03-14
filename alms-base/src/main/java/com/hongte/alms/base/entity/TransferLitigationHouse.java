package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 房贷移交法务信息表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-09
 */
@ApiModel
@TableName("tb_transfer_litigation_house")
public class TransferLitigationHouse extends Model<TransferLitigationHouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务编号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 流程ID
     */
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "流程ID")
	private String processId;
    /**
     * 用户还款计划ID
     */
	@TableField("crp_id")
	@ApiModelProperty(required= true,value = "用户还款计划ID")
	private String crpId;
    /**
     * 客户逾期情况描述
     */
	@TableField("overdue_desc")
	@ApiModelProperty(required= true,value = "客户逾期情况描述")
	private String overdueDesc;
    /**
     * 未能覆盖的风险口
     */
	@TableField("risk_desc")
	@ApiModelProperty(required= true,value = "未能覆盖的风险口")
	private String riskDesc;
    /**
     * 其他质押情况
     */
	@TableField("other_pledge_desc")
	@ApiModelProperty(required= true,value = "其他质押情况")
	private String otherPledgeDesc;
    /**
     * 转账收据张数
     */
	@TableField("transfer_receipt_count")
	@ApiModelProperty(required= true,value = "转账收据张数")
	private Integer transferReceiptCount;
    /**
     * 现金收据张数
     */
	@TableField("cash_receipt_count")
	@ApiModelProperty(required= true,value = "现金收据张数")
	private Integer cashReceiptCount;
    /**
     * 还款收据第二联是否在客户手上
     */
	@TableField("receipt_status")
	@ApiModelProperty(required= true,value = "还款收据第二联是否在客户手上")
	private String receiptStatus;
    /**
     * 业务合同有无特殊情况
     */
	@TableField("contract_desc")
	@ApiModelProperty(required= true,value = "业务合同有无特殊情况")
	private String contractDesc;
    /**
     * 客户是否有意向还款
     */
	@TableField("repayment_intention")
	@ApiModelProperty(required= true,value = "客户是否有意向还款")
	private String repaymentIntention;
    /**
     * 意向还款计划
     */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@TableField("repayment_intention_date")
	@ApiModelProperty(required= true,value = "意向还款计划")
	private Date repaymentIntentionDate;
    /**
     * 借款人相关人员描述（业务经办调查结果）
     */
	@TableField("first_related_personnel")
	@ApiModelProperty(required= true,value = "借款人相关人员描述（业务经办调查结果）")
	private String firstRelatedPersonnel;
    /**
     * 资产管理部经办人催收调查结果（分公司/区域贷后跟进结果）
     */
	@TableField("second_related_personnel")
	@ApiModelProperty(required= true,value = "资产管理部经办人催收调查结果（分公司/区域贷后跟进结果）")
	private String secondRelatedPersonnel;
    /**
     * 其他财务情况（业务经办调查结果）
     */
	@TableField("first_pecuniary_condition")
	@ApiModelProperty(required= true,value = "其他财务情况（业务经办调查结果）")
	private String firstPecuniaryCondition;
    /**
     * 其他财务情况（分公司/区域贷后跟进结果）
     */
	@TableField("second_pecuniary_condition")
	@ApiModelProperty(required= true,value = "其他财务情况（分公司/区域贷后跟进结果）")
	private String secondPecuniaryCondition;
    /**
     * 移交人
     */
	@ApiModelProperty(required= true,value = "移交人")
	private String operator;
    /**
     * 移交时间
     */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@TableField("operate_date")
	@ApiModelProperty(required= true,value = "移交时间")
	private Date operateDate;
    /**
     * 流程状态
     */
	@TableField("process_status")
	@ApiModelProperty(required= true,value = "流程状态")
	private String processStatus;
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
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	public String getOverdueDesc() {
		return overdueDesc;
	}

	public void setOverdueDesc(String overdueDesc) {
		this.overdueDesc = overdueDesc;
	}

	public String getRiskDesc() {
		return riskDesc;
	}

	public void setRiskDesc(String riskDesc) {
		this.riskDesc = riskDesc;
	}

	public String getOtherPledgeDesc() {
		return otherPledgeDesc;
	}

	public void setOtherPledgeDesc(String otherPledgeDesc) {
		this.otherPledgeDesc = otherPledgeDesc;
	}

	public Integer getTransferReceiptCount() {
		return transferReceiptCount;
	}

	public void setTransferReceiptCount(Integer transferReceiptCount) {
		this.transferReceiptCount = transferReceiptCount;
	}

	public Integer getCashReceiptCount() {
		return cashReceiptCount;
	}

	public void setCashReceiptCount(Integer cashReceiptCount) {
		this.cashReceiptCount = cashReceiptCount;
	}

	public String getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

	public String getContractDesc() {
		return contractDesc;
	}

	public void setContractDesc(String contractDesc) {
		this.contractDesc = contractDesc;
	}

	public String getRepaymentIntention() {
		return repaymentIntention;
	}

	public void setRepaymentIntention(String repaymentIntention) {
		this.repaymentIntention = repaymentIntention;
	}

	public Date getRepaymentIntentionDate() {
		return repaymentIntentionDate;
	}

	public void setRepaymentIntentionDate(Date repaymentIntentionDate) {
		this.repaymentIntentionDate = repaymentIntentionDate;
	}

	public String getFirstRelatedPersonnel() {
		return firstRelatedPersonnel;
	}

	public void setFirstRelatedPersonnel(String firstRelatedPersonnel) {
		this.firstRelatedPersonnel = firstRelatedPersonnel;
	}

	public String getSecondRelatedPersonnel() {
		return secondRelatedPersonnel;
	}

	public void setSecondRelatedPersonnel(String secondRelatedPersonnel) {
		this.secondRelatedPersonnel = secondRelatedPersonnel;
	}

	public String getFirstPecuniaryCondition() {
		return firstPecuniaryCondition;
	}

	public void setFirstPecuniaryCondition(String firstPecuniaryCondition) {
		this.firstPecuniaryCondition = firstPecuniaryCondition;
	}

	public String getSecondPecuniaryCondition() {
		return secondPecuniaryCondition;
	}

	public void setSecondPecuniaryCondition(String secondPecuniaryCondition) {
		this.secondPecuniaryCondition = secondPecuniaryCondition;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "TransferLitigationHouse{" +
			", businessId=" + businessId +
			", processId=" + processId +
			", crpId=" + crpId +
			", overdueDesc=" + overdueDesc +
			", riskDesc=" + riskDesc +
			", otherPledgeDesc=" + otherPledgeDesc +
			", transferReceiptCount=" + transferReceiptCount +
			", cashReceiptCount=" + cashReceiptCount +
			", receiptStatus=" + receiptStatus +
			", contractDesc=" + contractDesc +
			", repaymentIntention=" + repaymentIntention +
			", repaymentIntentionDate=" + repaymentIntentionDate +
			", firstRelatedPersonnel=" + firstRelatedPersonnel +
			", secondRelatedPersonnel=" + secondRelatedPersonnel +
			", firstPecuniaryCondition=" + firstPecuniaryCondition +
			", secondPecuniaryCondition=" + secondPecuniaryCondition +
			", operator=" + operator +
			", operateDate=" + operateDate +
			", processStatus=" + processStatus +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
