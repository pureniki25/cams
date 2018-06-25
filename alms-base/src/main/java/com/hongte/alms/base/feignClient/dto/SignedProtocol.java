package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author:陈泽圣
 * 客户银行卡存管信息
 * @date: 2018/6/4
 */
public class SignedProtocol implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private Integer channelType;
     private String channelName;
     private Date signingDate;
	public Integer getChannelType() {
		return channelType;
	}
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Date getSigningDate() {
		return signingDate;
	}
	public void setSigningDate(Date signingDate) {
		this.signingDate = signingDate;
	}
	
   
}
