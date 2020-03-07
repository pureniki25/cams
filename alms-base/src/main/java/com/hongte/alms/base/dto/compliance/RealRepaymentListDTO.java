package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 查询项目还款明细（实还）实际还款列表说明
 * @author huweiqian
 *
 */
@Data
public class RealRepaymentListDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 还款时间
	 */
	private String cycDate;
	/**
	 * 还款期数
	 */
	private int period;
	/**
	 * 借款人实还本息
	 */
	private BigDecimal factAmout;
	/**
	 * 借款人偿还垫付总本息
	 */
	private BigDecimal returnAdvanceAmount;
	/**
	 * 借款人偿还垫付总逾期费用
	 */
	private BigDecimal overdueAmount;

}
