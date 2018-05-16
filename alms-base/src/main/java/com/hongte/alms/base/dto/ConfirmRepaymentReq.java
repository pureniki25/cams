/**
 * 
 */
package com.hongte.alms.base.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 王继光
 * 2018年5月14日 下午9:15:26
 */
public class ConfirmRepaymentReq {

	private String businessId ;
	private String afterId ;
	private BigDecimal offlineOverDue ;
	private BigDecimal onlineOverDue ;
	private BigDecimal surplusFund ;
	private List<String> mprIds ;
	private String remark ;
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getAfterId() {
		return afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
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
	public BigDecimal getSurplusFund() {
		return surplusFund;
	}
	public void setSurplusFund(BigDecimal surplusFund) {
		this.surplusFund = surplusFund;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the mprIds
	 */
	public List<String> getMprIds() {
		return mprIds;
	}
	/**
	 * @param mprIds the mprIds to set
	 */
	public void setMprIds(List<String> mprIds) {
		this.mprIds = mprIds;
	}
	
}
