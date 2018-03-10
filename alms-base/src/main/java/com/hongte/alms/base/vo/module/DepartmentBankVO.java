package com.hongte.alms.base.vo.module;

import java.io.Serializable;

import com.hongte.alms.base.entity.DepartmentBank;

public class DepartmentBankVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2274331770167647125L;
	private String accountId;
	private Integer xdBankId;
	private String deptId;
	private String repaymentName;
	private String repaymentId;
	private String repaymentBank;
	private String repaymentSubBank;
	private String financeName;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getXdBankId() {
		return xdBankId;
	}
	public void setXdBankId(Integer xdBankId) {
		this.xdBankId = xdBankId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getRepaymentName() {
		return repaymentName;
	}
	public void setRepaymentName(String repaymentName) {
		this.repaymentName = repaymentName;
	}
	public String getRepaymentId() {
		return repaymentId;
	}
	public void setRepaymentId(String repaymentId) {
		this.repaymentId = repaymentId;
	}
	public String getRepaymentBank() {
		return repaymentBank;
	}
	public void setRepaymentBank(String repaymentBank) {
		this.repaymentBank = repaymentBank;
	}
	public String getRepaymentSubBank() {
		return repaymentSubBank;
	}
	public void setRepaymentSubBank(String repaymentSubBank) {
		this.repaymentSubBank = repaymentSubBank;
	}
	public String getFinanceName() {
		return financeName;
	}
	public void setFinanceName(String financeName) {
		this.financeName = financeName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DepartmentBankVO(DepartmentBank db) {
		super();
		if (db==null) {
			throw new RuntimeException("constructor fail");
		}
		setAccountId(db.getAccountId());
		setDeptId(db.getDeptId());
		setFinanceName(db.getFinanceName());
		setRepaymentBank(db.getRepaymentBank());
		setRepaymentId(db.getRepaymentId());
		setRepaymentName(db.getRepaymentName());
		setRepaymentSubBank(db.getRepaymentSubBank());
		setXdBankId(db.getXdBankId());
	}
	
	
}
