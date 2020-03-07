/**
 * 
 */
package com.hongte.alms.base.collection.vo;

import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

/**
 * @author 王继光
 * 2018年12月3日 上午9:39:00
 */
@ExcelTarget("BusinessManagerVo")
@Data
public class BusinessManagerVo {

	@Excel(name = "业务编号", orderNum = "1",  isImportField = "true_st")
    private String businessId; //业务编号
	
	@Excel(name = "期数", orderNum = "2", isImportField = "true_st")
    private  String afterId;//总期数字段
	
	@Excel(name = "业务类型", orderNum = "3",   isImportField = "true_st")
    private String businessTypeName; //业务类型名称  对应 tb_basic_business_type 表中 业务类型名称
	
	@Excel(name = "分公司", orderNum = "4",   isImportField = "true_st")
    private String companyName;//分公司 对应 tb_basic_company中
	
	@Excel(name = "客户名称", orderNum = "5",   isImportField = "true_st")
    private String customerName; //用户姓名  对应 tb_basic_business 表中 客户名称
	
	@Excel(name = "业务获取", orderNum = "6",   isImportField = "true_st")
    private String operatorName; //业务获取 对应tb_basic_business表中业务主办人姓名
	
	@Excel(name = "应还金额", orderNum = "7",   isImportField = "true_st" , type = 10 )
    private double totalBorrowAmount;    //本期应还款金额  对应tb_repayment_biz_plan_list表中 total_borrow_amount 总计划应还金额字段
	
	@Excel(name = "逾期天数", orderNum = "8",   isImportField = "true_st", type = 10)
    private Integer delayDays; //逾期天数  对应 当前日期减去应还款日期
	
	@Excel(name = "应还日期", orderNum = "9", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
    private Date dueDate;   //应还日期  对应 tb_repayment_biz_plan_list 表中 应还日期
	
	@Excel(name = "还款状态", orderNum = "10",   isImportField = "true_st")
    private String statusName; //还款状态 名称
	
	@Excel(name = "业务状态", orderNum = "11",   isImportField = "true_st")
	private String afterColStatusName;//贷后状态 名称
}
