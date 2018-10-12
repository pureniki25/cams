/**
 * 
 */
package com.hongte.alms.base.dto;

import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author 王继光
 * 2018年10月8日 上午10:24:28
 */
@Data
@ExcelTarget("FactRepayDto")
public class FactRepayDto {
	@Excel(name="业务编号_businessId")
	private String businessId ;
	
	@Excel(name="期数_period")
	private String period ;
	
	@Excel(name="业务类型_businessType")
	private String businessType ;
	
	@Excel(name="分公司_companyId")
	private String companyId ;
	
	@Excel(name="客户名称_customerName")
	private String customerName ;
	
	@Excel(name="所属投资端_srcType")
	private String srcType ;
	
	@Excel(name="还款方式_repayType")
	private String repayType ;
	
	@Excel(name="实还日期_factRepayDate")
	private Date factRepayDate ;
	
	@Excel(name="实还金额_factRepayAmount")
	private BigDecimal factRepayAmount ;
	
	@Excel(name="本金_item10")
	private BigDecimal item10 ;
	
	@Excel(name="利息_item20")
	private BigDecimal item20 ;
	
	@Excel(name="分公司服务费_item30")
	private BigDecimal item30 ;
	
	@Excel(name="平台服务费_item50")
	private BigDecimal item50 ;
	
	@Excel(name="线上滞纳金_item60online")
	private BigDecimal item60online ;
	
	@Excel(name="线下滞纳金_item60offline")
	private BigDecimal item60offline ;
	
    @Excel(name="提前结清本金违约金_penaltyPrincipal")
	private BigDecimal penaltyPrincipal ;
	
	@Excel(name="提前结清分公司服务费违约金_penaltySubCompany")
	private BigDecimal penaltySubCompany ;
	
	@Excel(name="提前结清平台违约金_penaltyPlatform")
	private BigDecimal penaltyPlatform ;
	
	@Excel(name="其他费用_otherFee")
	private BigDecimal otherFee ;
	
	@Excel(name="备注_remark")
	private String remark ;
	
	@Excel(name="操作人员_operator")
	private String operator ;
}
