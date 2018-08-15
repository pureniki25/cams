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
	/**
	 * 标ID
	 */
	private String projectId ;
	/**
	 * 明细项名称,feeName无值时取此值
	 */
	private String planItemName ;
	/**
	 * 明细项类型
	 */
	private String planItemType ;
	/**
	 * 明细项费用id
	 */
	private String feeId ;
	/**
	 * 明细项费用名称,planItemName无值时取此值
	 */
	private String feeName ;
	/**
	 * 明细项金额
	 */
	private BigDecimal amount ;
	/**
	 * 核销顺序,不一定有值
	 */
	private Integer shareProfitIndex ;
	/**
	 * planList 不一定有值
	 */
	private String planListId ;
	
	/**
	 * planListDetailId 不一定有值
	 */
	private String planListDetailId ;
	
//	@Override
//	public boolean equals(Object o) {
//		if (!(o instanceof SettleFeesVO)) {
//			return false ;
//		}
//		SettleFeesVO oFeesVO = (SettleFeesVO) o ;
//		if (oFeesVO.getAmount().equals(this.getAmount())
//				&& oFeesVO.getFeeId().equals(this.getFeeId())
//				&& oFeesVO.getFeeName()) {
//			
//		}
//		return false;
//		
//	}
}
