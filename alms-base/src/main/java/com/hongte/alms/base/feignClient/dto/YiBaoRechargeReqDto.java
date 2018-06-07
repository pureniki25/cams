package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * 易宝代扣dto
 * @author:陈泽圣
 * @date: 2018/5/24
 */
public class YiBaoRechargeReqDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
	 private String merchantaccount;
	 private String orderid;
	 private Long transtime;
	 private String currency;
	 private Double amount;
	 private String productname;
	 public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	private String productdesc;
	 private String identityid;
	 private String identitytype;
	 private String card_top;
	 private String card_last;
	 private String orderexpdate;
	 private String callbackurl;
	 private String imei;
	 private String userip;
	 private String ua;
	public String getMerchantaccount() {
		return merchantaccount;
	}
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Long getTranstime() {
		return transtime;
	}
	public void setTranstime(Long transtime) {
		this.transtime = transtime;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProductdesc() {
		return productdesc;
	}
	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}
	public String getIdentityid() {
		return identityid;
	}
	public void setIdentityid(String identityid) {
		this.identityid = identityid;
	}
	public String getIdentitytype() {
		return identitytype;
	}
	public void setIdentitytype(String identitytype) {
		this.identitytype = identitytype;
	}
	public String getCard_top() {
		return card_top;
	}
	public void setCard_top(String card_top) {
		this.card_top = card_top;
	}
	public String getCard_last() {
		return card_last;
	}
	public void setCard_last(String card_last) {
		this.card_last = card_last;
	}
	public String getOrderexpdate() {
		return orderexpdate;
	}
	public void setOrderexpdate(String orderexpdate) {
		this.orderexpdate = orderexpdate;
	}
	public String getCallbackurl() {
		return callbackurl;
	}
	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getUa() {
		return ua;
	}
	public void setUa(String ua) {
		this.ua = ua;
	}
	 
	 
	 
}
