/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 王继光
 * 2018年5月14日 下午5:56:36
 */
public class CurrPeriodRepaymentInfoVO {
	/*
	'应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，
	60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收',
	 */
	/**
	 * 还款日期
	 */
	@JsonFormat( pattern = "yyyy-MM-dd" ,timezone="GMT+8")
	private Date repayDate ;
	/**
	 * 应还本金
	 */
	private BigDecimal item10 = new BigDecimal(0) ;
	/**
	 * 应还利息
	 */
	private BigDecimal item20 = new BigDecimal(0);
	/**
	 * 月收服务费
	 */
	private BigDecimal item30 = new BigDecimal(0);
	/**
	 * 月收平台费
	 */
	private BigDecimal item50 = new BigDecimal(0);
	/**
	 * 线下逾期费
	 */
	private BigDecimal offlineOverDue = new BigDecimal(0);
	/**
	 * 线上逾期费
	 */
	private BigDecimal onlineOverDue = new BigDecimal(0);
	/**
	 * 逾期天数
	 */
	private Integer overDays = 0;
	private BigDecimal derate = new BigDecimal(0);
	/**
	 * 小计(不含违约金)
	 */
	private BigDecimal subTotal = new BigDecimal(0);
	/**
	 * 合计(含违约金)
	 */
	private BigDecimal total = new BigDecimal(0);
	
	private List<JSONObject> derateDetails  ;
	
	/**
	 * 已匹配银行流水的实还日期,若无匹配,则只有一个当前日期<br>
	 * 若多个流水是同一天,则选用其中一个
	 */
	private Set<String> moneyPoolRepayDates ;
	
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
	public Integer getOverDays() {
		return overDays;
	}
	public void setOverDays(Integer overDays) {
		this.overDays = overDays;
	}
	public BigDecimal getDerate() {
		return derate;
	}
	public void setDerate(BigDecimal derate) {
		this.derate = derate;
	}
	public BigDecimal getSubtotal() {
		subTotal = new BigDecimal(0);
		subTotal=subTotal.add(getItem10()).add(getItem20()).add(getItem30()).add(getItem50()) ;
		return subTotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subTotal = subtotal;
	}
	public BigDecimal getTotal() {
		total = new BigDecimal(0);
		total=total.add(getItem10()).add(getItem20()).add(getItem30()).add(getItem50())
				.add(getOfflineOverDue()).add(getOnlineOverDue()).subtract(getDerate());
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<JSONObject> getDerateDetails() {
		return derateDetails;
	}
	public void setDerateDetails(List<JSONObject> derateDetails) {
		this.derateDetails = derateDetails;
	}
	public Set<String> getMoneyPoolRepayDates() {
		return moneyPoolRepayDates;
	}
	public void setMoneyPoolRepayDates(Set<String> moneyPoolRepayDates) {
		this.moneyPoolRepayDates = moneyPoolRepayDates;
	}
}
