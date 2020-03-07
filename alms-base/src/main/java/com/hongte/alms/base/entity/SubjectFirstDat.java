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
 * 科目期初余额表
 * </p>
 *
 * @author czs
 * @since 2020-01-20
 */
@ApiModel
@TableName("tb_subject_first_dat")
public class SubjectFirstDat extends Model<SubjectFirstDat> {

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
     * 期初金额
     */
	@TableField("first_amount")
	@ApiModelProperty(required= true,value = "期初金额")
	private String firstAmount;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;

	@TableField("period")
	@ApiModelProperty(required= true,value = "")
	private String period;
	

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SubjectFirstDat{" +
			", id=" + id +
			", companyName=" + companyName +
			", subject=" + subject +
			", subjectName=" + subjectName +
			", firstAmount=" + firstAmount +
			", createTime=" + createTime +
			"}";
	}
}
