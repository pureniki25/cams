/**
 * 
 */
package com.hongte.alms.base.dto;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author 王继光
 * 2018年6月6日 下午3:28:39
 */
public class MoneyPoolManagerReq {

	private int curPage;
	private int pageSize;
	/**
	 * 还款日期
	 */
	private String tradeDateStart;
	private String tradeDateEnd;
	private String tradeDate;
	private String updateDateStart;
	private String updateDateEnd;
	private String updateDate;
	private String createTimeStart;
	private String createTimeEnd;
	private String createTime;

	/**
	 * 交易类型
	 */
	private String tradeType;
	/**
	 * 转入账号
	 */
	private String acceptBank;

	/**
	 * 状态
	 */
	private String status;

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getTradeDateStart() {
		return tradeDateStart;
	}

	public void setTradeDateStart(String tradeDateStart) {
		this.tradeDateStart = tradeDateStart;
	}

	public String getTradeDateEnd() {
		return tradeDateEnd;
	}

	public void setTradeDateEnd(String tradeDateEnd) {
		this.tradeDateEnd = tradeDateEnd;
	}


	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAcceptBank() {
		return acceptBank;
	}

	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the updateDateStart
	 */
	public String getUpdateDateStart() {
		return updateDateStart;
	}

	/**
	 * @param updateDateStart the updateDateStart to set
	 */
	public void setUpdateDateStart(String updateDateStart) {
		this.updateDateStart = updateDateStart;
	}

	/**
	 * @return the updateDateEnd
	 */
	public String getUpdateDateEnd() {
		return updateDateEnd;
	}

	/**
	 * @param updateDateEnd the updateDateEnd to set
	 */
	public void setUpdateDateEnd(String updateDateEnd) {
		this.updateDateEnd = updateDateEnd;
	}

	/**
	 * @return the createTimeEnd
	 */
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	/**
	 * @param createTimeEnd the createTimeEnd to set
	 */
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	/**
	 * @return the createTimeStart
	 */
	public String getCreateTimeStart() {
		return createTimeStart;
	}

	/**
	 * @param createTimeStart the createTimeStart to set
	 */
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	/**
	 * @return the tradeDate
	 */
	public String getTradeDate() {
		if (getTradeDateStart().equals(getTradeDateEnd())) {
			return getTradeDateStart();
		}else {
			return null;
		}
	}

	/**
	 * @param tradeDate the tradeDate to set
	 */
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		if (getUpdateDateStart().equals(getUpdateDateEnd())) {
			return getUpdateDateStart();
		}else {
			return null;
		}
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		if (getCreateTimeStart().equals(getCreateTimeEnd())) {
			return getCreateTimeStart();
		}else {
			return null;
		}
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
