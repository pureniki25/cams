package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.vo.PageRequest;

public class ComplianceRepaymentVO extends PageRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 财务确认开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date confirmTimeStart;
	/**
	 * 财务确认结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date confirmTimeEnd;
	/**
	 * 分发状态
	 */
	private Integer processStatus;
	/**
	 * 还款方式
	 */
	private Integer repaySource;
	/**
	 * 期数类型
	 */
	private Integer settleType;
	/**
	 * 编码
	 */
	private String origBusinessId;
	/**
	 * 业务类型
	 */
	private Integer businessType;
	/**
	 * 平台状态
	 */
	private Integer platStatus;
	/**
	 * 分公司
	 */
	private String companyName;

	public Date getConfirmTimeStart() {
		return confirmTimeStart;
	}

	public void setConfirmTimeStart(Date confirmTimeStart) {
		this.confirmTimeStart = confirmTimeStart;
	}

	public Date getConfirmTimeEnd() {
		return confirmTimeEnd;
	}

	public void setConfirmTimeEnd(Date confirmTimeEnd) {
		this.confirmTimeEnd = confirmTimeEnd;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public Integer getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Integer getPlatStatus() {
		return platStatus;
	}

	public void setPlatStatus(Integer platStatus) {
		this.platStatus = platStatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "ComplianceRepaymentVO [confirmTimeStart=" + confirmTimeStart + ", confirmTimeEnd=" + confirmTimeEnd
				+ ", processStatus=" + processStatus + ", repaySource=" + repaySource + ", settleType=" + settleType
				+ ", origBusinessId=" + origBusinessId + ", businessType=" + businessType + ", platStatus=" + platStatus
				+ ", companyName=" + companyName + "]";
	}

}
