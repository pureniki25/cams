package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;


/**
 * 单位期初数据导入
 * @author 24043
 *
 */
@ExcelTarget("CustomerFirstExcel")
@Data
public class CustomerFirstExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "单位编码", orderNum = "1")
    private String customerCode;
    @Excel(name = "单位名称", orderNum = "2")
    private String customerName;
    @Excel(name = "期初余额本币金额", orderNum = "3")
    private String firstAmount;
  

    
}
