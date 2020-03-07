package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceProductExcel")
@Data
public class InvoiceProductExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "产品编码", orderNum = "1")
    private String productCode;
    @Excel(name = "商品名称", orderNum = "2")
    private String productName;
    @Excel(name = "规格", orderNum = "3")
    private String productType;
    @Excel(name = "单位", orderNum = "4")
    private String unit;
    @Excel(name = "数量", orderNum = "5")
    private String kuCunLiang;

    
}
