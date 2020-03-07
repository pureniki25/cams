package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 公司表
 * </p>
 *
 * @author wjg
 * @since 2019-01-16
 */
@ApiModel
@TableName("tb_cams_company")
public class CamsCompany extends Model<CamsCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    @TableId("company_id")
	@ApiModelProperty(required= true,value = "公司ID")
	private String companyId;
    
    @TableId("is_del")
 	@ApiModelProperty(required= true,value = "是否删除")
 	private Integer isDel;
    /**
     * 公司名称
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;
	
	 /**
     * 公司识别号
     */
	@TableField("company_no")
	@ApiModelProperty(required= true,value = "公司识别号")
	private String companyNo;
	
	
	
	 /**
     * 公司法人
     */
	@TableField("company_owner")
	@ApiModelProperty(required= true,value = "公司法人")
	private String companyOwner;
	
	 /**
     * 公司状态
     */
	@TableField("company_status")
	@ApiModelProperty(required= true,value = "公司状态")
	private String companyStatus;
	
	
	 /**
     * 公司类型
     */
	@TableField("company_type")
	@ApiModelProperty(required= true,value = "公司类型")
	private String companyType;
	
	
	 /**
     * 公司财务制度
     */
	@TableField("company_rule")
	@ApiModelProperty(required= true,value = "财务制度")
	private String companyRule;
	
	
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
	
    /**
     * 序号
     */
	@TableField("idx")
	@ApiModelProperty(required= true,value = "序号")
	private Integer idx;
	
	
	 
	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public String getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyRule() {
		return companyRule;
	}

	public void setCompanyRule(String companyRule) {
		this.companyRule = companyRule;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyOwner() {
		return companyOwner;
	}

	public void setCompanyOwner(String companyOwner) {
		this.companyOwner = companyOwner;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.companyId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "CamsCompany{" +
			", companyId=" + companyId +
			", companyName=" + companyName +
			", createTime=" + createTime +
				", companyNo=" + companyNo +
					", companyOwner=" + companyOwner +
			"}";
	}
}
