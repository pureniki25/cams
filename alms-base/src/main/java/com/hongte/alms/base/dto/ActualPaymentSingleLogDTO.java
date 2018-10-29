package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ActualPaymentSingleLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private String afterId;

	/**
	 * 交易金额
	 */
	private double currentAmount;

	/**
	 * 收支类型
	 */
	private String incomeType;

	/**
	 * 交易时间
	 */
	private Date tradeTime;

	/**
	 * 交易方式
	 */
	private String tradeType;

	/**
	 * 账户名称
	 */
	private String accountName;
	
	/**
	 * 平台期数
	 */
	private Integer period; 
}
