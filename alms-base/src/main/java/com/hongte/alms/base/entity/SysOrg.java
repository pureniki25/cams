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
 * 组织机构表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@ApiModel
@TableName("tb_sys_org")
public class SysOrg extends Model<SysOrg> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="ID", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "ID")
	private Long id;
    /**
     * 机构编码
     */
	@TableField("ORG_CODE")
	@ApiModelProperty(required= true,value = "机构编码")
	private String orgCode;
    /**
     * 机构名称
     */
	@TableField("ORG_NAME")
	@ApiModelProperty(required= true,value = "机构名称")
	private String orgName;
    /**
     * 机构名称中文
     */
	@TableField("ORG_NAME_CN")
	@ApiModelProperty(required= true,value = "机构名称中文")
	private String orgNameCn;
    /**
     * 父机构编码
     */
	@TableField("PARENT_ORG_CODE")
	@ApiModelProperty(required= true,value = "父机构编码")
	private String parentOrgCode;
    /**
     * 根机构编码
     */
	@TableField("ROOT_ORG_CODE")
	@ApiModelProperty(required= true,value = "根机构编码")
	private String rootOrgCode;
    /**
     * 机构路径
     */
	@TableField("ORG_PATH")
	@ApiModelProperty(required= true,value = "机构路径")
	private String orgPath;
    /**
     * 机构类型
     */
	@TableField("ORG_TYPE")
	@ApiModelProperty(required= true,value = "机构类型")
	private String orgType;
    /**
     * 顺序号
     */
	@TableField("SEQUENCE")
	@ApiModelProperty(required= true,value = "顺序号")
	private Integer sequence;
    /**
     * 备注
     */
	@TableField("REMARK")
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 创建人
     */
	@TableField("CREATE_OPERATOR")
	@ApiModelProperty(required= true,value = "创建人")
	private String createOperator;
    /**
     * 创建时间
     */
	@TableField("CREATED_DATETIME")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createdDatetime;
    /**
     * 最后修改时间
     */
	@TableField("LAST_MODIFIED_DATETIME")
	@ApiModelProperty(required= true,value = "最后修改时间")
	private Date lastModifiedDatetime;
    /**
     * 乐观锁版本
     */
	@TableField("JPA_VERSION")
	@ApiModelProperty(required= true,value = "乐观锁版本")
	private Integer jpaVersion;
    /**
     * 使用状态
     */
	@TableField("DEL_FLAG")
	@ApiModelProperty(required= true,value = "使用状态")
	private Integer delFlag;
    /**
     * 更新人
     */
	@TableField("UPDATE_OPERATOR")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateOperator;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgNameCn() {
		return orgNameCn;
	}

	public void setOrgNameCn(String orgNameCn) {
		this.orgNameCn = orgNameCn;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public String getRootOrgCode() {
		return rootOrgCode;
	}

	public void setRootOrgCode(String rootOrgCode) {
		this.rootOrgCode = rootOrgCode;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateOperator() {
		return createOperator;
	}

	public void setCreateOperator(String createOperator) {
		this.createOperator = createOperator;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Date getLastModifiedDatetime() {
		return lastModifiedDatetime;
	}

	public void setLastModifiedDatetime(Date lastModifiedDatetime) {
		this.lastModifiedDatetime = lastModifiedDatetime;
	}

	public Integer getJpaVersion() {
		return jpaVersion;
	}

	public void setJpaVersion(Integer jpaVersion) {
		this.jpaVersion = jpaVersion;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getUpdateOperator() {
		return updateOperator;
	}

	public void setUpdateOperator(String updateOperator) {
		this.updateOperator = updateOperator;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysOrg{" +
			", id=" + id +
			", orgCode=" + orgCode +
			", orgName=" + orgName +
			", orgNameCn=" + orgNameCn +
			", parentOrgCode=" + parentOrgCode +
			", rootOrgCode=" + rootOrgCode +
			", orgPath=" + orgPath +
			", orgType=" + orgType +
			", sequence=" + sequence +
			", remark=" + remark +
			", createOperator=" + createOperator +
			", createdDatetime=" + createdDatetime +
			", lastModifiedDatetime=" + lastModifiedDatetime +
			", jpaVersion=" + jpaVersion +
			", delFlag=" + delFlag +
			", updateOperator=" + updateOperator +
			"}";
	}
}
