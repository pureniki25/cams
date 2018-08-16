/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.DateUtil;

/**
 * @author 王继光
 * 2018年5月29日 下午4:56:05
 */
public class RepaymentSettleListVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2141251720743615058L;
	private String afterId ;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date repayDate;
	private Integer period ;
	private Boolean isDefer ;
	private BigDecimal item10 ;
	private BigDecimal item20 ;
	private BigDecimal item30 ;
	private BigDecimal item50 ;
	private BigDecimal offlineOverDue ;
	private BigDecimal onlineOverDue ;
	private BigDecimal planAmount ;
	private BigDecimal lack ;
	private BigDecimal factAmount ;
	private BigDecimal penalty ;
	private String remark ;
	private String status ;
	public Integer getRepayYear() {
		if (repayDate==null) {
			return null;
		}
		return DateUtil.getYear(repayDate);
	}
	public Integer getRepayMonth() {
		if (repayDate==null) {
			return null;
		}
		return DateUtil.getMonth(repayDate);
	}
	public Integer getRepayDay() {
		if (repayDate==null) {
			return null;
		}
		return DateUtil.getDay(repayDate);
	}
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	public Date getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}
	public BigDecimal getItem10() {
		return item10;
	}
	public void setItem10(BigDecimal item10) {
		this.item10 = item10;
	}
	public BigDecimal getItem20() {
		return item20;
	}
	public void setItem20(BigDecimal item20) {
		this.item20 = item20;
	}
	public BigDecimal getItem30() {
		return item30;
	}
	public void setItem30(BigDecimal item30) {
		this.item30 = item30;
	}
	public BigDecimal getItem50() {
		return item50;
	}
	public void setItem50(BigDecimal item50) {
		this.item50 = item50;
	}
	public BigDecimal getOfflineOverDue() {
		return offlineOverDue;
	}
	public void setOfflineOverDue(BigDecimal offlineOverDue) {
		this.offlineOverDue = offlineOverDue;
	}
	public BigDecimal getOnlineOverDue() {
		return onlineOverDue;
	}
	public void setOnlineOverDue(BigDecimal onlineOverDue) {
		this.onlineOverDue = onlineOverDue;
	}
	public BigDecimal getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	public BigDecimal getLack() {
		return this.lack ;
	}
	public void setLack(BigDecimal lack) {
		this.lack = lack;
	}
	public BigDecimal getFactAmount() {
		return factAmount;
	}
	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}
	/**
	 * @return the isDefer
	 */
	public Boolean getIsDefer() {
		return isDefer;
	}
	/**
	 * @param isDefer the isDefer to set
	 */
	public void setIsDefer(Boolean isDefer) {
		this.isDefer = isDefer;
	}
	
	public boolean samePeriod(RepaymentSettleListVO vo) {
		if (!vo.getRepayYear().equals(getRepayYear())) {
			return false ;
		}
		if (!vo.getRepayMonth().equals(getRepayMonth())) {
			return false ;
		}
			
		return true;
	}
}
