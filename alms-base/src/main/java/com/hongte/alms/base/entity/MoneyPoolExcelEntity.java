/**
 * 
 */
package com.hongte.alms.base.entity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * @author 王继光
 * 2018年6月9日 下午3:57:06
 */
@ExcelTarget("moneyPoolExcel")
public class MoneyPoolExcelEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1334491522287867479L;

	@Excel(name="接收银行账号",orderNum="1")
	private String acceptBank ;
	@Excel(name="款项编码",orderNum="2")
	private String payCode ;
	@Excel(name="交易日期",orderNum="3")
	private String tradeDate ;
	@Excel(name="交易时间",orderNum="4")
	private String tradeTime ;
	@Excel(name="支付类型",orderNum="5")
	private String tradeType ;
	@Excel(name="摘要",orderNum="6")
	private String summary ;
	@Excel(name="交易场所",orderNum="7")
	private String tradePlace ;
	@Excel(name="记账金额",orderNum="8")
	private String accountMoney ;
	@Excel(name="类型",orderNum="9")
	private String incomeType ;
	@Excel(name="交易备注",orderNum="10")
	private String remark ;
	public String getAcceptBank() {
		return acceptBank;
	}
	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTradePlace() {
		return tradePlace;
	}
	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}
	public String getAccountMoney() {
		return accountMoney;
	}
	public void setAccountMoney(String accountMoney) {
		this.accountMoney = accountMoney;
	}
	public String getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
