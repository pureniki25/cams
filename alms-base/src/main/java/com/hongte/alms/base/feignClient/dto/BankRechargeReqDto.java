package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * 银行代扣dto
 * @author:陈泽圣
 * @date: 2018/5/24
 */
public class BankRechargeReqDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
	 
	 private String oidPartner;//资产端账户唯一编号,团贷分配，商户唯一号(测试，生产不一样)
	 private String rechargeUserId;//充值账户Id
	 private String orgUserName;//机构账户(团贷登录用户名)
	 private Double amount;//充值金额
	 private String channelType;//渠道类型:102：易宝 104：厦门银行云支付 105：通联 106：快钱
	 private String cmOrderNo;//机构订单编号
	 private String userIP;
	public String getOidPartner() {
		return oidPartner;
	}
	public void setOidPartner(String oidPartner) {
		this.oidPartner = oidPartner;
	}
	public String getRechargeUserId() {
		return rechargeUserId;
	}
	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}
	public String getOrgUserName() {
		return orgUserName;
	}
	public void setOrgUserName(String orgUserName) {
		this.orgUserName = orgUserName;
	}

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getCmOrderNo() {
		return cmOrderNo;
	}
	public void setCmOrderNo(String cmOrderNo) {
		this.cmOrderNo = cmOrderNo;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
   
}
