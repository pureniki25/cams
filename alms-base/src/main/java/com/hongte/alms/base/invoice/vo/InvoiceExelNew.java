package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceExelNew")
@Data
public class InvoiceExelNew implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "发票代码", orderNum = "2")
    private String faPiaoDaiMa;
    @Excel(name = "发票号码", orderNum = "3")
    private String faPiaoHaoMa;

    @Excel(name = "付款方名称", orderNum = "4")
    private String gouFangQiYeMingChen; 
    @Excel(name = "开票日期", orderNum = "1")
    private String kaiPiaoRiQi; 
    @Excel(name = "开票项目说明", orderNum = "5")
    private String shangPinMingChen; //'业务编号'


    @Excel(name = "开票项目金额", orderNum = "6")
    private String jine;//'本期应还金额'
   

    
}
