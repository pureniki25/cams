package com.hongte.alms.base.vo.module;


import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author chenzs
 * @since 2018/3/03
 */
public class RepaymentLogVO {

    private static final long serialVersionUID = 1L;



    private String logId;
    @Excel(name = "代扣人", orderNum = "9",  isImportField = "true_st")
	private String userName;
	
    @Excel(name = "业务编号", orderNum = "1",  isImportField = "true_st")
	private String originalBusinessId;
  
    @Excel(name = "期数", orderNum = "2",  isImportField = "true_st")
	private String afterId;
   
    @Excel(name = "业务类型", orderNum = "3",  isImportField = "true_st")
	private String businessTypeName;
	
	
	private String businessTypeId;
	
	private String districtId;
    @Excel(name = "所属分公司", orderNum = "4",  isImportField = "true_st")
	private String companyId;
    
    @Excel(name = "业务获取", orderNum = "5",  isImportField = "true_st")
	private String operatorName;
 
    
    @Excel(name = "客户名", orderNum = "6",  isImportField = "true_st")
	private String customerName;
    
    @Excel(name = "支付公司", orderNum = "7",  isImportField = "true_st")
	private String platformName;
    @Excel(name = "代扣金额", orderNum = "8",  isImportField = "true_st")
	private String currentAmount;
    @Excel(name = "代扣时间", orderNum = "10", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
	private String createTime;
    
    @Excel(name = "代扣结果", orderNum = "11",  isImportField = "true_st")
	private String repayStatus;
    @Excel(name = "备注", orderNum = "12",  isImportField = "true_st")
	private String remark;
    
	
	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		if("1".equals(repayStatus)) {
			repayStatus="成功";
			
		}else if("0".equals(repayStatus)) {
			repayStatus="失败";
		}else {
			repayStatus="处理中";
		}
		this.repayStatus = repayStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
}
