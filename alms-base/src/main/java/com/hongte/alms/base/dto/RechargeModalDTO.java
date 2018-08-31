package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RechargeModalDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 团贷分配，商户唯一号(测试，生产不一样)
	 */
	private String oIdPartner;
	/**
	 * 代充值账户userID
	 */
	private String rechargeUserId;
	/**
	 * 1：网关、2：快捷、3：代充值
	 */
	private String chargeType;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 由资产端生成，作为后续查询的一个标识
	 */
	private String cmOrderNo;
	/**
	 * 用户IP
	 */
	private String clientIp;
	/**
	 * 金额，不能少于100
	 */
	private BigDecimal amount;
	/**
	 * 机构编号
	 */
	private Integer orgType;

}
