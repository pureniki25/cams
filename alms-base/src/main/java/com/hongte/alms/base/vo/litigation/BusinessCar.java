package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * [车辆的详细信息表]
 * </p>
 *
 * @author huweiqian
 * @since 2018-03-01
 */
@Data
public class BusinessCar implements Serializable{

	private static final long serialVersionUID = 2248300875789582950L;
	/**
	 * 主键ID
	 */
	@ApiModelProperty(required = true, value = "主键ID")
	private String id;
	/**
	 * 车贷业务编号
	 */
	@ApiModelProperty(required = true, value = "车贷业务编号")
	private String carBusinessId;
	/**
	 * 所属人
	 */
	@ApiModelProperty(required = true, value = "所属人")
	private String ownerName;
	/**
	 * 车身颜色
	 */
	@ApiModelProperty(required = true, value = "车身颜色")
	private String carBodyColor;
	/**
	 * 车牌号
	 */
	@ApiModelProperty(required = true, value = "车牌号")
	private String licensePlateNumber;
	/**
	 * [汽车品牌]
	 */
	@ApiModelProperty(required = true, value = "[汽车品牌]")
	private String carBrand;
	/**
	 * 汽车型号
	 */
	@ApiModelProperty(required = true, value = "汽车型号")
	private String carModelNumber;
	/**
	 * 违章费用
	 */
	@ApiModelProperty(required = true, value = "违章费用")
	private String carViolations;
	/**
	 * 车架号
	 */
	@ApiModelProperty(required = true, value = "车架号")
	private String chassisNumber;
	/**
	 * 发动机号
	 */
	@ApiModelProperty(required = true, value = "发动机号")
	private String engineNumber;
	/**
	 * 评估价值
	 */
	@ApiModelProperty(required = true, value = "评估价值")
	private String carEvaluateMoney;
	/**
	 * 年审到期日
	 */
	@ApiModelProperty(required = true, value = "年审到期日")
	private String annualDueDate;
	/**
	 * 拖车时间
	 */
	@ApiModelProperty(required = true, value = "拖车时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dragDate;
	/**
	 * 车辆状态
	 */
	@ApiModelProperty(required = true, value = "车辆状态")
	private String status;
}
