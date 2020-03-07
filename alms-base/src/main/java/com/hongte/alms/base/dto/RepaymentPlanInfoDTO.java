package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RepaymentPlanInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 还款计划列表ID
	 */
	private String planListId;
	/**
	 * 类型: 计划还款、实际还款、差额
	 */
	private String repayment;
	/**
	 * 期数：还款计划的期数
	 */
	private String afterId;
	/**
	 * 平台期次
	 */
	private String period;
	/**
	 * 还款日期：应还日期/实际还款日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date repaymentDate;
	/**
	 * 本金：本期应还本金
	 */
	private double principal;
	/**
	 * 利息：本期应还利息
	 */
	private double accrual;
	/**
	 * 月收分公司服务费：本期月收分公司服务费
	 */
	private double serviceCharge;
	/**
	 * 月收平台费：本期月收平台费
	 */
	private double platformCharge;
	/**
	 * 其他费用：本期应收其他费用，该费用在应还时应为0，结清时还才有值
	 */
	private double otherFee;
	/**
	 * 小计：本金+利息+月收分公司服务费+月收平台费
	 */
	private double subtotal;
	/**
	 * 逾期天数
	 */
	private double overdueDays;
	/**
	 * 线上滞纳金：应还线上滞纳金 逾期天数×线上滞纳金费率（系统配置）
	 */
	private double onlineLateFee;
	/**
	 * 线下滞纳金 ：应还线下滞纳金 逾期天数× 线下滞纳金费率
	 */
	private double offlineLateFee;
	/**
	 * 线上减免金额
	 */
	private double onlineDerateAmount;
	/**
	 * 线下减免金额
	 */
	private double offlineDerateAmount;
	/**
	 * 结余：应还为0，实际实收结余费用项
	 */
	private double surplus;
	/**
	 * 还款合计（含滞纳金）：小计+线上滞纳金+线下滞纳金+结余
	 */
	private double total;
	/**
	 * 总计
	 */
	private double amount;
	/**
	 * 财务确认状态
	 */
	private int confirmFlag;
	/**
	 * 财务确认状态
	 */
	private String confirmFlagStr;
	/**
	 * 是否原业务 1、是；2、否
	 */
	private int isOrig;
	/**
	 * 减免金额
	 */
	private double derateMoney;
}
