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
 * 业务还款计划信息
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@ApiModel
@TableName("tb_repayment_biz_plan")
public class RepaymentBizPlan extends Model<RepaymentBizPlan> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款计划编号(主键)
     */
    @TableId("plan_id")
	@ApiModelProperty(required= true,value = "还款计划编号(主键)")
	private String planId;
    /**
     * 业务编号(若当前业务为展期，则存展期业务编号)
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号(若当前业务为展期，则存展期业务编号)")
	private String businessId;
    /**
     * 当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号，若是原业务，则为空。）
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号，若是原业务，则为空。）")
	private String originalBusinessId;
    /**
     * 还款计划批次号(展期与原业务同属一个还款计划批次号，多次出款的还款计划批次号可能不同)
     */
	@TableField("repayment_batch_id")
	@ApiModelProperty(required= true,value = "还款计划批次号(展期与原业务同属一个还款计划批次号，多次出款的还款计划批次号可能不同)")
	private String repaymentBatchId;
    /**
     * 生成还款计划对应的借款总额(元)
     */
	@TableField("borrow_money")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款总额(元)")
	private BigDecimal borrowMoney;
    /**
     * 生成还款计划对应的借款利率(%)，如11%则存11.0
     */
	@TableField("borrow_rate")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款利率(%)，如11%则存11.0")
	private BigDecimal borrowRate;
    /**
     * 生成还款计划对应的借款利率类型，1：年利率，2：月利率，3：日利率
     */
	@TableField("borrow_rate_unit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款利率类型，1：年利率，2：月利率，3：日利率")
	private Integer borrowRateUnit;
    /**
     * 生成还款计划对应的借款期限
     */
	@TableField("borrow_limit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款期限")
	private Integer borrowLimit;
    /**
     * 生成还款计划对应的借款期限单位，1：月，2：天
     */
	@TableField("borrow_limit_unit")
	@ApiModelProperty(required= true,value = "生成还款计划对应的借款期限单位，1：月，2：天")
	private Integer borrowLimitUnit;
    /**
     * 还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期
     */
	@TableField("plan_status")
	@ApiModelProperty(required= true,value = "还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期")
	private Integer planStatus;
    /**
     * 是否展期还款计划,0:否，1:是
     */
	@TableField("is_defer")
	@ApiModelProperty(required= true,value = "是否展期还款计划,0:否，1:是")
	private Integer isDefer;
    /**
     * 对应原信贷的还款批次号(after_guid)，用作历史数据迁移的标记字段。若非原信贷系统生成的还款计划，则为空
     */
	@TableField("xd_after_guid")
	@ApiModelProperty(required= true,value = "对应原信贷的还款批次号(after_guid)，用作历史数据迁移的标记字段。若非原信贷系统生成的还款计划，则为空")
	private String xdAfterGuid;
    /**
     * 对应原信贷的出款计划ID(out_id)，用作历史数据迁移的标记字段。若非原信贷系统生成的还款计划，则为空
     */
	@TableField("xd_out_id")
	@ApiModelProperty(required= true,value = "对应原信贷的出款计划ID(out_id)，用作历史数据迁移的标记字段。若非原信贷系统生成的还款计划，则为空")
	private Integer xdOutId;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getRepaymentBatchId() {
		return repaymentBatchId;
	}

	public void setRepaymentBatchId(String repaymentBatchId) {
		this.repaymentBatchId = repaymentBatchId;
	}

	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public BigDecimal getBorrowRate() {
		return borrowRate;
	}

	public void setBorrowRate(BigDecimal borrowRate) {
		this.borrowRate = borrowRate;
	}

	public Integer getBorrowRateUnit() {
		return borrowRateUnit;
	}

	public void setBorrowRateUnit(Integer borrowRateUnit) {
		this.borrowRateUnit = borrowRateUnit;
	}

	public Integer getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public Integer getBorrowLimitUnit() {
		return borrowLimitUnit;
	}

	public void setBorrowLimitUnit(Integer borrowLimitUnit) {
		this.borrowLimitUnit = borrowLimitUnit;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public Integer getIsDefer() {
		return isDefer;
	}

	public void setIsDefer(Integer isDefer) {
		this.isDefer = isDefer;
	}

	public String getXdAfterGuid() {
		return xdAfterGuid;
	}

	public void setXdAfterGuid(String xdAfterGuid) {
		this.xdAfterGuid = xdAfterGuid;
	}

	public Integer getXdOutId() {
		return xdOutId;
	}

	public void setXdOutId(Integer xdOutId) {
		this.xdOutId = xdOutId;
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
		return this.planId;
	}

	@Override
	public String toString() {
		return "RepaymentBizPlan{" +
			", planId=" + planId +
			", businessId=" + businessId +
			", originalBusinessId=" + originalBusinessId +
			", repaymentBatchId=" + repaymentBatchId +
			", borrowMoney=" + borrowMoney +
			", borrowRate=" + borrowRate +
			", borrowRateUnit=" + borrowRateUnit +
			", borrowLimit=" + borrowLimit +
			", borrowLimitUnit=" + borrowLimitUnit +
			", planStatus=" + planStatus +
			", isDefer=" + isDefer +
			", xdAfterGuid=" + xdAfterGuid +
			", xdOutId=" + xdOutId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
