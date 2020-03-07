package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("SubjectInitExcel")
@Data
public class SubjectInitExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "科目编码", orderNum = "1")
    private String subject;
    @Excel(name = "科目名称", orderNum = "2")
    private String subjectName;

    @Excel(name = "科目类别", orderNum = "3")
    private String subjectType; 
    @Excel(name = "余额方向", orderNum = "4") 
    private String direction; 
  


    
}
