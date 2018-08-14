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

    CYDZQ_TYPE(1, "车易贷展期", 1001, Constant.CAR_LOAN, 6),
    FSDZQ_TYPE(2, "房速贷展期", 1002, Constant.HOUSE_LOAN, 5),
    JRCC_TYPE(3, "金融仓储", -1, Constant.BLANK_STR, -1),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置""
    SN_TYPE(4, "三农金融", -1, Constant.BLANK_STR, -1),		// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    CYD_TYPE(9, "车易贷", 1001, Constant.CAR_LOAN, 6),
    FSD_TYPE(11, "房速贷", 1002, Constant.HOUSE_LOAN, 5),
    CQSZDC_TYPE(12, "车全垫资代采", 1004, Constant.CHE_QUAN_LOAN, 9),
    FPD_TYPE(13, "扶贫贷", 1003, Constant.POVERTY_ALLEVIATION_LOAN, 7),
    QCRZZL_TYPE(14, "汽车融资租赁", -1, Constant.BLANK_STR, -1),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    ESC_TYPE(15, "二手车商贷", 1005, Constant.ER_SHOU_CHE_LOAN, 8),
    YD_CAR_TYPE(20, "一点车贷", 1006, Constant.YI_DIAN_LOAN, 10),
    CREDIT_TYPE(25, "信用贷", -1, Constant.CREDIT_LOAN, -1),	// 没有对应的充值账户，充值账户ID设置-1，充值账户名称设置"
    PROPRIETOR_TYPE(26, "业主贷", 1008, Constant.PROPRIETOR_LOAN, 1),
    INSTALMENT_CREDIT_TYPE(27, "家装分期", 1009, Constant.INSTALMENT_CREDIT_LOAN, 2),
    COMMERCE_COMPANY_TYPE(28, "商贸贷共借", 1010, Constant.COMMERCE_COMPANY_LOAN, 3),
    PROPRIETOR_COMPANY_TYPE(29, "业主贷共借", 1011, Constant.PROPRIETOR_COMPANY_LOAN, 4),
	COMMERCE_TYPE(30, "商贸贷", 1007, Constant.COMMERCE_LOAN, 0),
	CAR_JOINTLY_TYPE(31, "车贷共借", 1012, Constant.CAR_JOINTLY_TYPE, 11),
	HOUSE_JOINTLY_TYPE(32, "房贷共借", 1013, Constant.HOUSE_JOINTLY_TYPE, 11),
	YOU_FANG_TYPE(35, "优房贷", 1002, Constant.HOUSE_LOAN, 5);


    private int value;	// 业务类型ID
    private String name;	// 业务类型名称
    private int rechargeAccountId;	// 代充值账户编号
    private String rechargeAccountName;	// 代充值账户名称
    private int orgType;	// 对应团代网tdUserName

    /**
     * 结构：（businessType, businessTypeName, rechargeAccountId, rechargeAccountName）
     * @param value	业务类型ID
     * @param name	业务类型名称
     * @param rechargeAccountId	充值账户类型ID
     * @param rechargeAccountName	充值账户类型名称
     * modify by huwiqian, 2018-07-20
     */
    BusinessTypeEnum(final int value, final String name, final int rechargeAccountId,
    		final String rechargeAccountName, final int orgType) {
        this.value = value;
        this.name = name;
        this.rechargeAccountId = rechargeAccountId;
        this.rechargeAccountName = rechargeAccountName;
        this.orgType = orgType;
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
    
    public int orgType() {
    	return this.orgType;
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
    
    /**
     * 根据业务类型ID获取orgType
     * @param value
     * @return
     * @author huweiqian
     */
    public static int getOrgTypeByValue(int value) {  
    	BusinessTypeEnum[] businessTypeEnums = values();  
    	for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {  
    		if (businessTypeEnum.value() == value) {  
    			return businessTypeEnum.orgType;
    		}  
    	}  
    	return -1;  
    }
}
