package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;


/**
 * 领料单
 * @author 24043
 *
 */
@ExcelTarget("PickExcel")
@Data
public class PickExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "商品编码", orderNum = "1")
    private String productCode;
    @Excel(name = "产品名称", orderNum = "2")
    private String productName;
    @Excel(name = "规格", orderNum = "3")
    private String productType;
    @Excel(name = "单位", orderNum = "4")
    private String unit;
    @Excel(name = "数量", orderNum = "5")
    private String shuLiang;
    @Excel(name = "单价", orderNum = "6")
    private String danJia;
    @Excel(name = "金额", orderNum = "7")
    private String jine;
  

    
}
