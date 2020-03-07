package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 还垫付明细（适用于6.28之上标数据，还垫付分润传参）
 * @author huweiqian
 *
 */
@Data
public class ReturnAdvanceListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询返回的AdvanceID
	 */
	private String advanceID;
	/**
	 * 1、本息、2、分润、3、逾期费用
	 */
	private Integer type;
	/**
	 * 还本息时为垫付人ID，还分润时为分润用户ID
	 */
	private String userID;
	/**
	 * 还本息时为垫付金额+滞纳金，还分润时为分润金额
	 */
	private BigDecimal amount;
}
