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
 * 业务结余暂收日志表
 * </p>
 *
 * @author wangjiguang
 * @since 2018-09-07
 */
@ApiModel
@TableName("tb_accountant_over_repay_log")
public class AccountantOverRepayLog extends Model<AccountantOverRepayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志表新主键,贷后专用
     */
    @TableId("log_id")
	@ApiModelProperty(required= true,value = "日志表新主键,贷后专用")
	private String logId;
    /**
     * 日志表主键
     */
	@ApiModelProperty(required= true,value = "日志表主键")
	private Integer id;
    /**
     * 业务编号
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 期数
     */
	@TableField("business_after_id")
	@ApiModelProperty(required= true,value = "期数")
	private String businessAfterId;
    /**
     * 当期结余暂收金额，财务确认后不再变化。根据money_type，如果为0，表示该期消费了结余暂收款，若为1，表示该期收入了结余金额
     */
	@TableField("over_repay_money")
	@ApiModelProperty(required= true,value = "当期结余暂收金额，财务确认后不再变化。根据money_type，如果为0，表示该期消费了结余暂收款，若为1，表示该期收入了结余金额")
	private BigDecimal overRepayMoney;
    /**
     * 0:消费;1:收入;
     */
	@TableField("money_type")
	@ApiModelProperty(required= true,value = "0:消费;1:收入;")
	private Integer moneyType;
    /**
     * 收入结余冻结状态，null或0：未冻结；1：冻结
     */
	@TableField("freeze_status")
	@ApiModelProperty(required= true,value = "收入结余冻结状态，null或0：未冻结；1：冻结")
	private Integer freezeStatus;
    /**
     * [是否退费结余, 0:正常结余; 1:退费结余]
     */
	@TableField("is_refund")
	@ApiModelProperty(required= true,value = "[是否退费结余, 0:正常结余; 1:退费结余]")
	private Integer isRefund;
    /**
     * 是否暂收款转结余(0:否，1:是)
     */
	@TableField("is_temporary")
	@ApiModelProperty(required= true,value = "是否暂收款转结余(0:否，1:是)")
	private Integer isTemporary;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人ID
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人ID")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人ID
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人ID")
	private String updateUser;


	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

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

	public String getBusinessAfterId() {
		return businessAfterId;
	}

	public void setBusinessAfterId(String businessAfterId) {
		this.businessAfterId = businessAfterId;
	}

	public BigDecimal getOverRepayMoney() {
		return overRepayMoney;
	}

	public void setOverRepayMoney(BigDecimal overRepayMoney) {
		this.overRepayMoney = overRepayMoney;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public Integer getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(Integer freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	public Integer getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(Integer isTemporary) {
		this.isTemporary = isTemporary;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return this.logId;
	}

	@Override
	public String toString() {
		return "AccountantOverRepayLog{" +
			", logId=" + logId +
			", id=" + id +
			", businessId=" + businessId +
			", businessAfterId=" + businessAfterId +
			", overRepayMoney=" + overRepayMoney +
			", moneyType=" + moneyType +
			", freezeStatus=" + freezeStatus +
			", isRefund=" + isRefund +
			", isTemporary=" + isTemporary +
			", remark=" + remark +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
