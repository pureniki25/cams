package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 匹配的款项池银行流水VO
 * 
 * @author 王继光
 *
 * @since 2018年3月1日 下午4:52:45
 */
public class MatchedMoneyPoolVO {

	/**
	 * 流水号
	 */
	private String moneyPoolId ;
	/**
	 * 款项码
	 */
	private String repaymentCode ;
	/**
	 * 转入账户
	 */
	private String bankAccount ;
	/**
	 * 转入时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date tradeDate ;
	/**
	 * 交易类型
	 */
	private String tradeType ;
	/**
	 * 交易场所
	 */
	private String tradePlace ;
	/**
	 * 记账金额
	 */
	private BigDecimal accountMoney ;
	/**
	 * 交易备注
	 */
	private String remark ;
	/**
	 * 摘要
	 */
	private String summary ;
	/**
	 * 状态
	 */
	private String status ;
	public String getRepaymentCode() {
		return repaymentCode;
	}
	public void setRepaymentCode(String repaymentCode) {
		this.repaymentCode = repaymentCode;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradePlace() {
		return tradePlace;
	}
	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the moneyPoolId
	 */
	public String getMoneyPoolId() {
		return moneyPoolId;
	}
	/**
	 * @param moneyPoolId the moneyPoolId to set
	 */
	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}
	/**
	 * @return the accountMoney
	 */
	public BigDecimal getAccountMoney() {
		return accountMoney;
	}
	/**
	 * @param accountMoney the accountMoney to set
	 */
	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}
	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
}
