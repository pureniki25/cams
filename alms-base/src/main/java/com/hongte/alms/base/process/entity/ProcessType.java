package com.hongte.alms.base.process.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 流程类型
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@ApiModel
@TableName("tb_process_type")
public class ProcessType extends Model<ProcessType> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程类别ID
     */
    @TableId("type_id")
	@ApiModelProperty(required= true,value = "流程类别ID")
	private String typeId;
    /**
     * 流程类别编码
     */
	@TableField("type_code")
	@ApiModelProperty(required= true,value = "流程类别编码")
	private String typeCode;
    /**
     * 类别名称
     */
	@TableField("type_name")
	@ApiModelProperty(required= true,value = "类别名称")
	private String typeName;
    /**
     * 单个业务该流程支持同时发起次数
     */
	@TableField("can_item_running_count")
	@ApiModelProperty(required= true,value = "单个业务该流程支持同时发起次数")
	private Integer canItemRunningCount;
    /**
     * 单个业务该流程支持发起总次
     */
	@TableField("can_item_total_count")
	@ApiModelProperty(required= true,value = "单个业务该流程支持发起总次")
	private Integer canItemTotalCount;
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


	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getCanItemRunningCount() {
		return canItemRunningCount;
	}

	public void setCanItemRunningCount(Integer canItemRunningCount) {
		this.canItemRunningCount = canItemRunningCount;
	}

	public Integer getCanItemTotalCount() {
		return canItemTotalCount;
	}

	public void setCanItemTotalCount(Integer canItemTotalCount) {
		this.canItemTotalCount = canItemTotalCount;
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
		return this.typeId;
	}

	@Override
	public String toString() {
		return "ProcessType{" +
			", typeId=" + typeId +
			", typeCode=" + typeCode +
			", typeName=" + typeName +
			", canItemRunningCount=" + canItemRunningCount +
			", canItemTotalCount=" + canItemTotalCount +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
