package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel(value = "移交诉讼信息查询接口返回车辆信息模型", description = "移交诉讼信息查询接口返回车辆信息模型")
@Data
@ToString
public class TransferLitigationCarDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 车主
	 */
	private String ownerName;
	/**
	 * 车牌号
	 */
	private String licensePlateNumber;
	/**
	 * 发动机号
	 */
	private String engineModel;
	/**
	 * 车辆识别码（车架号）
	 */
	private String vin;
	/**
	 * 汽车品牌 
	 */
	private String brandName;
	/**
	 * 品牌型号
	 */
	private String model;

}
