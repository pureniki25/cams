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
 * 财务还款确认日志
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-11-05
 */
@ApiModel
@TableName("tb_confirm_repay_log")
public class ConfirmRepayLog extends Model<ConfirmRepayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
	@TableId(value="id", type= IdType.INPUT)
	@ApiModelProperty(required= true,value = "日志ID")
	private Integer id;
    /**
     * 业务编号
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String originalBusinessId;
    /**
     * 期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "期数")
	private String afterId;
    /**
     * 还款前逾期天数
     */
	@TableField("before_overdays")
	@ApiModelProperty(required= true,value = "还款前逾期天数")
	private BigDecimal beforeOverdays;
    /**
     * 还款前逾期费
     */
	@TableField("before_overamount")
	@ApiModelProperty(required= true,value = "还款前逾期费")
	private BigDecimal beforeOveramount;
    /**
     * 还款后逾期天数
     */
	@TableField("after_overdays")
	@ApiModelProperty(required= true,value = "还款后逾期天数")
	private BigDecimal afterOverdays;
    /**
     * 还款后逾期费
     */
	@TableField("after_overamount")
	@ApiModelProperty(required= true,value = "还款后逾期费")
	private BigDecimal afterOveramount;
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
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getBeforeOverdays() {
		return beforeOverdays;
	}

	public void setBeforeOverdays(BigDecimal beforeOverdays) {
		this.beforeOverdays = beforeOverdays;
	}

	public BigDecimal getBeforeOveramount() {
		return beforeOveramount;
	}

	public void setBeforeOveramount(BigDecimal beforeOveramount) {
		this.beforeOveramount = beforeOveramount;
	}

	public BigDecimal getAfterOverdays() {
		return afterOverdays;
	}

	public void setAfterOverdays(BigDecimal afterOverdays) {
		this.afterOverdays = afterOverdays;
	}

	public BigDecimal getAfterOveramount() {
		return afterOveramount;
	}

	public void setAfterOveramount(BigDecimal afterOveramount) {
		this.afterOveramount = afterOveramount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ConfirmRepayLog{" +
			", id=" + id +
			", originalBusinessId=" + originalBusinessId +
			", afterId=" + afterId +
			", beforeOverdays=" + beforeOverdays +
			", beforeOveramount=" + beforeOveramount +
			", afterOverdays=" + afterOverdays +
			", afterOveramount=" + afterOveramount +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", remark=" + remark +
			"}";
	}
}
