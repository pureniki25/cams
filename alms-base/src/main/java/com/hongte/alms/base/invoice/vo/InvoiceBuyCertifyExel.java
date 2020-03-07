package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceBuyCertifyExel")
@Data
public class InvoiceBuyCertifyExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "税率", orderNum = "1")
    private String taxRate;
    @Excel(name = "发票号码", orderNum = "2")
    private String faPiaoHaoMa;
    @Excel(name = "是否可抵扣", orderNum = "3")
    private String shiFouKeDiKou;

    @Excel(name = "是否已认证", orderNum = "4")
    private String shiFouYiRenZheng;
    
    @Excel(name = "税额", orderNum = "5")
    private String shuie;



 
    
}
