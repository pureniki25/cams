package com.hongte.alms.base.excel;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@ExcelTarget("customerExel")
public class CustomerExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;

 
    @Excel(name = "客户/供应商名称", orderNum = "1", isImportField = "true_st")
    private String customerName;//'客户/供应商名称'



}
