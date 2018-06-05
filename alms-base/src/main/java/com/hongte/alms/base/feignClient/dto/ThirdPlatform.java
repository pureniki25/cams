package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author:陈泽圣
 * 客户银行卡存管信息
 * @date: 2018/6/4
 */
public class ThirdPlatform implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private Integer platformID;
     private String bindDate;
     private String bindUserName;
	public Integer getPlatformID() {
		return platformID;
	}
	public void setPlatformID(Integer platformID) {
		this.platformID = platformID;
	}
	public String getBindDate() {
		return bindDate;
	}
	public void setBindDate(String bindDate) {
		this.bindDate = bindDate;
	}
	public String getBindUserName() {
		return bindUserName;
	}
	public void setBindUserName(String bindUserName) {
		this.bindUserName = bindUserName;
	}
     
   
}
