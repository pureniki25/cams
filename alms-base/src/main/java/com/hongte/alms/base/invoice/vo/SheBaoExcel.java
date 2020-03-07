package com.hongte.alms.base.invoice.vo;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

@ExcelTarget("SheBaoExcel")
@Data
public class SheBaoExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "身份证明号码", orderNum = "1")
    private String idCardNo;
    @Excel(name = "单位合计", orderNum = "2")
    private String danWeiHeJi;
    @Excel(name = "个人合计", orderNum = "3")
    private String geRenHeJi;
    @Excel(name = "应缴金额", orderNum = "4")
    private String yingJiaoJine;
  

    
}
