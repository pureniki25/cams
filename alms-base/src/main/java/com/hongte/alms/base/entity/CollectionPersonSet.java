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
 * 清算人员设置表
 * </p>
 *
 * @author dengzhiming
 * @since 2018-03-05
 */
@ApiModel
@TableName("tb_collection_person_set")
public class CollectionPersonSet extends Model<CollectionPersonSet> {

    private static final long serialVersionUID = 1L;

    /**
     * 催收人员设置ID
     */
    @TableId("col_person_id")
	@ApiModelProperty(required= true,value = "催收人员设置ID")
	private String colPersonId;
    /**
     * 区域编码
     */
	@TableField("area_code")
	@ApiModelProperty(required= true,value = "区域编码")
	private String areaCode;
    /**
     * 分公司编码
     */
	@TableField("company_code")
	@ApiModelProperty(required= true,value = "分公司编码")
	private String companyCode;
    /**
     * 清算一（电催）人员用户Id，多个人员用户ID用逗号分隔
     */
	@TableField("collect1_person")
	@ApiModelProperty(required= true,value = "清算一（电催）人员用户Id，多个人员用户ID用逗号分隔")
	private String collect1Person;
	@TableField("collect2_person")
	@ApiModelProperty(required= true,value = "")
	private String collect2Person;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建
     */
	@TableField("creat_user")
	@ApiModelProperty(required= true,value = "创建")
	private String creatUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新用户ID
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户ID")
	private String updateUser;


	public String getColPersonId() {
		return colPersonId;
	}

	public void setColPersonId(String colPersonId) {
		this.colPersonId = colPersonId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCollect1Person() {
		return collect1Person;
	}

	public void setCollect1Person(String collect1Person) {
		this.collect1Person = collect1Person;
	}

	public String getCollect2Person() {
		return collect2Person;
	}

	public void setCollect2Person(String collect2Person) {
		this.collect2Person = collect2Person;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatUser() {
		return creatUser;
	}

	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.colPersonId;
	}

	@Override
	public String toString() {
		return "CollectionPersonSet{" +
			", colPersonId=" + colPersonId +
			", areaCode=" + areaCode +
			", companyCode=" + companyCode +
			", collect1Person=" + collect1Person +
			", collect2Person=" + collect2Person +
			", createTime=" + createTime +
			", creatUser=" + creatUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
