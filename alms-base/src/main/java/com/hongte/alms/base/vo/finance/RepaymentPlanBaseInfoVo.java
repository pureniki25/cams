package com.hongte.alms.base.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class RepaymentPlanBaseInfoVo implements Serializable {
	
	private static final long serialVersionUID = -975981272387174699L;
	
	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 客户姓名
	 */
	private String customerName;
	/**
	 * 电话号码
	 */
	private String phoneNumber;
	/**
	 * 还款方式
	 */
	private String repaymentTypeName;
	/**
	 * 借款金额
	 */
	private BigDecimal borrowMoney;
	/**
	 * 借款期限
	 */
	private Integer borrowLimit;
	/**
	 * 上标平台
	 */
	private Integer plateType;
	
	/**
	 * 存管账户余额
	 */
	private BigDecimal accountBalance;

	/**
	 * 今天结清应缴纳费用
	 */
	private BigDecimal settleTotalAmount;
	
	/**
	 * 本金
	 */
	private BigDecimal principal;
	
	/**
	 * 利息
	 */
	private BigDecimal interest;
	
	/**
	 * 平台费
	 */
	private BigDecimal platformAmount;
	
	/**
	 * 分公司服务费
	 */
	private BigDecimal orgAmount;
	
	/**
	 * 本金违约金
	 */
	private BigDecimal liquidatedDamage;
	
	/**
	 * 来源类型：1.信贷生成，2.贷后管理生成
	 */
	private Integer srcType;
}
