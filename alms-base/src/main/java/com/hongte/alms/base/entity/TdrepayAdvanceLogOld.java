package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 偿还垫付记录表（6.28之前数据）
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-11-21
 */
@ApiModel
@TableName("tb_tdrepay_Advance_log_old")
public class TdrepayAdvanceLogOld extends Model<TdrepayAdvanceLogOld> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * 标的ID
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 平台期数
     */
	@ApiModelProperty(required= true,value = "平台期数")
	private Integer period;
    /**
     * 是否结清(0:未结清,1:已结清)
     */
	@TableField("is_complete")
	@ApiModelProperty(required= true,value = "是否结清(0:未结清,1:已结清)")
	private Integer isComplete;
    /**
     * 垫付ID
     */
	@TableField("advance_id")
	@ApiModelProperty(required= true,value = "垫付ID")
	private String advanceId;
    /**
     * 还垫付分润类型:(1、本息；2、分润；3、逾期费用)
     */
	@TableField("repayment_type")
	@ApiModelProperty(required= true,value = "还垫付分润类型:(1、本息；2、分润；3、逾期费用)")
	private Integer repaymentType;
    /**
     * 用户ID，还本息时为垫付人ID，还分润时为分润用户ID
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "用户ID，还本息时为垫付人ID，还分润时为分润用户ID")
	private String userId;
    /**
     * 金额，还本息时为垫付金额+滞纳金，还分润时为分润金额
     */
	@ApiModelProperty(required= true,value = "金额，还本息时为垫付金额+滞纳金，还分润时为分润金额")
	private BigDecimal amount;
    /**
     * 状态(1:成功;2:失败)
     */
	@ApiModelProperty(required= true,value = "状态(1:成功;2:失败)")
	private Integer status;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
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
     * 创建时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public String getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(String advanceId) {
		this.advanceId = advanceId;
	}

	public Integer getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return "TdrepayAdvanceLogOld{" +
			", id=" + id +
			", projectId=" + projectId +
			", period=" + period +
			", isComplete=" + isComplete +
			", advanceId=" + advanceId +
			", repaymentType=" + repaymentType +
			", userId=" + userId +
			", amount=" + amount +
			", status=" + status +
			", remark=" + remark +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
