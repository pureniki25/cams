package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;


/**
 * 科目期初数据导入
 * @author 24043
 *
 */
@ExcelTarget("SubjectFirstExcel")
@Data
public class SubjectFirstExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "科目代码", orderNum = "1")
    private String subject;
    @Excel(name = "科目名称", orderNum = "2")
    private String subjectName;
    @Excel(name = "期初余额", orderNum = "3")
    private String firstAmount;
  

    
}
