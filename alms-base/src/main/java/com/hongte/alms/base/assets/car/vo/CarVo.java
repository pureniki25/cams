package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("carVo")
public class CarVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Excel(name = "业务编号", orderNum = "1",  isImportField = "true_st")
	private String businessId;
    @Excel(name = "分区", orderNum = "2",   isImportField = "true_st")
	private String districtAreaName;
    @Excel(name = "分公司", orderNum = "3",   isImportField = "true_st")
	private String companyName;
    @Excel(name = "客户姓名", orderNum = "4",   isImportField = "true_st")
	private String customerName;
    @Excel(name = "借款金额", orderNum = "5",   isImportField = "true_st")
	private BigDecimal borrowMoney;
    @Excel(name = "还款金额", orderNum = "6",   isImportField = "true_st")
	private BigDecimal repaidAmount;
    @Excel(name = "车牌号", orderNum = "7",   isImportField = "true_st")
    private String  licensePlateNumber;
    @Excel(name = "车型", orderNum = "8",   isImportField = "true_st")
    private String model;
    @Excel(name = "评估金额", orderNum = "9",   isImportField = "true_st")
    private BigDecimal evaluationAmount;
    @Excel(name = "评估日期", orderNum = "10",   isImportField = "true_st")
    private Date evaluationDate;
    @Excel(name = "拖车日期", orderNum = "11",   isImportField = "true_st")
    private Date trailerDate;
    @Excel(name = "状态", orderNum = "12",   isImportField = "true_st")
    private String status;
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}
	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public BigDecimal getEvaluationAmount() {
		return evaluationAmount;
	}
	public void setEvaluationAmount(BigDecimal evaluationAmount) {
		this.evaluationAmount = evaluationAmount;
	}
	public Date getEvaluationDate() {
		return evaluationDate;
	}
	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}
	public Date getTrailerDate() {
		return trailerDate;
	}
	public void setTrailerDate(Date trailerDate) {
		this.trailerDate = trailerDate;
	}
	public BigDecimal getRepaidAmount() {
		return repaidAmount;
	}
	public void setRepaidAmount(BigDecimal repaidAmount) {
		this.repaidAmount = repaidAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
