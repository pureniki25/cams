package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 业务额外汇率信息表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-11
 */
@ApiModel
@TableName("tb_baisc_biz_ext_rate")
public class BaiscBizExtRate extends Model<BaiscBizExtRate> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required= true,value = "主键ID")
	private String id;
    /**
     * 业务ID
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务ID")
	private String businessId;
    /**
     * 费率类型
     */
	@TableField("rate_type")
	@ApiModelProperty(required= true,value = "费率类型")
	private Integer rateType;
    /**
     * 费率名称
     */
	@TableField("rate_name")
	@ApiModelProperty(required= true,value = "费率名称")
	private String rateName;
    /**
     * 费率值
     */
	@TableField("rate_value")
	@ApiModelProperty(required= true,value = "费率值")
	private BigDecimal rateValue;
    /**
     * 费率类型，1：年利率，2：月利率，3：日利率
     */
	@TableField("rate_unit")
	@ApiModelProperty(required= true,value = "费率类型，1：年利率，2：月利率，3：日利率")
	private Integer rateUnit;
    /**
     * 资产端费用项ID
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "资产端费用项ID")
	private String feeId;
    /**
     * 资产端费用项名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "资产端费用项名称")
	private String feeName;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public BigDecimal getRateValue() {
		return rateValue;
	}

	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}

	public Integer getRateUnit() {
		return rateUnit;
	}

	public void setRateUnit(Integer rateUnit) {
		this.rateUnit = rateUnit;
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
		return this.id;
	}

	@Override
	public String toString() {
		return "BaiscBizExtRate{" +
			", id=" + id +
			", businessId=" + businessId +
			", rateType=" + rateType +
			", rateName=" + rateName +
			", rateValue=" + rateValue +
			", rateUnit=" + rateUnit +
			", feeId=" + feeId +
			", feeName=" + feeName +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
