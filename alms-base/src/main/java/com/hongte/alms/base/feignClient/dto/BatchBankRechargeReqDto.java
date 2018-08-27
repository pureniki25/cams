package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * 银行批量代扣dto
 * @author:陈泽圣
 * @date: 2018/5/24
 */
public class BatchBankRechargeReqDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
	 
	 private String rechargeUserId;//充值账户Id
	 private Double amount;//充值金额
	 private String channelType;//渠道类型:102：易宝 104：厦门银行云支付 105：通联 106：快钱
	 private String requestNo;//请求流水号 :GUID 字符串格式,如:" 6DC10AE5-55C5-43F3-95BC-CD677B0CD23F"

	public String getRechargeUserId() {
		return rechargeUserId;
	}
	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
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


	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	
   
}
