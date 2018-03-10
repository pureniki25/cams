package com.hongte.alms.base.collection.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
@ApiModel
@TableName("tb_collection_person_set_detail")
public class CollectionPersonSetDetail extends Model<CollectionPersonSetDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,uuid形式
     */
	@ApiModelProperty(required= true,value = "主键,uuid形式")
	private String id;
    /**
     * 催收人员设置ID,对应tb_collection_person_set的col_person_id
     */
	@TableField("col_person_id")
	@ApiModelProperty(required= true,value = "催收人员设置ID,对应tb_collection_person_set的col_person_id")
	private String colPersonId;
    /**
     * 催收人员id
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "催收人员id")
	private String userId;
    /**
     * 1表示清算1组，2表示清算2组
     */
	@ApiModelProperty(required= true,value = "1表示清算1组，2表示清算2组")
	private Integer team;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColPersonId() {
		return colPersonId;
	}

	public void setColPersonId(String colPersonId) {
		this.colPersonId = colPersonId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getTeam() {
		return team;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CollectionPersonSetDetail{" +
			", id=" + id +
			", colPersonId=" + colPersonId +
			", userId=" + userId +
			", team=" + team +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
