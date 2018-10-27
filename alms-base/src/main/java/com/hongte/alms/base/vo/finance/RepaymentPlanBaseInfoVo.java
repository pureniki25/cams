package com.hongte.alms.base.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
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

}
