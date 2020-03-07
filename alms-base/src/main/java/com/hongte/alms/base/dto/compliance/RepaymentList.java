package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 查询项目正常还款明细和提前结清待还本息接口返回每期还款列表
 * @author huweiqian
 *
 */
@Data
public class RepaymentList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 还款日期
	 */
	private String cycDate;
	/**
	 * 还款期数
	 */
	private int periods;
	/**
	 * 还款本金
	 */
	private BigDecimal amount;
	/**
	 * 还款利息
	 */
	private BigDecimal interestAmout;
	/**
	 * 还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)
	 */
	private int repaymentStatus;
}
