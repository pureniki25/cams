package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 款项池登记信息VO
 * 
 * @author 王继光
 *
 * @since 2018年3月1日 下午4:49:42
 */
public class MoneyPoolVO {
	/**
	 * 流水号
	 */
	private String id ;
	/**
	 * 还款日期
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date repaymentDate ;
	/**
	 * 还款金额
	 */
	private String repaymentMoney ;
	/**
	 * 实际还款人
	 */
	private String realRepaymentUser ;
	/**
	 * 转入账号
	 */
	private String acceptBank ;
	/**
	 * 交易类型
	 */
	private String tradeType ;
	/**
	 * 交易场所
	 */
	private String tradePlace ;
	/**
	 * 状态
	 */
	private String status ;
	/**
	 * 更新用户
	 */
	private String updateUser ;
	/**
	 * 更新时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date updateTime ;
	
	/**
	 * 凭证图片URL
	 */
	private String certUrl ;
	
	private String remark ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public String getRepaymentMoney() {
		return repaymentMoney;
	}
	public void setRepaymentMoney(String repaymentMoney) {
		this.repaymentMoney = repaymentMoney;
	}
	public String getRealRepaymentUser() {
		return realRepaymentUser;
	}
	public void setRealRepaymentUser(String realRepaymentUser) {
		this.realRepaymentUser = realRepaymentUser;
	}
	public String getAcceptBank() {
		return acceptBank;
	}
	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCertUrl() {
		return certUrl;
	}
	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
