package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户还款登记DTO
 * @author 王继光
 *
 * @since 2018年3月2日 下午2:49:59
 */
public class RepaymentRegisterInfoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953213548854600589L;
	public RepaymentRegisterInfoDTO() {
		super();
	}
	/**
	 * mprid
	 */
	private String mprid ;
	/**
	 * businessId
	 */
	private String businessId ;
	/**
	 * 期数
	 */
	private String afterId ;
	/**
	 * 还款日期
	 */
	private String repaymentDate ;
	
	
	private BigDecimal beforeOverAmount;//确认还款之前的逾期金额
	
	private BigDecimal beforeOverdueDays;//确认还款之前的逾期天数
	
	private BigDecimal afterOverdueDays;//确认还款之后的逾期天数
	
	
	private BigDecimal afterOverAmount;//确认还款之后的逾期金额
	
	/**
	 * 还款金额
	 */
	private String repaymentMoney ;
	/**
	 * 实际还款人
	 */
	private String factRepaymentUser ;
	/**
	 * 交易类型
	 */
	private String tradeType ;
	/**
	 * 交易场所
	 */
	private String tradePlace ;
	/**
	 * 转入账号/收款人
	 */
	private String acceptBank ;
	/**
	 * 凭证,图片url
	 */
	private String cert ;
	/**
	 * 备注
	 */
	private String remark ;
	/**
	 * 操作人id
	 */
	private String userId ;
	/**
	 * moneypool id / moneypool repayment id/ whatever
	 */
	
	
	private String moneyPoolId ;
	public BigDecimal getAfterOverAmount() {
		return afterOverAmount;
	}
	public void setAfterOverAmount(BigDecimal afterOverAmount) {
		this.afterOverAmount = afterOverAmount;
	}
	public BigDecimal getBeforeOverAmount() {
		return beforeOverAmount;
	}
	public void setBeforeOverAmount(BigDecimal beforeOverAmount) {
		this.beforeOverAmount = beforeOverAmount;
	}
	public BigDecimal getBeforeOverdueDays() {
		return beforeOverdueDays;
	}
	public void setBeforeOverdueDays(BigDecimal beforeOverdueDays) {
		this.beforeOverdueDays = beforeOverdueDays;
	}
	public BigDecimal getAfterOverdueDays() {
		return afterOverdueDays;
	}
	public void setAfterOverdueDays(BigDecimal afterOverdueDays) {
		this.afterOverdueDays = afterOverdueDays;
	}
	public String getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public String getRepaymentMoney() {
		return repaymentMoney;
	}
	public void setRepaymentMoney(String repaymentMoney) {
		this.repaymentMoney = repaymentMoney;
	}
	public String getFactRepaymentUser() {
		return factRepaymentUser;
	}
	public void setFactRepaymentUser(String factRepaymentUser) {
		this.factRepaymentUser = factRepaymentUser;
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
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMoneyPoolId() {
		return moneyPoolId;
	}
	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}
	public String getAcceptBank() {
		return acceptBank;
	}
	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}
	/**
	 * @return the mprid
	 */
	public String getMprid() {
		return mprid;
	}
	/**
	 * @param mprid the mprid to set
	 */
	public void setMprid(String mprid) {
		this.mprid = mprid;
	}
}
