package com.hongte.alms.platrepay.vo;

import java.io.Serializable;

import com.hongte.alms.platrepay.dto.TdGuaranteePaymentDTO;

public class TdGuaranteePaymentVO extends TdGuaranteePaymentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private int period;

	private String status;

	private String addDate;

	private double total;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "TdGuaranteePaymentVO [period=" + period + ", status=" + status + ", addDate=" + addDate + ", total="
				+ total + "]";
	}

}
