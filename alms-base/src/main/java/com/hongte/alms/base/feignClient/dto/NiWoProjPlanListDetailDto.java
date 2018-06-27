package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author:陈泽圣
 * 你我金融  标当前期的
 * @date: 2018/6/13
 */
public class NiWoProjPlanListDetailDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private Integer period;//当前期数
     private BigDecimal principal;//当期应该还本金
     private BigDecimal interest;//当期应还利息
     private BigDecimal platformManageFee;//当期应还平台管理费
     private BigDecimal commissionGuaranteFee;//当期应还担保服务费
     private long refundDate;//当期应该还款日期 格式：1524016406846 毫秒时间戳
     private BigDecimal repaidPrincipal;//当前期已还本金
     private BigDecimal repaidInterest;//当前期已还利息	
     private BigDecimal repaidPlatformManageFee;//当前期已还平台管理费
     private BigDecimal repaidCommissionGuaranteFee;//当期已还平台管理费
     private BigDecimal totalPenalty;//当前期总罚息
     private BigDecimal repaidPenalty;//当前期已还罚息
     private BigDecimal shouldConsultingFee;//当前应还咨询服务费
     private BigDecimal repaidConsultingFee;//当前已还咨询服务费
     
	public BigDecimal getShouldConsultingFee() {
		return shouldConsultingFee;
	}
	public void setShouldConsultingFee(BigDecimal shouldConsultingFee) {
		this.shouldConsultingFee = shouldConsultingFee;
	}
	public BigDecimal getRepaidConsultingFee() {
		return repaidConsultingFee;
	}
	public void setRepaidConsultingFee(BigDecimal repaidConsultingFee) {
		this.repaidConsultingFee = repaidConsultingFee;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getPlatformManageFee() {
		return platformManageFee;
	}
	public void setPlatformManageFee(BigDecimal platformManageFee) {
		this.platformManageFee = platformManageFee;
	}
	public BigDecimal getCommissionGuaranteFee() {
		return commissionGuaranteFee;
	}
	public void setCommissionGuaranteFee(BigDecimal commissionGuaranteFee) {
		this.commissionGuaranteFee = commissionGuaranteFee;
	}
	public long getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(long refundDate) {
		this.refundDate = refundDate;
	}
	public BigDecimal getRepaidPrincipal() {
		return repaidPrincipal;
	}
	public void setRepaidPrincipal(BigDecimal repaidPrincipal) {
		this.repaidPrincipal = repaidPrincipal;
	}
	public BigDecimal getRepaidInterest() {
		return repaidInterest;
	}
	public void setRepaidInterest(BigDecimal repaidInterest) {
		this.repaidInterest = repaidInterest;
	}
	public BigDecimal getRepaidPlatformManageFee() {
		return repaidPlatformManageFee;
	}
	public void setRepaidPlatformManageFee(BigDecimal repaidPlatformManageFee) {
		this.repaidPlatformManageFee = repaidPlatformManageFee;
	}
	public BigDecimal getRepaidCommissionGuaranteFee() {
		return repaidCommissionGuaranteFee;
	}
	public void setRepaidCommissionGuaranteFee(BigDecimal repaidCommissionGuaranteFee) {
		this.repaidCommissionGuaranteFee = repaidCommissionGuaranteFee;
	}
	public BigDecimal getTotalPenalty() {
		return totalPenalty;
	}
	public void setTotalPenalty(BigDecimal totalPenalty) {
		this.totalPenalty = totalPenalty;
	}
	public BigDecimal getRepaidPenalty() {
		return repaidPenalty;
	}
	public void setRepaidPenalty(BigDecimal repaidPenalty) {
		this.repaidPenalty = repaidPenalty;
	}

     
     
   
}
