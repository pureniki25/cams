package com.hongte.alms.base.vo.module;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/2/8
 */
@ExcelTarget("ApplyDerateVo")
public class ApplyDerateVo {
    
    @Excel(name = "业务编号", orderNum = "1",  isImportField = "true_st")
    private String businessId			;		//业务编号
    @Excel(name = "期数", orderNum = "2",  isImportField = "true_st")
    private String periods				;		//期数
    @Excel(name = "客户名称", orderNum = "3",  isImportField = "true_st")
    private String customerName			;	//客户名称
    @Excel(name = "业务类型名称", orderNum = "4",  isImportField = "true_st")
    private String businessTypeName		;	//业务类型名称
    @Excel(name = "区域", orderNum = "5",  isImportField = "true_st")
    private String districtAreaName		;	//区域
    private String districtId		;	//资产端ID
    @Excel(name = "分公司", orderNum = "6",  isImportField = "true_st")
    private String companyName			;		//分公司
    private String companyId			;		//分公司ID
    @Excel(name = "发起减免人", orderNum = "7",  isImportField = "true_st")
    private String createrName			;		//发起减免人
    private String createrId			;		//发起减免人ID
    @Excel(name = "减免费用项名称", orderNum = "8",  isImportField = "true_st")
    private String derateTypeName		;		//减免费用项名称
    private String derateTypeId		;		//减免费用项Id
    @Excel(name = "应收金额", orderNum = "9",  isImportField = "true_st")
    private String showPayMoney			;	//应收金额
    @Excel(name = "减免金额", orderNum = "10",  isImportField = "true_st")
    private String derateMoney			;		//减免金额
    @Excel(name = "实收金额", orderNum = "11",  isImportField = "true_st")
    private String realPayMoney			;	//实收金额
    @Excel(name = "减免时间", orderNum = "12",   databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
    private String derateTime			;		//减免时间

    private String applyDerateProcessId;//减免流程ID
    
    public String getApplyDerateProcessId() {
		return applyDerateProcessId;
	}

	public void setApplyDerateProcessId(String applyDerateProcessId) {
		this.applyDerateProcessId = applyDerateProcessId;
	}

	public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getDistrictAreaName() {
        return districtAreaName;
    }

    public void setDistrictAreaName(String districtAreaName) {
        this.districtAreaName = districtAreaName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getDerateTypeName() {
        return derateTypeName;
    }

    public void setDerateTypeName(String derateTypeName) {
        this.derateTypeName = derateTypeName;
    }

    public String getShowPayMoney() {
        return showPayMoney;
    }

    public void setShowPayMoney(String showPayMoney) {
        this.showPayMoney = showPayMoney;
    }

    public String getDerateMoney() {
        return derateMoney;
    }

    public void setDerateMoney(String derateMoney) {
        this.derateMoney = derateMoney;
    }

    public String getRealPayMoney() {
        return realPayMoney;
    }

    public void setRealPayMoney(String realPayMoney) {
        this.realPayMoney = realPayMoney;
    }

    public String getDerateTime() {
        return derateTime;
    }

    public void setDerateTime(String derateTime) {
        this.derateTime = derateTime;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getDerateTypeId() {
        return derateTypeId;
    }

    public void setDerateTypeId(String derateTypeId) {
        this.derateTypeId = derateTypeId;
    }
}
