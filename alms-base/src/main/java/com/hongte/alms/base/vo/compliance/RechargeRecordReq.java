package com.hongte.alms.base.vo.compliance;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.vo.PageRequest;

public class RechargeRecordReq extends PageRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 代充值账户
	 */
	private String rechargeAccountType;
	/**
	 * 代充值订单号
	 */
	private String cmOrderNo;
	/**
	 * 充值来源账户
	 */
	private String rechargeSourseAccount;
	/**
	 * 创建时间 开始
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTimeStart;
	/**
	 * 创建时间 结束
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTimeEnd;

	public String getRechargeAccountType() {
		return rechargeAccountType;
	}

	public void setRechargeAccountType(String rechargeAccountType) {
		this.rechargeAccountType = rechargeAccountType;
	}

	public String getCmOrderNo() {
		return cmOrderNo;
	}

	public void setCmOrderNo(String cmOrderNo) {
		this.cmOrderNo = cmOrderNo;
	}

	public String getRechargeSourseAccount() {
		return rechargeSourseAccount;
	}

	public void setRechargeSourseAccount(String rechargeSourseAccount) {
		this.rechargeSourseAccount = rechargeSourseAccount;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	@Override
	public String toString() {
		return "RechargeRecordReq [rechargeAccountType=" + rechargeAccountType + ", cmOrderNo=" + cmOrderNo
				+ ", rechargeSourseAccount=" + rechargeSourseAccount + ", createTimeStart=" + createTimeStart
				+ ", createTimeEnd=" + createTimeEnd + "]";
	}

}
