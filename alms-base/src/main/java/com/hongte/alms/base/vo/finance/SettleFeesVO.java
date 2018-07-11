/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author 王继光
 * 2018年7月5日 下午9:44:06
 */
@Data
public class SettleFeesVO {
	private String planItemName ;
	private String planItemType ;
	private String feeId ;
	private BigDecimal amount ;
}
