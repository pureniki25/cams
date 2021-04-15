package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author 24043
 *
 */
public class CamsConstant {
	
	public static final String COMPANY_TEMP="exportTemp/company.xls";
	
	public static final String SMALL_COMPANY_TEMP="exportTemp/smallCompany.xls";
	
	public enum DirectionEnum implements IEnum {

	    DAI("贷"),
	    JIE("借");

	    private String value;

		@Override
		public Serializable getValue() {
			// TODO Auto-generated method stub
		    return this.value;
		}

		DirectionEnum(final String value) {
	        this.value = value;
	    }

	 

	   
	}
	
	
	public enum JiTiTypeEnum implements IEnum {

	    SALARY("1","工资"),
	    SHE_BAO("2","社保"),
		ZO_CHAN("3","资产"),
		TAX("4","待代扣税"),
		XIAO_SHOU_CHENG_BEN("5","结转销售成本");

	    private String value;
	    private String desc;

	    JiTiTypeEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    @Override
	    public Serializable getValue() {
	        return this.value;
	    }

	    @JsonValue
	    public String getDesc(){
	        return this.desc;
	    }
	}
	
	public enum ProductTypeEnum implements IEnum {

	    SHANG_PIN("商品"),
	    CHAN_PIN("产品"),
	    GONG_CHENG_SHOU_RU("工程收入"),
	    FU_WU_FEI_SHOU_RU("工程收入"),
	    CAI_LIAO("材料"),
		XIAO_SHOU_SHOU_RU("销售收入");

	    private String value;

	    ProductTypeEnum(final String value) {
	        this.value = value;
	    }

	    @Override
	    public Serializable getValue() {
	        return this.value;
	    }

	}

	public enum PickStoreTypeEnum implements IEnum {

	    PICK("1","领料"),
	    STORE("2","入库");

	    private String value;
	    private String desc;

	    PickStoreTypeEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    @Override
	    public Serializable getValue() {
	        return this.value;
	    }

	    @JsonValue
	    public String getDesc(){
	        return this.desc;
	    }
	}
	
	public enum FeeTypeEnum implements IEnum {

	    FEI_YONG_YIN_FU("1","费用银付"),
	    FEI_YONG_XIAN_FU("3","费用现付"),
	    XIAN_JIN("2","现金支付"),
	    SHUI_JIN("4","税金"),
	    SELL("5","销售凭证"),
	    BUY("6","采购凭证");

	    private String value;
	    private String desc;

	    FeeTypeEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }

	    @Override
	    public Serializable getValue() {
	        return this.value;
	    }

	    @JsonValue
	    public String getDesc(){
	        return this.desc;
	    }
	}
	
	
	/**
	 * 公司状态
	 * @author 24043
	 *
	 */
	public enum CompanyStatusEnum implements IEnum {

	  
		
		
		 XIAO_GUI_MO("1","小规模"),
		 YI_BAN_NA_SHUI_REN("2","一般纳税人"),
		 GE_TI_HU("3","个体户");

	    private String value;
	    private String desc;

	    CompanyStatusEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }
	    public static String getValue(String desc) {  
	    	CompanyStatusEnum[] subjectEnums = values();  
	          for (CompanyStatusEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.desc.equals(desc)) {  
	                  return subjectEnum.getValue().toString();  
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
	}
	
	
	/**
	 * 公司状态
	 * @author 24043
	 *
	 */
	public enum CompanyTypeEnum implements IEnum {
		 SHENG_CHAN_XING("1","生产型"),
		 MAO_YI_XING("2","贸易型"),
		 JIN_CHU_KOU_XING("3","进出口型");

	    private String value;
	    private String desc;

	    CompanyTypeEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }
	    public static String getValue(String desc) {  
	    	CompanyTypeEnum[] subjectEnums = values();  
	          for (CompanyTypeEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.desc.equals(desc)) {  
	                  return subjectEnum.getValue().toString();  
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
	}
	
	/**
	 * 公司财务制度
	 * @author 24043
	 *
	 */
	public enum CompanyRuleEnum implements IEnum {

	  
		
		
		 QI_YE("1","企业会计制度"),
		 XIAO_QI_YE("2","小企业会计准则");

	    private String value;
	    private String desc;

	    CompanyRuleEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }
	    public static String getValue(String desc) {  
	    	CompanyRuleEnum[] subjectEnums = values();  
	          for (CompanyRuleEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.desc.equals(desc)) {  
	                  return subjectEnum.getValue().toString();  
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
	}


	/**
	 * 凭证类型
	 * @author 24043
	 *
	 */
	public enum BankTypeEnum implements IEnum {




		SHOU_RU("1","收入"),
		ZHI_CHU("2","支出");

		private String value;
		private String desc;

		BankTypeEnum(final String value, final String desc) {
			this.value = value;
			this.desc = desc;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}

		@JsonValue
		public String getDesc(){
			return this.desc;
		}
	}

	/**
	 * 发票类型
	 * @author 24043
	 *
	 */
	public enum InvoiceTypeEnum implements IEnum {




		PU_PIAO("1","普票"),
		ZHUAN_PIAO("2","专票");

		private String value;
		private String desc;

		InvoiceTypeEnum(final String value, final String desc) {
			this.value = value;
			this.desc = desc;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}

		@JsonValue
		public String getDesc(){
			return this.desc;
		}
	}
	
	
	
	/**
	 * 计提科目，枚举
	 * @author 24043
	 *
	 */
	public enum JiTiSubjectEnum implements IEnum {

	  
		
		
		 SHUI_JIN_2171_12("2171-12","计提个人所得税"),
		 SHUI_JIN_2171_15("2171-15","计提印花税"),
		 SHUI_JIN_2171_14("2171-14","计提地方教育附加税"),
		 SHUI_JIN_2171_13("2171-13","计提教育附加税"),
		 SHUI_JIN_2171_08("2171-08","计提城建税"),
		 SHUI_JIN_2171_06("2171-06","计提季度企业所得税"),
		 SHUI_JIN_5402("5402","计提外经证税金"),
		 SHUI_JIN_2171_02("2171-02","计提增值税");
		
		
		

	    private String value;
	    private String desc;

	    JiTiSubjectEnum(final String value, final String desc) {
	        this.value = value;
	        this.desc = desc;
	    }
	    public static String getName(String value) {  
	    	JiTiSubjectEnum[] subjectEnums = values();  
	          for (JiTiSubjectEnum subjectEnum : subjectEnums) {  
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
	}
	
	/**
	 * 科目类型枚举
	 * @author 24043
	 *
	 */
	public enum SubjectTypeEnum implements IEnum {

	  
		
		
		 ZI_CHAN("1","资产","借"),
		 FU_ZHAI("2","负债","贷"),
		 QUAN_YI("3","权益","贷"), 
		 CHENG_BEN("4","成本","借"),
		 SUN_YI("5","损益","贷");
		
		
		

	    private String value;
	    private String desc;
	    private String direction;

	    SubjectTypeEnum(final String value, final String desc,final String direction) {
	        this.value = value;
	        this.desc = desc;
	        this.direction=direction;
	    }
	    public static String getDesc(String value) {  
	    	SubjectTypeEnum[] subjectEnums = values();  
	          for (SubjectTypeEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.value.equals(value)) {  
	                  return subjectEnum.getDesc();  
	              }  
	          }  
	          return null;  
	      } 
	    
	    public static String getDirect(String value) {  
	    	SubjectTypeEnum[] subjectEnums = values();  
	          for (SubjectTypeEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.value.equals(value)) {  
	                  return subjectEnum.getDirection();  
	              }  
	          }  
	          return null;  
	      } 
	    
	    public static SubjectTypeEnum getEnum(String value) {  
	    	SubjectTypeEnum[] subjectEnums = values();  
	          for (SubjectTypeEnum subjectEnum : subjectEnums) {  
	              if (subjectEnum.value.equals(value)) {  
	                  return subjectEnum;  
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
	    public String getDirection(){
	        return this.direction;
	    }
	}
}
	