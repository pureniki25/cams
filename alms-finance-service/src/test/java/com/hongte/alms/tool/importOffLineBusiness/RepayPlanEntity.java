/**
 * 
 */
package com.hongte.alms;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author 王继光
 * 2018年9月11日 下午3:47:08
 */
@Data
@ExcelTarget("RepayPlanEntity")
public class RepayPlanEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -1169778219083455572L;
	
	@Excel(name = "businessId")
	String businessId ;
	@Excel(name = "borrowMoney")
	BigDecimal borrowMoney ;
	@Excel(name = "borrowRate")
	String borrowRate ;
	@Excel(name = "borrowRateUnit")
	Integer borrowRateUnit ;
	@Excel(name = "borrowLimit")
	Integer borrowLimit ;
	@Excel(name = "borrowLimitUnit")
	Integer borrowLimitUnit ;
	@Excel(name = "planStatus")
	Integer planStatus ;
	
}
