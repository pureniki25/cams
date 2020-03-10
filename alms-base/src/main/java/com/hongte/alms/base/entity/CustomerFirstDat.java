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
 * 单位期初余额表
 * </p>
 *
 * @author czs
 * @since 2020-02-11
 */
@ApiModel
@TableName("tb_customer_first_dat")
public class CustomerFirstDat extends Model<CustomerFirstDat> {

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
     * 单位代码
     */
	@TableField("customer_code")
	@ApiModelProperty(required= true,value = "单位代码")
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
     * 科目代码
     */
	@TableField(exist = false)
	@ApiModelProperty(required= true,value = "科目代码")
	private String subject;
	
	
    /**
     * 科目名称
     */
	@TableField(exist = false)
	@ApiModelProperty(required= true,value = "科目名称")
	private String subjectName;
	
	
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
    /**
     * 期初金额的年份
     */
	@ApiModelProperty(required= true,value = "期初金额的年份")
	private String period;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
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

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	


	@Override
	public String toString() {
		return "CustomerFirstDat{" +
			", id=" + id +
			", companyName=" + companyName +
			", customerCode=" + customerCode +
			", customerName=" + customerName +
			", firstAmount=" + firstAmount +
			", createTime=" + createTime +
			", subject=" + subject +
			", subjectName=" + subjectName +
			", period=" + period +
			"}";
	}
}
