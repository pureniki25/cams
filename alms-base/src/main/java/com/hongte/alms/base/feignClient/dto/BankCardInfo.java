package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author:陈泽圣
 * 客户银行卡存管信息
 * @date: 2018/6/4
 */
public class BankCardInfo implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private String htUserID;//资产端( 鸿特)用户ID
     private String platformUserID;//资金端存管用户ID，当platformType为1或2时不能为空。若在团贷网注册存管，则返回团贷用户ID。若在你我金融注册存管，则返回你我金融用户ID。
     private String bankCardNumber;//【四要素】银行卡号码
     private String bankCardName;//【四要素】开户人真实姓名
     private String mobilePhone;//【四要素】必须为银行卡绑定手机号码，非联系电话。
     private String identityNo;//【四要素】此银行卡对应的开户人身份证号
     private Integer platformType;//资金端存管注册类型，0：第三方代扣银行卡，1：团贷网，2：你我金融
     private Integer withholdingType;//1：代扣主卡，2：代扣附属卡 代扣主卡表示自动代扣默认使用的卡
     private String bankName;//银行卡开户行名称
     private String bankSubName;//银行卡开户行支行
     private String bankCode;//见资产端银行编码对照表
     private String bankProvince;//银行卡开户行所在省
     private String bankCity;//银行卡开户行名称
     private List<ThirdPlatform> thirdPlatformList;//第三方代扣绑卡平台列表信息, 注1：只要列表内返回，则表示已绑定该平台,注2：存管注册银行卡也可以同时绑定其他第三方代扣平台

	public String getHtUserID() {
		return htUserID;
	}
	public void setHtUserID(String htUserID) {
		this.htUserID = htUserID;
	}
	public String getPlatformUserID() {
		return platformUserID;
	}
	public void setPlatformUserID(String platformUserID) {
		this.platformUserID = platformUserID;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getBankCardName() {
		return bankCardName;
	}
	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getIdentityNo() {
		return identityNo;
	}
	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}
	public Integer getPlatformType() {
		return platformType;
	}
	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
	public Integer getWithholdingType() {
		return withholdingType;
	}
	public void setWithholdingType(Integer withholdingType) {
		this.withholdingType = withholdingType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankSubName() {
		return bankSubName;
	}
	public void setBankSubName(String bankSubName) {
		this.bankSubName = bankSubName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public List<ThirdPlatform> getThirdPlatformList() {
		return thirdPlatformList;
	}
	public void setThirdPlatformList(List<ThirdPlatform> thirdPlatformList) {
		this.thirdPlatformList = thirdPlatformList;
	}


     

   
}
