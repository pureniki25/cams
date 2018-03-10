package com.hongte.alms.base.vo.module.api;

import java.util.List;

public class RepayResultRespData {
	private String originalBusinessId;//主业务编号
	private String afterId;//还款期数
	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	private String planStatus; // 还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期
	private String dueDate;// 应还日期
	private String totalBorrowAmount;// 总计划应还金额(元)，不含滞纳金
	private String overdueAmount;// 总应还滞纳金(元)，每天零点由系统自动计算
	private String overdueDays;// 逾期天数，每天零点由系统自动计算
	private String currentStatus;// 当前还款状态，目前只有三种，分别为"还款中"，"逾期"，"已还款"
	private String repayFlag;// 已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结
	private String factRepayDate;// 客户实还日期
	private String financeComfirmDate;// 财务确认还款操作日期
	private String financeConfirmUser;// 财务还款确认人ID
	private String financeConfirmUserName;// 财务还款确认人名称
	private String confirmFlag;// 财务还款金额确认(1:已确认,0:未确认)
	private String autoWithholdingConfirmedDate;// 财务确认自动代扣日期
	private String autoWithholdingConfirmedUser;// 确认自动代扣的确认者ID
	private String accountantConfirmStatus;// 会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
	private String accountantConfirmUser;// 会计确认人ID
	private String accountantConfirmUserName;// 会计确认人姓名
	private String accountantConfirmDate;// 会计确认日期

	private List<RepayDetailResultRespData> repayResultDetailList;

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getTotalBorrowAmount() {
		return totalBorrowAmount;
	}

	public void setTotalBorrowAmount(String totalBorrowAmount) {
		this.totalBorrowAmount = totalBorrowAmount;
	}

	public String getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(String overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getRepayFlag() {
		return repayFlag;
	}

	public void setRepayFlag(String repayFlag) {
		this.repayFlag = repayFlag;
	}

	public String getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(String factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public String getFinanceComfirmDate() {
		return financeComfirmDate;
	}

	public void setFinanceComfirmDate(String financeComfirmDate) {
		this.financeComfirmDate = financeComfirmDate;
	}

	public String getFinanceConfirmUser() {
		return financeConfirmUser;
	}

	public void setFinanceConfirmUser(String financeConfirmUser) {
		this.financeConfirmUser = financeConfirmUser;
	}

	public String getFinanceConfirmUserName() {
		return financeConfirmUserName;
	}

	public void setFinanceConfirmUserName(String financeConfirmUserName) {
		this.financeConfirmUserName = financeConfirmUserName;
	}

	public String getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public String getAutoWithholdingConfirmedDate() {
		return autoWithholdingConfirmedDate;
	}

	public void setAutoWithholdingConfirmedDate(String autoWithholdingConfirmedDate) {
		this.autoWithholdingConfirmedDate = autoWithholdingConfirmedDate;
	}

	public String getAutoWithholdingConfirmedUser() {
		return autoWithholdingConfirmedUser;
	}

	public void setAutoWithholdingConfirmedUser(String autoWithholdingConfirmedUser) {
		this.autoWithholdingConfirmedUser = autoWithholdingConfirmedUser;
	}

	public String getAccountantConfirmStatus() {
		return accountantConfirmStatus;
	}

	public void setAccountantConfirmStatus(String accountantConfirmStatus) {
		this.accountantConfirmStatus = accountantConfirmStatus;
	}

	public String getAccountantConfirmUser() {
		return accountantConfirmUser;
	}

	public void setAccountantConfirmUser(String accountantConfirmUser) {
		this.accountantConfirmUser = accountantConfirmUser;
	}

	public String getAccountantConfirmUserName() {
		return accountantConfirmUserName;
	}

	public void setAccountantConfirmUserName(String accountantConfirmUserName) {
		this.accountantConfirmUserName = accountantConfirmUserName;
	}

	public String getAccountantConfirmDate() {
		return accountantConfirmDate;
	}

	public void setAccountantConfirmDate(String accountantConfirmDate) {
		this.accountantConfirmDate = accountantConfirmDate;
	}

	public List<RepayDetailResultRespData> getRepayResultDetailList() {
		return repayResultDetailList;
	}

	public void setRepayResultDetailList(List<RepayDetailResultRespData> repayResultDetailList) {
		this.repayResultDetailList = repayResultDetailList;
	}

}
