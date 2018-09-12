package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel(value = "移交诉讼信息查询接口返回房产信息模型", description = "移交诉讼信息查询接口返回房产信息模型")
@Data
@ToString
public class TransferLitigationHouseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 房产权所属人
	 */
	private String houseName;
	/**
	 * 房产地址
	 */
	private String houseAddress;
	/**
	 * 房产证编号
	 */
	private String houseNo;
	/**
	 * 几抵
	 */
	private String pledgeTypeDetail;
	/**
	 * 房产权所属人身份证号码
	 */
	private String houseOwerIdentityCard;
	/**
	 * 房产权所属人联系地址
	 */
	private String houseOwerAddress;
	/**
	 * 房产权所属人联系电话
	 */
	private String houseOwerPhone;
}
