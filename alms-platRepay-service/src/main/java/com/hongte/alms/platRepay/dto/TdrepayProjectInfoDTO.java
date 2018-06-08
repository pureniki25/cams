package com.hongte.alms.platRepay.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TdrepayProjectInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 发标人ID
	 */
	private String publishUserId;
	/**
	 * 借款利率
	 */
	private Double interestRate;
	/**
	 * 借款期限
	 */
	private int deadline;
	/**
	 * 还款方式 2:每月付息
	 */
	private int repaymentType;
	/**
	 * 借款总额（元）
	 */
	private Double amount;
	/**
	 * 应还本息
	 */
	private String repayAmount;
	/**
	 * 应还总期数
	 */
	private int totalRefundMonths;
	/**
	 * 是否满标1:满标 0：未满标
	 */
	private int isProjectFull;
	/**
	 * 样式：yyyy-MM-dd HH:mm:ss
	 */
	private String projectFullDate;
	/**
	 * 审核时间
	 */
	private Date auditDate;
	/**
	 * 应还明细
	 */
	private List<TdrepayProjectPeriodInfoDTO> periodsList;

	public String getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(String repayAmount) {
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

	public String getProjectFullDate() {
		return projectFullDate;
	}

	public void setProjectFullDate(String projectFullDate) {
		this.projectFullDate = projectFullDate;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public List<TdrepayProjectPeriodInfoDTO> getPeriodsList() {
		return periodsList;
	}

	public void setPeriodsList(List<TdrepayProjectPeriodInfoDTO> periodsList) {
		this.periodsList = periodsList;
	}

	@Override
	public String toString() {
		return "TdrepayProjectInfoDTO [publishUserId=" + publishUserId + ", interestRate=" + interestRate
				+ ", deadline=" + deadline + ", repaymentType=" + repaymentType + ", amount=" + amount
				+ ", repayAmount=" + repayAmount + ", totalRefundMonths=" + totalRefundMonths + ", isProjectFull="
				+ isProjectFull + ", projectFullDate=" + projectFullDate + ", auditDate=" + auditDate + ", periodsList="
				+ periodsList + "]";
	}

}
