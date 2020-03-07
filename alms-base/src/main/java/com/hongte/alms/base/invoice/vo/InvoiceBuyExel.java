package com.hongte.alms.base.invoice.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("InvoiceBuyExel")
@Data
public class InvoiceBuyExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
    @Excel(name = "发票代码", orderNum = "1")
    private String faPiaoDaiMa;
    @Excel(name = "发票号", orderNum = "2") 
    private String faPiaoHaoMa;
    @Excel(name = "开票日期", orderNum = "3")
    private String kaiPiaoRiQi;
    @Excel(name = "销方名称", orderNum = "4")
    private String xiaoFangMingChen; 
    @Excel(name = "销方税号", orderNum = "5")
    private String xiaoFangShuiHao; 
    @Excel(name = "销方地址", orderNum = "6")
    private String xiaoFangDiZhi; //'业务编号'
    @Excel(name = "销方银行账号", orderNum = "7")
    private String xiaoFangYingHangZhangHao;//'客户名称'

    @Excel(name = "合计金额", orderNum = "8")
    private String heJiJine;//'期数'
    @Excel(name = "合计税额", orderNum = "9")
    private String heJiShuie;//'所属分公司'
    @Excel(name = "价税合计", orderNum = "10")
    private String jiaShuiHeJi;//'转账金额'

    @Excel(name = "备注", orderNum = "11")
    private String beiZhu;//'本期应还金额'
    @Excel(name = "作废标志", orderNum = "12")
    private String zuoFeiBiaoZhi;//'实际转账人'
    @Excel(name = "发票种类", orderNum = "13")
    private String faPiaoZhongLei;//'转账日期'
    
    @Excel(name = "行号", orderNum = "14")
    private String hangHao;//'转账日期'
    
    
    @Excel(name = "产品名称", orderNum = "15")
    private String chanPinMingChen;//'转账日期'
    
    @Excel(name = "产品型号", orderNum = "16")
    private String changPinXingHao;//'转账日期'
    @Excel(name = "计量单位", orderNum = "17")
    private String jiLiangDanWei;//'转账日期'
    @Excel(name = "数量", orderNum = "18")
    private String shuLiang;//'转账日期'
    @Excel(name = "单价", orderNum = "19")
    private String danJia;//'转账日期'
    @Excel(name = "不含税金额", orderNum = "20")
    private String buHanShuiJine;//'转账日期'
    @Excel(name = "税率", orderNum = "21")
    private String shuiLv;//'转账日期'
    @Excel(name = "税额", orderNum = "22")
    private String shuie;//'转账日期'
    @Excel(name = "含税金额", orderNum = "23")
    private String hanShuiJine;//'转账日期'
    
    @Excel(name = "扫描时间", orderNum = "24")
    private String saoMiaoShiJian;//'转账日期'
    
    
    @Excel(name = "购方公司", orderNum = "25")
    private String gouFangGongSi;//'转账日期'
    
    @Excel(name = "扫描人", orderNum = "26")
    private String saoMiaoRen;//'转账日期'


    @Excel(name = "认证标志", orderNum = "27")
    private String renZhengBiaoZhi;//'转账日期'
    
    @Excel(name = "认证时间", orderNum = "28")
    private String renZhengShjJian;//'转账日期'
    
    @Excel(name = "认证所属期", orderNum = "29")
    private String renZhengSuoShuQi;//'转账日期'
 
    
}
