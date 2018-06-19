/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 王继光
 * 2018年5月28日 下午4:03:04
 */
public class ConfirmWithholdListVO {
	private String afterId ;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date dueDate;
	private BigDecimal item10 ;
	private BigDecimal item20 ;
	private BigDecimal item30 ;
	private BigDecimal item50 ;
	private BigDecimal total ;
	private String status ;
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
