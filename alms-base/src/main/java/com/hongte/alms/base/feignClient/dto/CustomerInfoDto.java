package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author:陈泽圣
 * 客户信息
 * @date: 2018/6/4
 */
public class CustomerInfoDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private List <BankCardInfo> list;//银行卡相关信息
	public List<BankCardInfo> getList() {
		return list;
	}
	public void setList(List<BankCardInfo> list) {
		this.list = list;
	}
    
}
