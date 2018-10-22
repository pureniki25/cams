package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * [车辆的详细信息表]
 * </p>
 *
 * @author huweiqian
 * @since 2018-03-01
 */

@Data
@ToString
public class BusinessPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 利息还款情况
	 */
	@ApiModelProperty(required = true, value = "利息还款情况")
	private String paymentId;
	/**
	 * 业务编码
	 */
	@ApiModelProperty(required = true, value = "业务编码")
	private String businessId;
	/**
	 * 当前还款期数，关联小贷系统 tb_car_business_after
	 */
	@ApiModelProperty(required = true, value = "当前还款期数，关联小贷系统 tb_car_business_after")
	private String businessAfterId;
	/**
	 * 剩余本金
	 */
	@ApiModelProperty(required = true, value = "剩余本金")
	private BigDecimal oddcorpus;
	/**
	 * 应还日期
	 */
	@ApiModelProperty(required = true, value = "应还日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date planRepayDate;
	/**
	 * 实还日期
	 */
	@ApiModelProperty(required = true, value = "实还日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date factRepayDate;
}
