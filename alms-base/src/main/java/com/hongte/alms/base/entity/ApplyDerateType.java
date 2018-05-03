package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
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
 * 申请减免项目类型表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-08
 */
@ApiModel
@TableName("tb_apply_derate_type")
public class ApplyDerateType extends Model<ApplyDerateType> {

    private static final long serialVersionUID = 1L;

    /**
     * 减免申请项目类型ID
     */
    @TableId("apply_derate_type_id")
	@ApiModelProperty(required= true,value = "减免申请项目类型ID")
	private String applyDerateTypeId;
    /**
     * 减免申请流程ID
     */
	@TableField("apply_derate_process_id")
	@ApiModelProperty(required= true,value = "减免申请流程ID")
	private String applyDerateProcessId;
	
	
    /**
     * 减免申请流程ID
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空")
	private String feeId;
    /**
     * 申请减免类型
     */
	@TableField("derate_type")
	@ApiModelProperty(required= true,value = "申请减免类型")
	private String derateType;
	
    /**
     * 申请减免费用名称
     */
	@TableField("derate_type_name")
	@ApiModelProperty(required= true,value = "申请减免费用名称")
	private String derateTypeName;
    /**
     * 申请减免金额
     */
	@TableField("before_derate_money")
	@ApiModelProperty(required= true,value = "申请减免前的金额")
	private BigDecimal beforeDerateMoney;
	
    /**
     * 申请减免金额
     */
	@TableField("derate_money")
	@ApiModelProperty(required= true,value = "申请减免金额")
	private BigDecimal derateMoney;
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


	public String getDerateTypeName() {
		return derateTypeName;
	}

	public void setDerateTypeName(String derateTypeName) {
		this.derateTypeName = derateTypeName;
	}

	public BigDecimal getBeforeDerateMoney() {
		return beforeDerateMoney;
	}

	public void setBeforeDerateMoney(BigDecimal beforeDerateMoney) {
		this.beforeDerateMoney = beforeDerateMoney;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getApplyDerateTypeId() {
		return applyDerateTypeId;
	}

	public void setApplyDerateTypeId(String applyDerateTypeId) {
		this.applyDerateTypeId = applyDerateTypeId;
	}

	public String getApplyDerateProcessId() {
		return applyDerateProcessId;
	}

	public void setApplyDerateProcessId(String applyDerateProcessId) {
		this.applyDerateProcessId = applyDerateProcessId;
	}

	public String getDerateType() {
		return derateType;
	}

	public void setDerateType(String derateType) {
		this.derateType = derateType;
	}

	public BigDecimal getDerateMoney() {
		return derateMoney;
	}

	public void setDerateMoney(BigDecimal derateMoney) {
		this.derateMoney = derateMoney;
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

	@Override
	protected Serializable pkVal() {
		return this.applyDerateTypeId;
	}

	@Override
	public String toString() {
		return "ApplyDerateType{" +
			", applyDerateTypeId=" + applyDerateTypeId +
			", applyDerateProcessId=" + applyDerateProcessId +
			", derateType=" + derateType +
			", derateMoney=" + derateMoney +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
