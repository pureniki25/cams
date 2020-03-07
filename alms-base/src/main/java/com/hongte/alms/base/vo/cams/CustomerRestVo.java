package com.hongte.alms.base.vo.cams;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.vo.PageRequest;

import io.swagger.annotations.ApiModel;
/**
 * <p>
 * 单位余额表
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
public class CustomerRestVo extends PageRequest {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 公司名称
     */
	private String companyName;
    /**
     * 科目代码
     */
	private String subject;
    /**
     * 科目名称
     */
	private String subjectName;
    /**
     * 单位编码
     */
	private String customerCode;
    /**
     * 单位名称
     */
	private String customerName;
    /**
     * 期初金额
     */
	private String firstAmount;
    /**
     * 借方发生金额
     */
	private String borrowAmount;
    /**
     * 贷方发生金额
     */
	private String almsAmount;
	
	   /**
     * 期末余额
     */
	private String restAmount;
    /**
     * 开票日期
     */
	private String openDate;
	
    
	
	
	
	private Date createTime;
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date beginDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date endDate;

	


	public String getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(String restAmount) {
		this.restAmount = restAmount;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getFirstAmount() {
		return firstAmount;
	}

	public void setFirstAmount(String firstAmount) {
		this.firstAmount = firstAmount;
	}

	public String getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(String borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public String getAlmsAmount() {
		return almsAmount;
	}

	public void setAlmsAmount(String almsAmount) {
		this.almsAmount = almsAmount;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	@Override
	public String toString() {
		return "CustomerRestDat{" +
			", id=" + id +
			", companyName=" + companyName +
			", subject=" + subject +
			", subjectName=" + subjectName +
			", customerCode=" + customerCode +
			", customerName=" + customerName +
			", firstAmount=" + firstAmount +
			", borrowAmount=" + borrowAmount +
			", almsAmount=" + almsAmount +
			", openDate=" + openDate +
			", createTime=" + createTime +
			"}";
	}
}
