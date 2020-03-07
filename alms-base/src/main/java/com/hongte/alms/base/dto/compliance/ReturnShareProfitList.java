package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 查询项目正常还款明细和提前结清待还本息接口返回每期应还分润列表
 * @author huweiqian
 *
 */

@Data
public class ReturnShareProfitList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 还款日期
	 */
	private String cycDate;
	
	/**
	 * 还款期数
	 */
	private int period;
	
	/**
	 * 分润用户ID
	 */
	private String profitUserID;
	
	/**
	 * 应还分润金额(当前期该分润用户应还分润金额)
	 */
	private BigDecimal amount;

	/**
	 * 实际还分润金额
	 */
	private BigDecimal returnAmount;
	/**
	 * 分润逾期金额
	 */
	private BigDecimal overAmount;
	/**
	 * 还垫付分润的金额
	 */
	private BigDecimal returnAdvanceShareAmount;
}
