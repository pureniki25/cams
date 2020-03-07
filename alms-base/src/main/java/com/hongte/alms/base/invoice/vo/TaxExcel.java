package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("TaxExcel")
@Data
public class TaxExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "纳税人", orderNum = "1")
    private String companyName;
    
   
    
    @Excel(name = "国税编码", orderNum = "3")
    private String guoShuiBianMa; 
    
    @Excel(name = "应交增值税", orderNum = "10")
    private String zengZhiShui; 
    
    @Excel(name = "城建税/7%", orderNum = "11")
    private String chenJianShui;
    
    @Excel(name = "教育附加/3%", orderNum = "12")
    private String jiaoYuFuJiaShui; 
    
    @Excel(name = "地方教育附加(一般)/2%", orderNum = "13")
    private String diFangJiaoYuFuJiaShui;
    
    @Excel(name = "印花税", orderNum = "14")
    private String yinHuaShui; 
    
    @Excel(name = "消费税", orderNum = "14")
    private String xiaoFeiShui; 
    
    @Excel(name = "文化事业建设费", orderNum = "15")
    private String wenHuaShiYejianSheShui; 
    
    @Excel(name = "城镇土地使用税(季度申报)", orderNum = "16")
    private String chengZhenTuDiShui; 
    
    @Excel(name = "房产税   (季度申报)", orderNum = "17")
    private String fangChanShui; 
    
    @Excel(name = "残疾人就业保障金(年度申报)", orderNum = "18")
    private String canJiRenBaoZhangJin; 
    
  
    @Excel(name = "企业所得税", orderNum = "22")
    private String qiYeSuoDeShui; 
    
    
    
    
}
 