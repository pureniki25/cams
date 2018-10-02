/**
 * 
 */
package com.hongte.alms.tool.importOffLineBusiness;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author 王继光
 * 2018年9月11日 下午2:15:45
 */
@Data
@ExcelTarget(value = "BaseBusinessEntity")
public class BaseBusinessEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1790768261278826949L;
	@Excel(name = "businessId")
	String businessId ;
	@Excel(name = "inputTime",importFormat="yyyy/M/d")
	Date inputTime ;
	@Excel(name = "businessType")
	Integer businessType ;
	@Excel(name = "customerName")
	String customerName ;
	@Excel(name = "repaymentTypeId")
	String repaymentTypeId ;
	@Excel(name = "borrowMoney")
	BigDecimal borrowMoney ; 
	@Excel(name = "borrowLimit")
	Integer borrowLimit ;
	@Excel(name = "borrowRate")
	String borrowRate ;
	@Excel(name = "borrowRateUnit")
	Integer borrowRateUnit ;
	@Excel(name = "companyName")
	String companyName ;

}
