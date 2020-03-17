package com.hongte.alms.base.vo.cams;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageRequest;

import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 科目余额表
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@ApiModel
public class BalanceVo extends PageRequest {
	private static final long serialVersionUID = 1L;

	private String balanceType;
    private String companyName;
    private String balanceSubeType;
	private String balanceName;
	private String subjectName;
	private String borrowAmount;
	private String restAmount;
	private String firstAmount; //本月数
	private Integer rowNum;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date beginDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date endDate;

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getBalanceSubeType() {
		return balanceSubeType;
	}

	public void setBalanceSubeType(String balanceSubeType) {
		this.balanceSubeType = balanceSubeType;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBalanceName() {
		return balanceName;
	}

	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(String borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public String getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(String restAmount) {
		this.restAmount = restAmount;
	}

	public String getFirstAmount() {
		return firstAmount;
	}

	public void setFirstAmount(String firstAmount) {
		this.firstAmount = firstAmount;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	

}
