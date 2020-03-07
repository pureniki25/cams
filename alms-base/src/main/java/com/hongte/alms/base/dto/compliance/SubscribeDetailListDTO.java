package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 查询项目还款明细（实还）还款明细列表
 * @author huweiqian
 *
 */
@Data
public class SubscribeDetailListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 还款时间
	 */
	private String cycDate;
	/**
	 * 还款期数
	 */
	private int periods;
	/**
	 * 借款人偿还本金
	 */
	private BigDecimal amount;
	/**
	 * 借款人偿还利息
	 */
	private BigDecimal interestAmout;
	/**
	 * 借款人逾期滞纳金
	 */
	private BigDecimal borrowPenalty;
	/**
	 * 代偿人ID
	 */
	private String assureUserid;
	/**
	 * 代偿本息
	 */
	private BigDecimal assureAmount;
	/**
	 * 代偿逾期滞纳金
	 */
	private BigDecimal assurePenalty;
}
