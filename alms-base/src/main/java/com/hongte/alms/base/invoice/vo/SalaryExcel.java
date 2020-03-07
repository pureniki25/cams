package com.hongte.alms.base.invoice.vo;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import lombok.Data;

@ExcelTarget("SalaryExcel")
@Data
public class SalaryExcel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;
	private Integer id;
    /**
     * 姓名
     */
	@Excel(name="姓名" , orderNum = "1")
	private String name;
    /**
     * 身份号码
     */
	@Excel(name="证照号码" , orderNum = "2")
	private String idcardNo;
    /**
     * 所得期间起
     */
	@Excel(name="所得期间起" , orderNum = "3")
	private String suoDeQiJianQi;
    /**
     * 所得期间止
     */
	@Excel(name="所得期间止" , orderNum = "4")
	private String suoDeQiJianZhi;
    /**
     * 本期收入
     */
	@Excel(name="本期收入" , orderNum = "5")
	private String benQiShouRu;
	
	   /**
     * 本期免税收入
     */
	@Excel(name="本期免税收入" , orderNum = "5")
	private String benQiMianShuiShouRu;
    /**
     * 基本养老保险
     */
	@Excel(name="基本养老保险费" , orderNum = "6")
	private String jiBenYangLaoBaoXian;
    /**
     * 基本医疗保险
     */
	@Excel(name="基本医疗保险费" , orderNum = "7")
	private String jiBenYiLiaoBaoXian;
    /**
     * 失业保险
     */
	@Excel(name="失业保险费" , orderNum = "8")
	private String shiYeBaoXian;
    /**
     * 住房公积金
     */
	@Excel(name="住房公积金" , orderNum = "9")
	private String zhuFangGongJiJin;
    /**
     * 累计子女教育
     */
	@Excel(name="累计子女教育" , orderNum = "10")
	private String leiJiZiNvJiaoYu;
    /**
     * 累计住房贷款利息
     */
	@Excel(name="累计住房贷款利息" , orderNum = "11")
	private String leiJiZhuFangDaiKuanLiXi;
    /**
     * 累计住房租金
     */
	@Excel(name="累计住房租金" , orderNum = "12")
	private String leiJiZhuFangZuJin;
    /**
     * 累计赡养老人
     */
	@Excel(name="累计赡养老人" , orderNum = "13")
	private String leiJiShanYangLaoRen;
    /**
     * 累计继续教育
     */
	@Excel(name="累计继续教育" , orderNum = "14")
	private String leiJiJiXuJiaoYu;
    /**
     * 企业年金
     */
	@Excel(name="企业(职业)年金" , orderNum = "15")
	private String qiYeNianJin;
    /**
     * 商业健康保险
     */
	@Excel(name="商业健康保险" , orderNum = "16")
	private String shangYeJianKangBaoXian;
    /**
     * 税延养老保险
     */
	@Excel(name="税延养老保险" , orderNum = "17")
	private String shuiYanYangLaoBaoXian;
    /**
     * 其他
     */
	@Excel(name="其他" , orderNum = "18")
	private String qiTa;
    /**
     * 准予扣除的捐赠额
     */
	@Excel(name="准予扣除的捐赠额" , orderNum = "19")
	private String zhunYuKouChuJuanZengKuan;
    /**
     * 税前扣除项目合计
     */
	@Excel(name="税前扣除项目合计" , orderNum = "20")
	private String shuiQianKouChuXiangMuHeJi;
    /**
     * 减免税额
     */
	@Excel(name="减免税额" , orderNum = "21")
	private String jianMianShuiE;
    /**
     * 减除费用标准
     */
	@Excel(name="减除费用标准" , orderNum = "22")
	private String jianChuFeiYongBiaoZhun;
    /**
     * 已扣缴税额
     */
	@Excel(name="已扣缴税额" , orderNum = "23")
	private String yiKouJiaoShuiE;


    
    
  
    
}
 