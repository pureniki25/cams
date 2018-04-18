package com.hongte.alms.base.vo.module.api;

import java.math.BigDecimal;

public class AddFee {
   private String feeId;//费用id
   private String feeName;//string	费用名称
   private BigDecimal amount;//减免后的金额
   
   
   
public String getFeeName() {
	return feeName;
}
public void setFeeName(String feeName) {
	this.feeName = feeName;
}
public String getFeeId() {
	return feeId;
}
public void setFeeId(String feeId) {
	this.feeId = feeId;
}
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}
   
   
}
