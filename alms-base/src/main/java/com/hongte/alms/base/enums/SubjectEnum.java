package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2019/03/01
 */
public enum SubjectEnum implements IEnum {

    CUSTOMER_SUBJECT("2121","供应商科目代码",""),
    YING_FU_GONG_ZI_SUBJECT("2151","应付工资",""),
    CLIENT_SUBJECT("1131","客户科目代码",""),
    TAX_SUBJECT("2171-01-01","进项税额科目代码",""),
    WAIT_TAX_SUBJECT("2171-20","待抵扣税额科目代码",""),
    BANK_SUBJECT("1002","公司银行账号科目代码",""),
    CASH_SUBJECT("1001","现金支付科目代码",""),
     
    
    ZENG_ZHI_SHUI_SUBJECT("2171-02","增值税3%",""),
    CHENG_JIAN_SHUI_SUBJECT("2171-08","城建税/7%",""),
    JIAO_YU_SUBJECT("2171-13","教育附加/3%",""), 
    DI_FANG_JIAO_YU_SUBJECT("2171-14","地方教育附加/2%",""),
    YIN_HUA_SUBJECT("2171-15","印花税0.03%",""),
    YING_JIAO_GE_REN_SUBJECT("2171-12","应交个人所得税",""),
    YING_JIAO_SUBJECT("2171-06","应交所得税",""),
    SHE_BAO_GONG_SI_SUBJECT("5502-02","社保公司部分",""),
    SHE_BAO_GE_REN_SUBJECT("1133-01","社保个人部分",""),
	
    GONG_JI_JIN_GONG_SI_SUBJECT("5502-04","公积金公司部分",""),
    GONG_JI_JIN_GE_REN_SUBJECT("1133-03","公积金个人部分",""),
    JIE_ZHUAN_CHENG_BEN_SUBJECT("5401-03","结转成本科目",""),
    GONG_CHENG_CHENG_BEN_SUBJECT("5401-02","结转工程成本",""),
    LAO_WU_CHENG_BEN_SUBJECT("4107","劳务成本",""),
	CHENG_BEN_SUBJECT("4101-01","成本科目",""),
	 ZHI_FU_SUBJECT("1002-01","支付科目",""),
	 SHUI_JIN_2171_12("2171-12","计提个人所得税",""),
	 SHUI_JIN_2171_15("2171-15","计提印花税",""),
	 SHUI_JIN_2171_14("2171-14","计提地方教育附加税",""),
	 SHUI_JIN_2171_13("2171-13","计提教育附加税",""),
	 SHUI_JIN_2171_08("2171-08","计提城建税",""),
	 SHUI_JIN_2171_04("2171-04","计提消费税",""),
	 SHUI_JIN_2171_06("2171-06","计提季度企业所得税","5701"),
	 SHUI_JIN_2171_02("2171-02","计提增值税",""),
	 SHUI_JIN_2171_16("2171-16","计提文化建设费","5502-20"),
	 SHUI_JIN_2171_10("2171-10","计提土地使用税","5502-22"),
	 SHUI_JIN_2171_09("2171-09","计提房产税","5502-21"),
	 SHUI_JIN_5502_23("5502-23","残疾人就业保障金","5502-23"),
     JIE_ZHUAN_XIAO_SHOU_CHENG_BEN_5401_01("5401-01","结转本月销售成本",""),
     KU_CUN_SHANG_PIN_1234("1234","库存商品",""),

    YING_SHUI_XIAO_SHOU_5101_01("5101-01","应税销售收人",""),

    XIAO_SHOU_5101_10("5101-10","销售收入","");
	
	
	

    private String value;
    private String desc;
    private String desc2;

    SubjectEnum(final String value, final String desc,final String desc2) {
        this.value = value;
        this.desc = desc;
        this.desc2=desc2;
    }
    public static String getName(String value) {  
    	SubjectEnum[] subjectEnums = values();  
          for (SubjectEnum subjectEnum : subjectEnums) {  
              if (subjectEnum.value.equals(value)) {  
                  return subjectEnum.getDesc();  
              }  
          }  
          return null;  
      } 
    
    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
    @JsonValue
    public String getDesc2(){
        return this.desc2;
    }
}
