package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceExel")
@Data
public class CamsCompanyExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "公司名称", orderNum = "1")
    private String companyName;
    @Excel(name = "纳税识别码", orderNum = "2")
    private String companyNo;

    @Excel(name = "纳税法人", orderNum = "3")
    private String companyOwner; 

    @Excel(name = "公司状态", orderNum = "4")
    private String companyStatus; 

    @Excel(name = "财务制度", orderNum = "5")
    private String companyRule; 
    
    @Excel(name = "公司类型", orderNum = "6")
    private String companyType; 
    


    




    
}
