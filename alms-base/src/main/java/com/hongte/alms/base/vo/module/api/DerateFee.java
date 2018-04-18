package com.hongte.alms.base.vo.module.api;

import java.math.BigDecimal;

public class DerateFee {
   private String feeId;//费用id
   private BigDecimal amount;//减免后的金额
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
