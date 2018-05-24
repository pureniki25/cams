package com.hongte.alms.base.RepayPlan.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 业务还款计划费用项目明细表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-11
 */
@ApiModel
@TableName("tb_car_business_after_detail")
public class CarBusinessAfterDetailDto implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "自增长ID")
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
     * 费用项ID，特殊费用ID(本金：b69a84dc-ed67-4c9f-80bf-89ee8efd5167，利息：556bce4f-f3a9-4b7a-a8b1-43368bebb49c，滞纳金：79069922-e13a-4229-8656-2a1e19b44879，冲应收：adede422-4293-4456-8517-5b4c8874b700，展期未结清服务费：f6b645e8-480b-11e7-8ed5-000c2928bb0d，展期未结清其他费用：3a401d0a-480c-11e7-8ed5-000c2928bb0d)
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "费用项ID，特殊费用ID(本金：b69a84dc-ed67-4c9f-80bf-89ee8efd5167，利息：556bce4f-f3a9-4b7a-a8b1-43368bebb49c，滞纳金：79069922-e13a-4229-8656-2a1e19b44879，冲应收：adede422-4293-4456-8517-5b4c8874b700，展期未结清服务费：f6b645e8-480b-11e7-8ed5-000c2928bb0d，展期未结清其他费用：3a401d0a-480c-11e7-8ed5-000c2928bb0d)")
	private String feeId;
    /**
     * 费用名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "费用名称")
	private String feeName;
    /**
     * 费用对应贷后主表的分类ID，1:本金; 2:利息; 3:服务费; 4:其他费用; 5:违约金;6:冲应收
     */
	@TableField("after_fee_type")
	@ApiModelProperty(required= true,value = "费用对应贷后主表的分类ID，1:本金; 2:利息; 3:服务费; 4:其他费用; 5:违约金;6:冲应收")
	private Integer afterFeeType;
    /**
     * [本期应还金额]
     */
	@TableField("plan_fee_value")
	@ApiModelProperty(required= true,value = "[本期应还金额]")
	private BigDecimal planFeeValue;
    /**
     * 应还日期
     */
	@TableField("plan_repayment_date")
	@ApiModelProperty(required= true,value = "应还日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planRepaymentDate;
    /**
     * [本期实还金额]
     */
	@TableField("actual_fee_value")
	@ApiModelProperty(required= true,value = "[本期实还金额]")
	private BigDecimal actualFeeValue;
    /**
     * 实还日期
     */
	@TableField("actual_repayment_date")
	@ApiModelProperty(required= true,value = "实还日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actualRepaymentDate;
    /**
     * 减免金额
     */
	@TableField("derate_amount")
	@ApiModelProperty(required= true,value = "减免金额")
	private BigDecimal derateAmount;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * [本期应还比例]
     */
	@TableField("plan_fee_rate")
	@ApiModelProperty(required= true,value = "[本期应还比例]")
	private BigDecimal planFeeRate;


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

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public Integer getAfterFeeType() {
		return afterFeeType;
	}

	public void setAfterFeeType(Integer afterFeeType) {
		this.afterFeeType = afterFeeType;
	}

	public BigDecimal getPlanFeeValue() {
		return planFeeValue;
	}

	public void setPlanFeeValue(BigDecimal planFeeValue) {
		this.planFeeValue = planFeeValue;
	}

	public Date getPlanRepaymentDate() {
		return planRepaymentDate;
	}

	public void setPlanRepaymentDate(Date planRepaymentDate) {
		this.planRepaymentDate = planRepaymentDate;
	}

	public BigDecimal getActualFeeValue() {
		return actualFeeValue;
	}

	public void setActualFeeValue(BigDecimal actualFeeValue) {
		this.actualFeeValue = actualFeeValue;
	}

	public Date getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Date actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
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

	public BigDecimal getPlanFeeRate() {
		return planFeeRate;
	}

	public void setPlanFeeRate(BigDecimal planFeeRate) {
		this.planFeeRate = planFeeRate;
	}


}
