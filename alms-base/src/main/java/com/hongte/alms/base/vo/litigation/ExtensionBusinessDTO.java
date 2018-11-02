package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

/**
 * 展期信息
 * @author huweiqian
 *
 */
@Data
@ToString
public class ExtensionBusinessDTO implements Serializable {

	private static final long serialVersionUID = 8507563552140920150L;

	/**
	 * 展期业务编号
	 */
	private String businessId;
	
	/**
	 * 展期利率
	 */
	private BigDecimal borrowRate;
	
	/**
	 * 展期期限
	 */
	private int borrowLimit;
}
