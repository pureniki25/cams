package com.hongte.alms.base.collection.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date; 
/**
 * <p>
 * 贷后移交催收日志
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-26
 */
@ApiModel
@TableName("tb_collection_log")
public class CollectionLog extends Model<CollectionLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "日志ID")
	private Integer id;
	/**
	 * 业务编号
	 */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
	/**
	 * 业务还款计划列表主键(tb_repayment_business_plan_list.plan_list_id)
	 */
	@TableField("crp_id")
	@ApiModelProperty(required= true,value = "业务还款计划列表主键(tb_repayment_business_plan_list.plan_list_id)")
	private String crpId;
	/**
	 * 贷后状态，1：电催，50：催收中，100：已移交法务
	 */
	@TableField("after_status")
	@ApiModelProperty(required= true,value = "贷后状态，1：电催，50：催收中，100：已移交法务")
	private Integer afterStatus;
	/**
	 * 催收人用户ID
	 */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "催收人用户ID")
	private String collectionUser;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
	/**
	 * 更新人
	 */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
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
	 * 修改前的贷后状态，1：电催，50：催收中，100：已移交法务,150已拖车登记,200:已关闭
	 */
	@TableField("before_status")
	@ApiModelProperty(required= true,value = "修改前的贷后状态，1：电催，50：催收中，100：已移交法务,150已拖车登记,200:已关闭")
	private Integer beforeStatus;
	/**
	 * 发起这次修改的状态：1：电催，50：催收中，100：已移交法务,150已拖车登记， 200:已关闭
	 */
	@TableField("set_type_status")
	@ApiModelProperty(required= true,value = "发起这次修改的状态：1：电催，50：催收中，100：已移交法务,150已拖车登记， 200:已关闭")
	private Integer setTypeStatus;


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

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	public Integer getAfterStatus() {
		return afterStatus;
	}

	public void setAfterStatus(Integer afterStatus) {
		this.afterStatus = afterStatus;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CollectionLog{" +
				", id=" + id +
				", businessId=" + businessId +
				", crpId=" + crpId +
				", afterStatus=" + afterStatus +
				", collectionUser=" + collectionUser +
				", createTime=" + createTime +
				", createUser=" + createUser +
				", updateTime=" + updateTime +
				", updateUser=" + updateUser +
				", describe=" + describe +
				"}";
	}

	public Integer getSetWay() {
		return setWay;
	}

	public void setSetWay(Integer setWay) {
		this.setWay = setWay;
	}

	public Integer getBeforeStatus() {
		return beforeStatus;
	}

	public void setBeforeStatus(Integer beforeStatus) {
		this.beforeStatus = beforeStatus;
	}

	public Integer getSetTypeStatus() {
		return setTypeStatus;
	}

	public void setSetTypeStatus(Integer setTypeStatus) {
		this.setTypeStatus = setTypeStatus;
	}
}
