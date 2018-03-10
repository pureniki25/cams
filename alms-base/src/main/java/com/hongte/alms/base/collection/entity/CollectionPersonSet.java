package com.hongte.alms.base.collection.entity;

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
 * @author 曾坤
 * @since 2018-03-08
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
			", createTime=" + createTime +
			", creatUser=" + creatUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
