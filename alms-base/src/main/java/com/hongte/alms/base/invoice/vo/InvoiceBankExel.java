package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceBankExel")
@Data
public class InvoiceBankExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "日期", orderNum = "1")
    private String riQi;
    @Excel(name = "编码", orderNum = "1")
    private String bianMa;
    @Excel(name = "对方户名", orderNum = "2")
    private String duiFangHuMing;
    @Excel(name = "摘要", orderNum = "3")
    private String zhaiYao; 
    @Excel(name = "支出", orderNum = "4")
    private String zhiChu; 
    @Excel(name = "收入", orderNum = "5")
    private String shouRu; 
    @Excel(name = "余额", orderNum = "6")
    private String ye;

  
    
}
