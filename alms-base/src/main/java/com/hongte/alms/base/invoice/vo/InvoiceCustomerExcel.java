package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;

@ExcelTarget("InvoiceCustomerExcel")
@Data
public class InvoiceCustomerExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "单位编码", orderNum = "1")
    private String customerCode;
    @Excel(name = "单位名称", orderNum = "2")
    private String customerName;



    
}
