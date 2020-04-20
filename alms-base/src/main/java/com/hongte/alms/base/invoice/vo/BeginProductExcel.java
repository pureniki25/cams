package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("BeginProductExcel")
@Data
public class BeginProductExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "商品编码", orderNum = "1")
    private String productCode;
    @Excel(name = "商品名称", orderNum = "2")
    private String productName;
    @Excel(name = "商品规格", orderNum = "3")
    private String productType;
    @Excel(name = "期初数量", orderNum = "4")
    private String kuCunLiang;
    @Excel(name = "期初单价", orderNum = "5")
    private String qiChuDanJia;
    @Excel(name = "期初金额", orderNum = "6")
    private String qiChuJine;
    @Excel(name = "单位", orderNum = "7")
    private String danWei;


    
}
