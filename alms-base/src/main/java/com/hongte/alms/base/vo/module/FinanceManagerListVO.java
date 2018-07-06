/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author 王继光 2018年5月3日 下午3:10:11
 */
@Data
public class FinanceManagerListVO {

	// 业务编号 期数 部门 主办 客户姓名 业务类型 还款日期 还款登记日期 还款金额 会计确认状态 状态 是否支持代扣
	private String planId ;
	/**
	 * 还款计划id
	 */
	private String planListId;
	/**
	 * 业务编号
	 */
	private String businessId;
	private String orgBusinessId;
	/**
	 * 期数
	 */
	private String afterId;
	private Integer period ;
	/**
	 * 部门
	 */
	private String dept;
	/**
	 * 主办
	 */
	private String operator;
	/**
	 * 客户姓名
	 */
	private String customer;
	
	/**
	 * 是否主借款人
	 */
	private Boolean ismainCustomer ; 
	/**
	 * 业务类型
	 */
	private String businessType;
	/**
	 * 借款金额
	 */
	private String borrowMoney;
	/**
	 * 还款方式
	 */
	private String repaymentType;
	/**
	 * 借款期限
	 */
	private String borrowLimit;
	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 还款日期
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date planRepayDate;
	/**
	 * 还款登记日期
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date factRepayDate;

	/**
	 * 还款金额
	 */
	private BigDecimal planRepayAmount;
	
	/**
	 * 已还金额
	 */
	private BigDecimal repaidAmount;
	/**
	 * 会计确认状态
	 */
	private String financeConfirmStatus;
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
	 */
	private String repayStatus ;
	/**
	 * 是否支持代扣
	 */
	private boolean canWithhold;

	/**
	 * 是否支持代扣描述
	 */
	private String canWithholdDesc;
	/**
	 * 代扣确认状态
	 */
	private Integer confirmFlag ;
	
	/**
	 * 是否线上代扣
	 */
	private boolean bankRepay; 
	/**
	 * 来源类型：1.信贷生成，2.贷后管理生成
	 */
	
	private Integer srcType;
	

}
