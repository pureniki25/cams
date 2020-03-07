package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 单位余额表
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
@ApiModel
@TableName("tb_customer_rest_dat")
public class CustomerRestDat extends Model<CustomerRestDat> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
    /**
     * 公司名称
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;
    /**
     * 科目代码
     */
	@ApiModelProperty(required= true,value = "科目代码")
	private String subject;
    /**
     * 科目名称
     */
	@TableField("subject_name")
	@ApiModelProperty(required= true,value = "科目名称")
	private String subjectName;
    /**
     * 单位编码
     */
	@TableField("customer_code")
	@ApiModelProperty(required= true,value = "单位编码")
	private String customerCode;
    /**
     * 单位名称
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "单位名称")
	private String customerName;
    /**
     * 期初金额
     */
	@TableField("first_amount")
	@ApiModelProperty(required= true,value = "期初金额")
	private String firstAmount;
    /**
     * 借方发生金额
     */
	@TableField("borrow_amount")
	@ApiModelProperty(required= true,value = "借方发生金额")
	private String borrowAmount;
    /**
     * 贷方发生金额
     */
	@TableField("alms_amount")
	@ApiModelProperty(required= true,value = "贷方发生金额")
	private String almsAmount;
    /**
     * 开票日期
     */
	@TableField("open_date")
	@ApiModelProperty(required= true,value = "开票日期")
	private String openDate;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


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
	protected Serializable pkVal() {
		return this.id;
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
