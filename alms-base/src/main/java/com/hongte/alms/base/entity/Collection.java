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
 * [催收信息主表]
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
@ApiModel
@TableName("tb_collection")
public class Collection extends Model<Collection> {

    private static final long serialVersionUID = 1L;

    /**
     * [业务编号]
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "[业务编号]")
	private String businessId;
    /**
     * [还款期数]
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "[还款期数]")
	private String afterId;
    /**
     * [状态]
     */
	@ApiModelProperty(required= true,value = "[状态]")
	private String status;
    /**
     * [催收业务员]
     */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "[催收业务员]")
	private String collectionUser;
    /**
     * [分配给催收业务员时所写的备注]
     */
	@TableField("assign_remark")
	@ApiModelProperty(required= true,value = "[分配给催收业务员时所写的备注]")
	private String assignRemark;
    /**
     * [创建人]
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "[创建人]")
	private String createUser;
    /**
     * [创建时间]
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "[创建时间]")
	private Date createTime;
    /**
     * [更新人]
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "[更新人]")
	private String updateUser;
    /**
     * [更新时间]
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "[更新时间]")
	private Date updateTime;
    /**
     * [贷后管理移交催收的备注]
     */
	@TableField("hand_to_remark")
	@ApiModelProperty(required= true,value = "[贷后管理移交催收的备注]")
	private String handToRemark;
    /**
     * 业务流程编号
     */
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "业务流程编号")
	private String processId;


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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
	}

	public String getAssignRemark() {
		return assignRemark;
	}

	public void setAssignRemark(String assignRemark) {
		this.assignRemark = assignRemark;
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

	public String getHandToRemark() {
		return handToRemark;
	}

	public void setHandToRemark(String handToRemark) {
		this.handToRemark = handToRemark;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "Collection{" +
			", businessId=" + businessId +
			", afterId=" + afterId +
			", status=" + status +
			", collectionUser=" + collectionUser +
			", assignRemark=" + assignRemark +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", handToRemark=" + handToRemark +
			", processId=" + processId +
			"}";
	}
}
