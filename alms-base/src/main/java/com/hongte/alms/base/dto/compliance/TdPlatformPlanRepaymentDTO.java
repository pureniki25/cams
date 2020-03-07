package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 平台标的还款计划信息DTO
 * @author 胡伟骞
 *
 */
@Data
public class TdPlatformPlanRepaymentDTO implements Serializable {

	private static final long serialVersionUID = -7918825844438203785L;

	/**
	 * 期次
	 */
	private int period;

	/**
	 * 还款日期
	 */
	private String cycDate;

	/**
	 * 本金
	 */
	private BigDecimal amount;
	/**
	 * 利息
	 */
	private BigDecimal interestAmount;
	/**
	 * 保证金
	 */
	private BigDecimal depositAmount;
	/**
	 * 担保费
	 */
	private BigDecimal guaranteeAmount;
	/**
	 * 仲裁费
	 */
	private BigDecimal arbitrationAmount;
	/**
	 * 资产端服务费
	 */
	private BigDecimal orgAmount;
	/**
	 * 平台服务费
	 */
	private BigDecimal tuandaiAmount;
	/**
	 * 中介服务费
	 */
	private BigDecimal agencyAmount;
	/**
	 * 其他费用
	 */
	private BigDecimal otherAmount;
	/**
	 * 总金额
	 */
	private BigDecimal totalAmount;
	/**
	 * .net接口还款状态
	 */
	private String repaymentStatus;
}
