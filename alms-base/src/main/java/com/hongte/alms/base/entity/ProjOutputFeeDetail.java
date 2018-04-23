package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 标的出款申请费用明细期限范围明细表
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-23
 */
@ApiModel("标的出款申请费用明细期限范围明细表")
@TableName("tb_proj_output_fee_detail")
public class ProjOutputFeeDetail extends Model<ProjOutputFeeDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "自增ID")
	private Integer id;
    /**
     * 上标编号
     */
	@TableField("proj_id")
	@ApiModelProperty(required= true,value = "上标编号")
	private String projId;
    /**
     * 最小期限范围
     */
	@TableField("fee_term_range_min")
	@ApiModelProperty(required= true,value = "最小期限范围")
	private Integer feeTermRangeMin;
    /**
     * 最大期限范围
     */
	@TableField("fee_term_range_max")
	@ApiModelProperty(required= true,value = "最大期限范围")
	private Integer feeTermRangeMax;
    /**
     * 费用项ID
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "费用项ID")
	private String feeId;
    /**
     * 费用名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "费用名称")
	private String feeName;
    /**
     * 本期金额
     */
	@TableField("fee_value")
	@ApiModelProperty(required= true,value = "本期金额")
	private BigDecimal feeValue;
    /**
     * 本期比例
     */
	@TableField("fee_rate")
	@ApiModelProperty(required= true,value = "本期比例")
	private BigDecimal feeRate;
    /**
     * 费用收取方式，1为按比例，2为按金额
     */
	@TableField("fee_charging_type")
	@ApiModelProperty(required= true,value = "费用收取方式，1为按比例，2为按金额")
	private Integer feeChargingType;
    /**
     * 利率,1为年利率，2为月利率，3为日利率
     */
	@TableField("interest_rate")
	@ApiModelProperty(required= true,value = "利率,1为年利率，2为月利率，3为日利率")
	private Integer interestRate;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public Integer getFeeTermRangeMin() {
		return feeTermRangeMin;
	}

	public void setFeeTermRangeMin(Integer feeTermRangeMin) {
		this.feeTermRangeMin = feeTermRangeMin;
	}

	public Integer getFeeTermRangeMax() {
		return feeTermRangeMax;
	}

	public void setFeeTermRangeMax(Integer feeTermRangeMax) {
		this.feeTermRangeMax = feeTermRangeMax;
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

	public BigDecimal getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
	}

	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	public Integer getFeeChargingType() {
		return feeChargingType;
	}

	public void setFeeChargingType(Integer feeChargingType) {
		this.feeChargingType = feeChargingType;
	}

	public Integer getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Integer interestRate) {
		this.interestRate = interestRate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjOutputFeeDetail{" +
			", id=" + id +
			", projId=" + projId +
			", feeTermRangeMin=" + feeTermRangeMin +
			", feeTermRangeMax=" + feeTermRangeMax +
			", feeId=" + feeId +
			", feeName=" + feeName +
			", feeValue=" + feeValue +
			", feeRate=" + feeRate +
			", feeChargingType=" + feeChargingType +
			", interestRate=" + interestRate +
			"}";
	}
}
