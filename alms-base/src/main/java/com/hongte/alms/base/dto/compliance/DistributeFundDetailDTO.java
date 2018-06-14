package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;

/**
 * 资金分发详细信息参数DTO
 * @author 胡伟骞
 *
 */
public class DistributeFundDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单唯一标识 必须为guid转化成的字符串
	 */
	private String requestNo;
	/**
	 * 团贷网用户唯一编号 必须为guid转化成的字符串
	 */
	private String userId;
	/**
	 * 保留两位小数 单位（元）
	 */
	private double amount;

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "DistributeFundDetailDTO [requestNo=" + requestNo + ", userId=" + userId + ", amount=" + amount + "]";
	}

}
