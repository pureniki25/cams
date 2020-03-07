package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceExel")
@Data
public class InvoiceExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "发票代码", orderNum = "1")
    private String faPiaoDaiMa;
    @Excel(name = "发票号码", orderNum = "2")
    private String faPiaoHaoMa;

    @Excel(name = "购方企业名称", orderNum = "3")
    private String gouFangQiYeMingChen; 
    @Excel(name = "开票日期", orderNum = "4") 
    private String kaiPiaoRiQi; 
    @Excel(name = "商品名称", orderNum = "5")
    private String shangPinMingChen; //'业务编号'
    @Excel(name = "规格", orderNum = "6")
    private String guiGe;//'客户名称'

    @Excel(name = "单位", orderNum = "7")
    private String danWei;//'期数'
    @Excel(name = "数量", orderNum = "8")
    private String shuLiang;//'所属分公司'
    @Excel(name = "单价", orderNum = "9")
    private String danJia;//'转账金额'

    @Excel(name = "金额", orderNum = "10")
    private String jine;//'本期应还金额'
    @Excel(name = "税率", orderNum = "11")
    private String shuiLv;//'实际转账人'
    @Excel(name = "税额", orderNum = "12")
    private String shuie;//'转账日期'
    @Excel(name = "发票状态", orderNum = "13")
    private String faPiaoZhuangTai;//'发票状态'


    
}
