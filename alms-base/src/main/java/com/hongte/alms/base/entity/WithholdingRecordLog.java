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
 * 执行代扣记录日志表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
@ApiModel
@TableName("tb_withholding_record_log")
public class WithholdingRecordLog extends Model<WithholdingRecordLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
	@TableId(value="log_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer logId;
    /**
     * 原业务单号
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "原业务单号")
	private String originalBusinessId;
    /**
     * 批次期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "批次期数")
	private String afterId;
    /**
     * 本次代扣金额(元)
     */
	@TableField("current_amount")
	@ApiModelProperty(required= true,value = "本次代扣金额(元)")
	private BigDecimal currentAmount;
    /**
     * 银行卡预留手机号码
     */
	@TableField("phone_number")
	@ApiModelProperty(required= true,value = "银行卡预留手机号码")
	private String phoneNumber;
    /**
     * 代扣状态(1:成功,0:失败;2:处理中)
     */
	@TableField("repay_status")
	@ApiModelProperty(required= true,value = "代扣状态(1:成功,0:失败;2:处理中)")
	private Integer repayStatus;
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


	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
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

	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
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

	@Override
	protected Serializable pkVal() {
		return this.logId;
	}

	@Override
	public String toString() {
		return "WithholdingRecordLog{" +
			", logId=" + logId +
			", originalBusinessId=" + originalBusinessId +
			", afterId=" + afterId +
			", currentAmount=" + currentAmount +
			", phoneNumber=" + phoneNumber +
			", repayStatus=" + repayStatus +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			"}";
	}
}
