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
 * 贷后催收状态表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-09-19
 */
@ApiModel
@TableName("tb_collection_status")
public class CollectionStatus extends Model<CollectionStatus> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务编号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 业务还款计划列表主键(tb_repayment_biz_plan_list.plan_list_id)
     */
	@TableField("crp_id")
	@ApiModelProperty(required= true,value = "业务还款计划列表主键(tb_repayment_biz_plan_list.plan_list_id)")
	private String crpId;
    /**
     * 贷后状态，1：电催，50：催收中，100：已移交法务，200：已关闭
     */
	@TableField("collection_status")
	@ApiModelProperty(required= true,value = "贷后状态，1：电催，50：催收中，100：已移交法务，200：已关闭")
	private Integer collectionStatus;
    /**
     * 电催专员用户ID
     */
	@TableField("phone_staff")
	@ApiModelProperty(required= true,value = "电催专员用户ID")
	private String phoneStaff;
    /**
     * 催收专员用户ID
     */
	@TableField("visit_staff")
	@ApiModelProperty(required= true,value = "催收专员用户ID")
	private String visitStaff;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建用户ID
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户ID")
	private String createUser;
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
    /**
     * 描述
     */
	@ApiModelProperty(required= true,value = "描述")
	private String describe;
    /**
     * 设置催收的方式：1，界面手动设置；2，定时器自动设置
     */
	@TableField("set_way")
	@ApiModelProperty(required= true,value = "设置催收的方式：1，界面手动设置；2，定时器自动设置")
	private Integer setWay;
    /**
     * 还款计划类型：1.一般还款计划，2.末期还款计划
     */
	@TableField("crp_type")
	@ApiModelProperty(required= true,value = "还款计划类型：1.一般还款计划，2.末期还款计划")
	private Integer crpType;
    /**
     * 原业务ID
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "原业务ID")
	private String originalBusinessId;
    /**
     * after_id
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "after_id")
	private String afterId;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	public Integer getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(Integer collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getPhoneStaff() {
		return phoneStaff;
	}

	public void setPhoneStaff(String phoneStaff) {
		this.phoneStaff = phoneStaff;
	}

	public String getVisitStaff() {
		return visitStaff;
	}

	public void setVisitStaff(String visitStaff) {
		this.visitStaff = visitStaff;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getSetWay() {
		return setWay;
	}

	public void setSetWay(Integer setWay) {
		this.setWay = setWay;
	}

	public Integer getCrpType() {
		return crpType;
	}

	public void setCrpType(Integer crpType) {
		this.crpType = crpType;
	}

	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "CollectionStatus{" +
			", businessId=" + businessId +
			", crpId=" + crpId +
			", collectionStatus=" + collectionStatus +
			", phoneStaff=" + phoneStaff +
			", visitStaff=" + visitStaff +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", describe=" + describe +
			", setWay=" + setWay +
			", crpType=" + crpType +
			", originalBusinessId=" + originalBusinessId +
			", afterId=" + afterId +
			"}";
	}
}
