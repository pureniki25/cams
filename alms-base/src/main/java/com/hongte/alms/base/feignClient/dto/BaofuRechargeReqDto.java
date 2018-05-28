package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * 宝付代扣dto
 * @author:陈泽圣
 * @date: 2018/5/24
 */
public class BaofuRechargeReqDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
	 private String bizType        ;//其他：不填写和默认0000,表示为储蓄卡支付。
	 private String payCode        ;//具体参数见:银行编码
	 private String payCm          ;
	 private String accNo          ;
	 private String idCardType     ;
	 private String idCard         ;
	 private String idHolder       ;
	 private String mobile         ;
	 private String validDate      ;
	 private String validNo        ;
	 private String transId        ;
	 private String txnAmt         ;
	 private String tradeDate      ;
	 private String additionalInfo ;
	 private String reqReserved    ;
	 private String transSerialNo  ;
	 
	 public String getBizType() {
			return bizType;
		}
		public void setBizType(String bizType) {
			this.bizType = bizType;
		}
		public String getPayCode() {
			return payCode;
		}
		public void setPayCode(String payCode) {
			this.payCode = payCode;
		}
		public String getPayCm() {
			return payCm;
		}
		public void setPayCm(String payCm) {
			this.payCm = payCm;
		}
		public String getAccNo() {
			return accNo;
		}
		public void setAccNo(String accNo) {
			this.accNo = accNo;
		}
		public String getIdCardType() {
			return idCardType;
		}
		public void setIdCardType(String idCardType) {
			this.idCardType = idCardType;
		}
		public String getIdCard() {
			return idCard;
		}
		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}
		public String getIdHolder() {
			return idHolder;
		}
		public void setIdHolder(String idHolder) {
			this.idHolder = idHolder;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
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
		public String getTransId() {
			return transId;
		}
		public void setTransId(String transId) {
			this.transId = transId;
		}
		public String getTxnAmt() {
			return txnAmt;
		}
		public void setTxnAmt(String txnAmt) {
			this.txnAmt = txnAmt;
		}
		public String getTradeDate() {
			return tradeDate;
		}
		public void setTradeDate(String tradeDate) {
			this.tradeDate = tradeDate;
		}
		public String getAdditionalInfo() {
			return additionalInfo;
		}
		public void setAdditionalInfo(String additionalInfo) {
			this.additionalInfo = additionalInfo;
		}
		public String getReqReserved() {
			return reqReserved;
		}
		public void setReqReserved(String reqReserved) {
			this.reqReserved = reqReserved;
		}
		public String getTransSerialNo() {
			return transSerialNo;
		}
		public void setTransSerialNo(String transSerialNo) {
			this.transSerialNo = transSerialNo;
		}
}
