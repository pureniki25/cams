package com.hongte.alms.base.vo.litigation.house;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划信息
 * 
 * @author huweiqian
 * @since 2018.3.4
 */
public class HousePlanInfo implements Serializable {

	private static final long serialVersionUID = -4832188634896323365L;

	/**
	 * 还款计划序号
	 */
	private int repaymentNum;
	/**
	 * 已还本金
	 */
	private BigDecimal factPrincipal;
	/**
	 * 剩余本金
	 */
	private BigDecimal surplusPrincipal;
	/**
	 * 逾期天数
	 */
	private BigDecimal overdueDays;
	/**
	 * 利息交至
	 */
	private Date dueDate;

	public int getRepaymentNum() {
		return repaymentNum;
	}

	public void setRepaymentNum(int repaymentNum) {
		this.repaymentNum = repaymentNum;
	}

	public BigDecimal getFactPrincipal() {
		return factPrincipal;
	}

	public void setFactPrincipal(BigDecimal factPrincipal) {
		this.factPrincipal = factPrincipal;
	}

	public BigDecimal getSurplusPrincipal() {
		return surplusPrincipal;
	}

	public void setSurplusPrincipal(BigDecimal surplusPrincipal) {
		this.surplusPrincipal = surplusPrincipal;
	}

	public BigDecimal getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(BigDecimal overdueDays) {
		this.overdueDays = overdueDays;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((factPrincipal == null) ? 0 : factPrincipal.hashCode());
		result = prime * result + ((overdueDays == null) ? 0 : overdueDays.hashCode());
		result = prime * result + repaymentNum;
		result = prime * result + ((surplusPrincipal == null) ? 0 : surplusPrincipal.hashCode());
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
		HousePlanInfo other = (HousePlanInfo) obj;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (factPrincipal == null) {
			if (other.factPrincipal != null)
				return false;
		} else if (!factPrincipal.equals(other.factPrincipal))
			return false;
		if (overdueDays == null) {
			if (other.overdueDays != null)
				return false;
		} else if (!overdueDays.equals(other.overdueDays))
			return false;
		if (repaymentNum != other.repaymentNum)
			return false;
		if (surplusPrincipal == null) {
			if (other.surplusPrincipal != null)
				return false;
		} else if (!surplusPrincipal.equals(other.surplusPrincipal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HousePlanInfo [repaymentNum=" + repaymentNum + ", factPrincipal=" + factPrincipal
				+ ", surplusPrincipal=" + surplusPrincipal + ", overdueDays=" + overdueDays + ", dueDate=" + dueDate
				+ "]";
	}

}
