package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceCashExcel")
@Data
public class InvoiceCashExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "科目编码", orderNum = "1")
    private String keMuBianMa;
    @Excel(name = "科目名称", orderNum = "2")
    private String keMuMingChen;
    @Excel(name = "金额", orderNum = "3")
    private String jine; 
  
    
}
 