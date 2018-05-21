package com.hongte.alms.base.collection.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * [电话催收分配记录表]
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
@ApiModel
@TableName("tb_collection_log_xd")
public class CollectionLogXd extends Model<CollectionLogXd> {

    private static final long serialVersionUID = 1L;

    /**
     * [编号]
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "[编号]")
	private Integer id;
    /**
     * [业务编号]
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "[业务编号]")
	private String businessId;
    /**
     * [还款期数]
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "[还款期数]")
	private String afterId;
    /**
     * [创建人]
     */
	@TableField("create_user_id")
	@ApiModelProperty(required= true,value = "[创建人]")
	private String createUserId;
    /**
     * [创建时间]
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "[创建时间]")
	private Date createTime;
    /**
     * [电话催收人]
     */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "[电话催收人]")
	private String collectionUser;
    /**
     * [电催分配备注]
     */
	@TableField("collection_remark")
	@ApiModelProperty(required= true,value = "[电催分配备注]")
	private String collectionRemark;
    /**
     * [分配方式0:自动分配,1:手动分配]
     */
	@TableField("collection_type")
	@ApiModelProperty(required= true,value = "[分配方式0:自动分配,1:手动分配]")
	private Integer collectionType;
    /**
     * 是否删除（null或0:否，1:是）
     */
	@TableField("is_delete")
	@ApiModelProperty(required= true,value = "是否删除（null或0:否，1:是）")
	private Integer isDelete;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
	}

	public String getCollectionRemark() {
		return collectionRemark;
	}

	public void setCollectionRemark(String collectionRemark) {
		this.collectionRemark = collectionRemark;
	}

	public Integer getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(Integer collectionType) {
		this.collectionType = collectionType;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CollectionLogXd{" +
			", id=" + id +
			", businessId=" + businessId +
			", afterId=" + afterId +
			", createUserId=" + createUserId +
			", createTime=" + createTime +
			", collectionUser=" + collectionUser +
			", collectionRemark=" + collectionRemark +
			", collectionType=" + collectionType +
			", isDelete=" + isDelete +
			"}";
	}
}
