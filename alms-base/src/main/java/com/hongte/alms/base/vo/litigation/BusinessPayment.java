package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * [车辆的详细信息表]
 * </p>
 *
 * @author huweiqian
 * @since 2018-03-01
 */

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

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessAfterId() {
		return businessAfterId;
	}

	public void setBusinessAfterId(String businessAfterId) {
		this.businessAfterId = businessAfterId;
	}

	public BigDecimal getOddcorpus() {
		return oddcorpus;
	}

	public void setOddcorpus(BigDecimal oddcorpus) {
		this.oddcorpus = oddcorpus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessAfterId == null) ? 0 : businessAfterId.hashCode());
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
		result = prime * result + ((oddcorpus == null) ? 0 : oddcorpus.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessPayment other = (BusinessPayment) obj;
		if (businessAfterId == null) {
			if (other.businessAfterId != null)
				return false;
		} else if (!businessAfterId.equals(other.businessAfterId))
			return false;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		if (oddcorpus == null) {
			if (other.oddcorpus != null)
				return false;
		} else if (!oddcorpus.equals(other.oddcorpus))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessPayment [paymentId=" + paymentId + ", businessId=" + businessId + ", businessAfterId="
				+ businessAfterId + ", oddcorpus=" + oddcorpus + "]";
	}

}
