package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.List;

public class ActualPaymentLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ActualPaymentSingleLogDTO> actualPaymentSingleLogDTOs;

	private String afterId;

	/**
	 * 实收总计
	 */
	private double receivedTotal;

	public List<ActualPaymentSingleLogDTO> getActualPaymentSingleLogDTOs() {
		return actualPaymentSingleLogDTOs;
	}

	public void setActualPaymentSingleLogDTOs(List<ActualPaymentSingleLogDTO> actualPaymentSingleLogDTOs) {
		this.actualPaymentSingleLogDTOs = actualPaymentSingleLogDTOs;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public double getReceivedTotal() {
		return receivedTotal;
	}

	public void setReceivedTotal(double receivedTotal) {
		this.receivedTotal = receivedTotal;
	}

	@Override
	public String toString() {
		return "ActualPaymentLogDTO [actualPaymentSingleLogDTOs=" + actualPaymentSingleLogDTOs + ", afterId=" + afterId
				+ ", receivedTotal=" + receivedTotal + "]";
	}

}
