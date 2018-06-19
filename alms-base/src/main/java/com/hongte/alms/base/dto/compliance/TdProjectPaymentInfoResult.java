package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 调用eip标的还款信息查询接口返回结果 （还款计划， 接口地址/assetside/getProjectPayment）
 * @author 胡伟骞
 *
 */
public class TdProjectPaymentInfoResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 发标人ID
	 */
	private String publishUserId;
	/**
	 * 借款利率
	 */
	private BigDecimal interestRate;
	/**
	 * 借款期限
	 */
	private int deadline;
	/**
	 * 还款方式
	 */
	private int repaymentType;
	/**
	 * 借款总额（元）
	 */
	private BigDecimal amount;
	/**
	 * 应还本息
	 */
	private BigDecimal repayAmount;
	/**
	 * 应还总期数
	 */
	private int totalRefundMonths;
	/**
	 * 是否满标
	 */
	private int isProjectFull;
	/**
	 * 满标日期
	 */
	private int projectFullDate;
	/**
	 * 审核时间
	 */
	private BigDecimal auditDate;
	/**
	 * 应还明细
	 */
	private List<TdRefundMonthInfoDTO> periodsList;

	public String getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public int getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public int getTotalRefundMonths() {
		return totalRefundMonths;
	}

	public void setTotalRefundMonths(int totalRefundMonths) {
		this.totalRefundMonths = totalRefundMonths;
	}

	public int getIsProjectFull() {
		return isProjectFull;
	}

	public void setIsProjectFull(int isProjectFull) {
		this.isProjectFull = isProjectFull;
	}

	public int getProjectFullDate() {
		return projectFullDate;
	}

	public void setProjectFullDate(int projectFullDate) {
		this.projectFullDate = projectFullDate;
	}

	public BigDecimal getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(BigDecimal auditDate) {
		this.auditDate = auditDate;
	}

	public List<TdRefundMonthInfoDTO> getPeriodsList() {
		return periodsList;
	}

	public void setPeriodsList(List<TdRefundMonthInfoDTO> periodsList) {
		this.periodsList = periodsList;
	}

	@Override
	public String toString() {
		return "TdProjectPaymentInfoResult [publishUserId=" + publishUserId + ", interestRate=" + interestRate
				+ ", deadline=" + deadline + ", repaymentType=" + repaymentType + ", amount=" + amount
				+ ", repayAmount=" + repayAmount + ", totalRefundMonths=" + totalRefundMonths + ", isProjectFull="
				+ isProjectFull + ", projectFullDate=" + projectFullDate + ", auditDate=" + auditDate + ", periodsList="
				+ periodsList + "]";
	}

}
