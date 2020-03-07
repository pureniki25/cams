package com.hongte.alms.base.vo.cams;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageRequest;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 科目余额表
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@ApiModel
public class SubjectRestVo extends PageRequest {
	private static final long serialVersionUID = 1L;

	private String openDate;
    private String companyName;
	private String subject;
	private String subjectName;
	private String borrowAmount;
	private String almsAmount;
	private String restAmount;
	private String firstAmount;
	private String groupBySubject;
	private String groupByType;
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date beginDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date endDate;

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
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

	//按二级科目和三级科目一起分组
	public String getGroupBySubject() {
		if (subject.length()<7) {
			return subject.substring(0, 4);
		} else {
			return subject.substring(0, 7);
		}

	}
	
	//按一级级科目分组
	public String getGroupByFirstSubject() {
		return subject.substring(0, 4);
	}
    
	
	
	public String getGroupByType() {
		if (!StringUtil.isEmpty(subject)) {
			return subject.substring(0, 1);
		} else {
			return subject;
		}

	}

	public void setGroupByType(String groupByType) {
		this.groupByType = groupByType;
	}

	public void setGroupBySubject(String groupBySubject) {
		this.groupBySubject = groupBySubject;
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
