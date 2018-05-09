/**
 * 
 */
package com.hongte.alms.finance.req;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王继光
 * 2018年5月2日 上午10:40:20
 */
public class MoneyPoolReq {

	private String businessId ;
	private String afterId ;
	private String moneyPoolId;
	private int curPage ;
	private int pageSize ;
	/**
	 * 还款日期
	 */
	private String repayDate ;
	/**
	 * 还款金额
	 */
	private BigDecimal accountMoney ;
	/**
	 * 转入账号
	 */
	private String acceptBank ;
	
	/**
	 * 
	 */
	private String status ;
	/**
	 * @return the businessId
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * @return the afterId
	 */
	public String getAfterId() {
		return afterId;
	}
	/**
	 * @param afterId the afterId to set
	 */
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
	/**
	 * @return the moneyPoolId
	 */
	public String getMoneyPoolId() {
		return moneyPoolId;
	}
	/**
	 * @param moneyPoolId the moneyPoolId to set
	 */
	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}
	/**
	 * @return the repayDate
	 */
	public String getRepayDate() {
		return repayDate;
	}
	/**
	 * @param repayDate the repayDate to set
	 */
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	/**
	 * @return the accountMoney
	 */
	public BigDecimal getAccountMoney() {
		return accountMoney;
	}
	/**
	 * @param accountMoney the accountMoney to set
	 */
	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}
	/**
	 * @return the acceptBank
	 */
	public String getAcceptBank() {
		return acceptBank;
	}
	/**
	 * @param acceptBank the acceptBank to set
	 */
	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the curPage
	 */
	public int getCurPage() {
		return curPage;
	}
	/**
	 * @param curPage the curPage to set
	 */
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
