package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hongte.alms.common.util.Constant;

/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum BusinessTypeEnum implements IEnum {

    CYDZQ_TYPE(1, "车易贷展期", 1, Constant.CAR_LOAN),
    FSDZQ_TYPE(2, "房速贷展期", 2, Constant.HOUSE_LOAN),
    JRCC_TYPE(3, "金融仓储", -1, Constant.BLANK_STR),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置""
    SN_TYPE(4, "三农金融", -1, Constant.BLANK_STR),		// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    CYD_TYPE(9, "车易贷", 1, Constant.CAR_LOAN),
    FSD_TYPE(11, "房速贷", 2, Constant.HOUSE_LOAN),
    CQSZDC_TYPE(12, "车全垫资代采", 4, Constant.CHE_QUAN_LOAN),
    FPD_TYPE(13, "扶贫贷", 3, Constant.POVERTY_ALLEVIATION_LOAN),
    QCRZZL_TYPE(14, "汽车融资租赁", -1, Constant.BLANK_STR),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    ESC_TYPE(15, "二手车商贷", 5, Constant.ER_SHOU_CHE_LOAN),
    YD_CAR_TYPE(20, "一点车贷", 6, Constant.YI_DIAN_LOAN),
    CREDIT_TYPE(25, "信用贷", -1, Constant.CREDIT_LOAN),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    PROPRIETOR_TYPE(26, "业主贷", 8, Constant.PROPRIETOR_LOAN),
    INSTALMENT_CREDIT_TYPE(27, "家装分期", 9, Constant.INSTALMENT_CREDIT_LOAN),
    COMMERCE_COMPANY_TYPE(28, "商贸共借", 10, Constant.COMMERCE_COMPANY_LOAN),
    PROPRIETOR_COMPANY_TYPE(29, "业主共借", 11, Constant.PROPRIETOR_COMPANY_LOAN),
	COMMERCE_TYPE(30, "商贸贷", 7, Constant.COMMERCE_LOAN),
	YOU_FANG_TYPE(35, "优房贷", 2, Constant.HOUSE_LOAN);


    private int value;
    private String name;
    private int rechargeAccountId;
    private String rechargeAccountName;

    /**
     * 结构：（businessType, businessTypeName, rechargeAccountId, rechargeAccountName）
     * @param value	业务类型ID
     * @param name	业务类型名称
     * @param rechargeAccountId	充值账户类型ID
     * @param rechargeAccountName	充值账户类型名称
     * modify by huwiqian, 2018-07-20
     */
    BusinessTypeEnum(final int value, final String name, final int rechargeAccountId, final String rechargeAccountName) {
        this.value = value;
        this.name = name;
        this.rechargeAccountId = rechargeAccountId;
        this.rechargeAccountName = rechargeAccountName;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }
    
    public int value() {
        return this.value;
    }
    
    public int rechargeAccountId() {
    	return this.rechargeAccountId;
    }
    
    public String rechargeAccountName() {
    	return this.rechargeAccountName;
    }
    
    /**
     * 根据业务类型ID获取对应的业务类型名称
     * @param value
     * @return
     * @author huweiqian
     */
    public static String getName(int value) {  
		BusinessTypeEnum[] businessTypeEnums = values();  
        for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
            if (businessTypeEnum.value() == value) {  
                return businessTypeEnum.getName();  
            }  
        }  
        return null;  
    } 
    /**
     * 根据业务类型ID获取充值账户类型ID
     * @param value
     * @return
     * @author huweiqian
     */
    public static int getRechargeAccountId(int value) {  
    	BusinessTypeEnum[] businessTypeEnums = values();  
    	for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
    		if (businessTypeEnum.value() == value) {  
    			return businessTypeEnum.rechargeAccountId();  
    		}  
    	}  
    	return -1;  
    } 
    /**
     * 根据代充值账户类型ID获取充值账户类型名称
     * @param rechargeAccountId
     * @return
     * @author huweiqian
     */
    public static String getRechargeAccountNameByRechargeAccountId(int rechargeAccountId) {  
    	BusinessTypeEnum[] businessTypeEnums = values();  
    	for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
    		if (businessTypeEnum.rechargeAccountId() == rechargeAccountId) {  
    			return businessTypeEnum.rechargeAccountName();  
    		}  
    	}  
    	return null;  
    } 
    /**
     * 根据业务类型ID获取充值账户类型名称
     * @param value
     * @return
     * @author huweiqian
     */
    public static String getRechargeAccountName(int value) {  
    	BusinessTypeEnum[] businessTypeEnums = values();  
    	for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
    		if (businessTypeEnum.value() == value) {  
    			return businessTypeEnum.rechargeAccountName();  
    		}  
    	}  
    	return null;  
    } 
    
    /**
     * 根据代充值账户类型名称获取获取充值账户类型ID
     * @param rechargeAccountName
     * @return
     * @author huweiqian
     */
    public static int getRechargeAccountIdByRechargeAccountName(String rechargeAccountName) {  
    	BusinessTypeEnum[] businessTypeEnums = values();  
    	for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
    		if (businessTypeEnum.rechargeAccountName().equals(rechargeAccountName)) {  
    			return businessTypeEnum.rechargeAccountId();  
    		}  
    	}  
    	return -1;  
    } 
}
