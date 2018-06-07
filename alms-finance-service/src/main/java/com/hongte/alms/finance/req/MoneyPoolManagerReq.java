/**
 * 
 */
package com.hongte.alms.finance.req;

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
	private String udpateDateStart;
	private String udpateDateEnd;

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

	public String getUdpateDateStart() {
		return udpateDateStart;
	}

	public void setUdpateDateStart(String udpateDateStart) {
		this.udpateDateStart = udpateDateStart;
	}

	public String getUdpateDateEnd() {
		return udpateDateEnd;
	}

	public void setUdpateDateEnd(String udpateDateEnd) {
		this.udpateDateEnd = udpateDateEnd;
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
}
