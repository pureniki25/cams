package com.hongte.alms.platrepay.vo;

import java.io.Serializable;

/**
 * 代充值结果
 * @author 胡伟骞
 *
 */
public class AgencyRechargeResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 1 充值失败；2 充值成功；3 待付款
	 */
	private String status;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 错误消息
	 */
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "AgencyRechargeResult [status=" + status + ", orderId=" + orderId + ", message=" + message + "]";
	}

}
