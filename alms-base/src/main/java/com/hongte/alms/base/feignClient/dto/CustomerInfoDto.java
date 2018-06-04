package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * 客户代扣相关信息
 * @date: 2018/5/25
 */
public class CustomerInfoDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private String bankBindCardNo;//银行代扣银行卡号
     private String platformBindCardNo;//绑定第三方平台银行卡号
     private String phone;//客户电话号码
     private String isDepository;//是否有注册存管账号
     private String platformId;//绑定平台ID
     private Integer isBindThirdParty;//是否绑定第三方平台 0:否 1：是
     private String validDate;//绑定第三方平台银行卡的有效期
     private String validNo;//绑定第三方平台银行卡的安全码	
     private String rechargeUserId;//团贷网用户ID
     
	public Integer getIsBindThirdParty() {
		return isBindThirdParty;
	}
	public void setIsBindThirdParty(Integer isBindThirdParty) {
		this.isBindThirdParty = isBindThirdParty;
	}
	public String getBankBindCardNo() {
		return bankBindCardNo;
	}
	public void setBankBindCardNo(String bankBindCardNo) {
		this.bankBindCardNo = bankBindCardNo;
	}
	public String getPlatformBindCardNo() {
		return platformBindCardNo;
	}
	public void setPlatformBindCardNo(String platformBindCardNo) {
		this.platformBindCardNo = platformBindCardNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIsDepository() {
		return isDepository;
	}
	public void setIsDepository(String isDepository) {
		this.isDepository = isDepository;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public String getValidNo() {
		return validNo;
	}
	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}
	public String getRechargeUserId() {
		return rechargeUserId;
	}
	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
   
}
