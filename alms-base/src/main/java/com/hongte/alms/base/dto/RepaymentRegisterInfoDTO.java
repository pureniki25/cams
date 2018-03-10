package com.hongte.alms.base.dto;

import java.io.Serializable;

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
	 * 款项池id/还款记录id/whatever
	 */
	private String moneyPoolId ;
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
}
