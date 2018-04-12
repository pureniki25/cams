package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-10
 */
@ApiModel
@TableName("tb_car_business_after")
public class CarBusinessAfter extends Model<CarBusinessAfter> {

    private static final long serialVersionUID = 1L;

    @TableId("car_business_id")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessId;
	@TableField("car_business_after_id")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessAfterId;
	@ApiModelProperty(required= true,value = "")
	private String paratype;
	@TableField("Customer_Name")
	@ApiModelProperty(required= true,value = "")
	private String CustomerName;
	@TableField("operator_name")
	@ApiModelProperty(required= true,value = "")
	private String operatorName;
	@TableField("operator_dept")
	@ApiModelProperty(required= true,value = "")
	private String operatorDept;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
	@TableField("modify_time")
	@ApiModelProperty(required= true,value = "")
	private Date modifyTime;
	@TableField("is_delete")
	@ApiModelProperty(required= true,value = "")
	private Boolean isDelete;
	@TableField("repayment_type")
	@ApiModelProperty(required= true,value = "")
	private String repaymentType;
	@TableField("borrow_money")
	@ApiModelProperty(required= true,value = "")
	private String borrowMoney;
	@ApiModelProperty(required= true,value = "")
	private String oddcorpus;
	@TableField("installment_num_date")
	@ApiModelProperty(required= true,value = "")
	private String installmentNumDate;
	@TableField("installment_num")
	@ApiModelProperty(required= true,value = "")
	private String installmentNum;
    /**
     * [本期应还本金]
     */
	@TableField("current_Principa")
	@ApiModelProperty(required= true,value = "[本期应还本金]")
	private BigDecimal currentPrincipa;
	@TableField("current_accrual")
	@ApiModelProperty(required= true,value = "")
	private String currentAccrual;
	@TableField("punitive_rate_should")
	@ApiModelProperty(required= true,value = "")
	private String punitiveRateShould;
	@TableField("punitive_rate_fact")
	@ApiModelProperty(required= true,value = "")
	private String punitiveRateFact;
	@ApiModelProperty(required= true,value = "")
	private String overdueDays;
	@TableField("borrow_date")
	@ApiModelProperty(required= true,value = "")
	private Date borrowDate;
	@TableField("car_business_after_type")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessAfterType;
	@TableField("car_business_after_defer")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessAfterDefer;
	@TableField("is_defer")
	@ApiModelProperty(required= true,value = "")
	private Boolean isDefer;
	@TableField("car_business_defer_id")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessDeferId;
	@TableField("other_money")
	@ApiModelProperty(required= true,value = "")
	private String otherMoney;
	@TableField("fact_money")
	@ApiModelProperty(required= true,value = "")
	private String factMoney;
	@TableField("lawyer_money")
	@ApiModelProperty(required= true,value = "")
	private String lawyerMoney;
	@ApiModelProperty(required= true,value = "")
	private Date createdate;
	@ApiModelProperty(required= true,value = "")
	private Date updateDate;
	@TableField("is_collection")
	@ApiModelProperty(required= true,value = "")
	private Boolean isCollection;
	@TableField("repayed_flag")
	@ApiModelProperty(required= true,value = "")
	private Integer repayedFlag;
	@TableField("reserve_2")
	@ApiModelProperty(required= true,value = "")
	private String reserve2;
	@TableField("reserve_3")
	@ApiModelProperty(required= true,value = "")
	private String reserve3;
	@TableField("reserve_4")
	@ApiModelProperty(required= true,value = "")
	private String reserve4;
	@TableField("reserve_5")
	@ApiModelProperty(required= true,value = "")
	private String reserve5;
	@ApiModelProperty(required= true,value = "")
	private String ModifyUser;
	@ApiModelProperty(required= true,value = "")
	private String workflowstatus;
	@TableField("reserve_6")
	@ApiModelProperty(required= true,value = "")
	private String reserve6;
	@TableField("reserve_7")
	@ApiModelProperty(required= true,value = "")
	private String reserve7;
	@TableField("reserve_8")
	@ApiModelProperty(required= true,value = "")
	private String reserve8;
	@TableField("reserve_9")
	@ApiModelProperty(required= true,value = "")
	private String reserve9;
	@TableField("reserve_10")
	@ApiModelProperty(required= true,value = "")
	private String reserve10;
	@ApiModelProperty(required= true,value = "")
	private String TrackRecord;
	@ApiModelProperty(required= true,value = "")
	private Date collectionDate;
	@TableField("bad_debt_mark")
	@ApiModelProperty(required= true,value = "")
	private String badDebtMark;
    /**
     * [结清备注]
     */
	@TableField("settle_mark")
	@ApiModelProperty(required= true,value = "[结清备注]")
	private String settleMark;
	@TableField("fact_replayMoney")
	@ApiModelProperty(required= true,value = "")
	private String factReplayMoney;
	@TableField("fatct_replayDate")
	@ApiModelProperty(required= true,value = "")
	private Date fatctReplayDate;
	@TableField("fact_principa")
	@ApiModelProperty(required= true,value = "")
	private String factPrincipa;
	@TableField("fact_accrual")
	@ApiModelProperty(required= true,value = "")
	private String factAccrual;
	@TableField("overdue_money")
	@ApiModelProperty(required= true,value = "")
	private String overdueMoney;
    /**
     * 违约金额
     */
	@TableField("current_Breach")
	@ApiModelProperty(required= true,value = "违约金额")
	private String currentBreach;
	@TableField("repay_service")
	@ApiModelProperty(required= true,value = "")
	private BigDecimal repayService;
	@TableField("fact_service")
	@ApiModelProperty(required= true,value = "")
	private BigDecimal factService;
	@ApiModelProperty(required= true,value = "")
	private String remark;
	@TableField("payment_type")
	@ApiModelProperty(required= true,value = "")
	private String paymentType;
    /**
     * 计划其他费用
     */
	@TableField("current_other_money")
	@ApiModelProperty(required= true,value = "计划其他费用")
	private BigDecimal currentOtherMoney;
    /**
     * 财务还款金额确认(1:已确认,0:未确认)
     */
	@TableField("confirm_flag")
	@ApiModelProperty(required= true,value = "财务还款金额确认(1:已确认,0:未确认)")
	private Integer confirmFlag;
    /**
     * [催收人]
     */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "[催收人]")
	private String collectionUser;
    /**
     * [电催分配备注]
     */
	@TableField("collection_remark")
	@ApiModelProperty(required= true,value = "[电催分配备注]")
	private String collectionRemark;
    /**
     * [出款编号]
     */
	@TableField("out_id")
	@ApiModelProperty(required= true,value = "[出款编号]")
	private Integer outId;
    /**
     * [是否亏损结清 空或者0：不是亏损结清 1:是亏损结清]
     */
	@TableField("is_loss_settle")
	@ApiModelProperty(required= true,value = "[是否亏损结清 空或者0：不是亏损结清 1:是亏损结清]")
	private Integer isLossSettle;
    /**
     * [财务还款确认日期]
     */
	@TableField("finance_confirmed_date")
	@ApiModelProperty(required= true,value = "[财务还款确认日期]")
	private Date financeConfirmedDate;
    /**
     * [财务还款确认人ID]
     */
	@TableField("finance_confirmed_user")
	@ApiModelProperty(required= true,value = "[财务还款确认人ID]")
	private String financeConfirmedUser;
	@TableField("auto_withholding_confirmed_date")
	@ApiModelProperty(required= true,value = "")
	private Date autoWithholdingConfirmedDate;
	@TableField("auto_withholding_confirmed_user")
	@ApiModelProperty(required= true,value = "")
	private String autoWithholdingConfirmedUser;
    /**
     * [财务还款确认的时候选择的还款银行]
     */
	@TableField("finance_bank_id")
	@ApiModelProperty(required= true,value = "[财务还款确认的时候选择的还款银行]")
	private Integer financeBankId;
    /**
     * [会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核]
     */
	@TableField("accountant_confirm_status")
	@ApiModelProperty(required= true,value = "[会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核]")
	private Integer accountantConfirmStatus;
    /**
     * [会计确认人]
     */
	@TableField("accountant_confirm_user")
	@ApiModelProperty(required= true,value = "[会计确认人]")
	private String accountantConfirmUser;
    /**
     * [会计确认日期]
     */
	@TableField("accountant_confirm_date")
	@ApiModelProperty(required= true,value = "[会计确认日期]")
	private Date accountantConfirmDate;
    /**
     * [null或0:资金未分发，1:所有标资金分发成功，2：资金分发处理中，3：所有标资金分发失败，4：资金部分标分发成功]
     */
	@TableField("tuandai_distribute_fund_status")
	@ApiModelProperty(required= true,value = "[null或0:资金未分发，1:所有标资金分发成功，2：资金分发处理中，3：所有标资金分发失败，4：资金部分标分发成功]")
	private Integer tuandaiDistributeFundStatus;
    /**
     * [平台还款状态：未还款，已代偿，已还款]
     */
	@TableField("issue_after_type")
	@ApiModelProperty(required= true,value = "[平台还款状态：未还款，已代偿，已还款]")
	private String issueAfterType;
    /**
     * [null或0：未执行垫付操作，0:本期不需要还垫付，1:本期平台垫付已结清，2：本期平台垫付未结清，3：本期不需要还垫付]
     */
	@TableField("tuandai_advance_status")
	@ApiModelProperty(required= true,value = "[null或0：未执行垫付操作，0:本期不需要还垫付，1:本期平台垫付已结清，2：本期平台垫付未结清，3：本期不需要还垫付]")
	private Integer tuandaiAdvanceStatus;
    /**
     * [null或0：未执行分润操作，0:本期不需要还分润，1:本期分润已还清，2：本期分润未结清，3：本期不需要还分润]
     */
	@TableField("tuandai_profit_status")
	@ApiModelProperty(required= true,value = "[null或0：未执行分润操作，0:本期不需要还分润，1:本期分润已还清，2：本期分润未结清，3：本期不需要还分润]")
	private Integer tuandaiProfitStatus;
    /**
     * [资金分发备注]
     */
	@TableField("tuandai_distribute_fund_remark")
	@ApiModelProperty(required= true,value = "[资金分发备注]")
	private String tuandaiDistributeFundRemark;
    /**
     * 还款计划guid
     */
	@TableField("business_after_guid")
	@ApiModelProperty(required= true,value = "还款计划guid")
	private String businessAfterGuid;
    /**
     * [贷后跟踪状态:电催、催收、诉讼 ]
     */
	@TableField("tracking_after_type")
	@ApiModelProperty(required= true,value = "[贷后跟踪状态:电催、催收、诉讼 ]")
	private String trackingAfterType;
    /**
     * 是否法务退回 1:是 0或null：不是
     */
	@TableField("legal_return_status")
	@ApiModelProperty(required= true,value = "是否法务退回 1:是 0或null：不是")
	private Integer legalReturnStatus;


	public String getCarBusinessId() {
		return carBusinessId;
	}

	public void setCarBusinessId(String carBusinessId) {
		this.carBusinessId = carBusinessId;
	}

	public String getCarBusinessAfterId() {
		return carBusinessAfterId;
	}

	public void setCarBusinessAfterId(String carBusinessAfterId) {
		this.carBusinessAfterId = carBusinessAfterId;
	}

	public String getParatype() {
		return paratype;
	}

	public void setParatype(String paratype) {
		this.paratype = paratype;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorDept() {
		return operatorDept;
	}

	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Boolean getDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(String borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public String getOddcorpus() {
		return oddcorpus;
	}

	public void setOddcorpus(String oddcorpus) {
		this.oddcorpus = oddcorpus;
	}

	public String getInstallmentNumDate() {
		return installmentNumDate;
	}

	public void setInstallmentNumDate(String installmentNumDate) {
		this.installmentNumDate = installmentNumDate;
	}

	public String getInstallmentNum() {
		return installmentNum;
	}

	public void setInstallmentNum(String installmentNum) {
		this.installmentNum = installmentNum;
	}

	public BigDecimal getCurrentPrincipa() {
		return currentPrincipa;
	}

	public void setCurrentPrincipa(BigDecimal currentPrincipa) {
		this.currentPrincipa = currentPrincipa;
	}

	public String getCurrentAccrual() {
		return currentAccrual;
	}

	public void setCurrentAccrual(String currentAccrual) {
		this.currentAccrual = currentAccrual;
	}

	public String getPunitiveRateShould() {
		return punitiveRateShould;
	}

	public void setPunitiveRateShould(String punitiveRateShould) {
		this.punitiveRateShould = punitiveRateShould;
	}

	public String getPunitiveRateFact() {
		return punitiveRateFact;
	}

	public void setPunitiveRateFact(String punitiveRateFact) {
		this.punitiveRateFact = punitiveRateFact;
	}

	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getCarBusinessAfterType() {
		return carBusinessAfterType;
	}

	public void setCarBusinessAfterType(String carBusinessAfterType) {
		this.carBusinessAfterType = carBusinessAfterType;
	}

	public String getCarBusinessAfterDefer() {
		return carBusinessAfterDefer;
	}

	public void setCarBusinessAfterDefer(String carBusinessAfterDefer) {
		this.carBusinessAfterDefer = carBusinessAfterDefer;
	}

	public Boolean getDefer() {
		return isDefer;
	}

	public void setDefer(Boolean isDefer) {
		this.isDefer = isDefer;
	}

	public String getCarBusinessDeferId() {
		return carBusinessDeferId;
	}

	public void setCarBusinessDeferId(String carBusinessDeferId) {
		this.carBusinessDeferId = carBusinessDeferId;
	}

	public String getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(String otherMoney) {
		this.otherMoney = otherMoney;
	}

	public String getFactMoney() {
		return factMoney;
	}

	public void setFactMoney(String factMoney) {
		this.factMoney = factMoney;
	}

	public String getLawyerMoney() {
		return lawyerMoney;
	}

	public void setLawyerMoney(String lawyerMoney) {
		this.lawyerMoney = lawyerMoney;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Boolean getCollection() {
		return isCollection;
	}

	public void setCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Integer getRepayedFlag() {
		return repayedFlag;
	}

	public void setRepayedFlag(Integer repayedFlag) {
		this.repayedFlag = repayedFlag;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getModifyUser() {
		return ModifyUser;
	}

	public void setModifyUser(String ModifyUser) {
		this.ModifyUser = ModifyUser;
	}

	public String getWorkflowstatus() {
		return workflowstatus;
	}

	public void setWorkflowstatus(String workflowstatus) {
		this.workflowstatus = workflowstatus;
	}

	public String getReserve6() {
		return reserve6;
	}

	public void setReserve6(String reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve7() {
		return reserve7;
	}

	public void setReserve7(String reserve7) {
		this.reserve7 = reserve7;
	}

	public String getReserve8() {
		return reserve8;
	}

	public void setReserve8(String reserve8) {
		this.reserve8 = reserve8;
	}

	public String getReserve9() {
		return reserve9;
	}

	public void setReserve9(String reserve9) {
		this.reserve9 = reserve9;
	}

	public String getReserve10() {
		return reserve10;
	}

	public void setReserve10(String reserve10) {
		this.reserve10 = reserve10;
	}

	public String getTrackRecord() {
		return TrackRecord;
	}

	public void setTrackRecord(String TrackRecord) {
		this.TrackRecord = TrackRecord;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public String getBadDebtMark() {
		return badDebtMark;
	}

	public void setBadDebtMark(String badDebtMark) {
		this.badDebtMark = badDebtMark;
	}

	public String getSettleMark() {
		return settleMark;
	}

	public void setSettleMark(String settleMark) {
		this.settleMark = settleMark;
	}

	public String getFactReplayMoney() {
		return factReplayMoney;
	}

	public void setFactReplayMoney(String factReplayMoney) {
		this.factReplayMoney = factReplayMoney;
	}

	public Date getFatctReplayDate() {
		return fatctReplayDate;
	}

	public void setFatctReplayDate(Date fatctReplayDate) {
		this.fatctReplayDate = fatctReplayDate;
	}

	public String getFactPrincipa() {
		return factPrincipa;
	}

	public void setFactPrincipa(String factPrincipa) {
		this.factPrincipa = factPrincipa;
	}

	public String getFactAccrual() {
		return factAccrual;
	}

	public void setFactAccrual(String factAccrual) {
		this.factAccrual = factAccrual;
	}

	public String getOverdueMoney() {
		return overdueMoney;
	}

	public void setOverdueMoney(String overdueMoney) {
		this.overdueMoney = overdueMoney;
	}

	public String getCurrentBreach() {
		return currentBreach;
	}

	public void setCurrentBreach(String currentBreach) {
		this.currentBreach = currentBreach;
	}

	public BigDecimal getRepayService() {
		return repayService;
	}

	public void setRepayService(BigDecimal repayService) {
		this.repayService = repayService;
	}

	public BigDecimal getFactService() {
		return factService;
	}

	public void setFactService(BigDecimal factService) {
		this.factService = factService;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getCurrentOtherMoney() {
		return currentOtherMoney;
	}

	public void setCurrentOtherMoney(BigDecimal currentOtherMoney) {
		this.currentOtherMoney = currentOtherMoney;
	}

	public Integer getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
	}

	public String getCollectionRemark() {
		return collectionRemark;
	}

	public void setCollectionRemark(String collectionRemark) {
		this.collectionRemark = collectionRemark;
	}

	public Integer getOutId() {
		return outId;
	}

	public void setOutId(Integer outId) {
		this.outId = outId;
	}

	public Integer getIsLossSettle() {
		return isLossSettle;
	}

	public void setIsLossSettle(Integer isLossSettle) {
		this.isLossSettle = isLossSettle;
	}

	public Date getFinanceConfirmedDate() {
		return financeConfirmedDate;
	}

	public void setFinanceConfirmedDate(Date financeConfirmedDate) {
		this.financeConfirmedDate = financeConfirmedDate;
	}

	public String getFinanceConfirmedUser() {
		return financeConfirmedUser;
	}

	public void setFinanceConfirmedUser(String financeConfirmedUser) {
		this.financeConfirmedUser = financeConfirmedUser;
	}

	public Date getAutoWithholdingConfirmedDate() {
		return autoWithholdingConfirmedDate;
	}

	public void setAutoWithholdingConfirmedDate(Date autoWithholdingConfirmedDate) {
		this.autoWithholdingConfirmedDate = autoWithholdingConfirmedDate;
	}

	public String getAutoWithholdingConfirmedUser() {
		return autoWithholdingConfirmedUser;
	}

	public void setAutoWithholdingConfirmedUser(String autoWithholdingConfirmedUser) {
		this.autoWithholdingConfirmedUser = autoWithholdingConfirmedUser;
	}

	public Integer getFinanceBankId() {
		return financeBankId;
	}

	public void setFinanceBankId(Integer financeBankId) {
		this.financeBankId = financeBankId;
	}

	public Integer getAccountantConfirmStatus() {
		return accountantConfirmStatus;
	}

	public void setAccountantConfirmStatus(Integer accountantConfirmStatus) {
		this.accountantConfirmStatus = accountantConfirmStatus;
	}

	public String getAccountantConfirmUser() {
		return accountantConfirmUser;
	}

	public void setAccountantConfirmUser(String accountantConfirmUser) {
		this.accountantConfirmUser = accountantConfirmUser;
	}

	public Date getAccountantConfirmDate() {
		return accountantConfirmDate;
	}

	public void setAccountantConfirmDate(Date accountantConfirmDate) {
		this.accountantConfirmDate = accountantConfirmDate;
	}

	public Integer getTuandaiDistributeFundStatus() {
		return tuandaiDistributeFundStatus;
	}

	public void setTuandaiDistributeFundStatus(Integer tuandaiDistributeFundStatus) {
		this.tuandaiDistributeFundStatus = tuandaiDistributeFundStatus;
	}

	public String getIssueAfterType() {
		return issueAfterType;
	}

	public void setIssueAfterType(String issueAfterType) {
		this.issueAfterType = issueAfterType;
	}

	public Integer getTuandaiAdvanceStatus() {
		return tuandaiAdvanceStatus;
	}

	public void setTuandaiAdvanceStatus(Integer tuandaiAdvanceStatus) {
		this.tuandaiAdvanceStatus = tuandaiAdvanceStatus;
	}

	public Integer getTuandaiProfitStatus() {
		return tuandaiProfitStatus;
	}

	public void setTuandaiProfitStatus(Integer tuandaiProfitStatus) {
		this.tuandaiProfitStatus = tuandaiProfitStatus;
	}

	public String getTuandaiDistributeFundRemark() {
		return tuandaiDistributeFundRemark;
	}

	public void setTuandaiDistributeFundRemark(String tuandaiDistributeFundRemark) {
		this.tuandaiDistributeFundRemark = tuandaiDistributeFundRemark;
	}

	public String getBusinessAfterGuid() {
		return businessAfterGuid;
	}

	public void setBusinessAfterGuid(String businessAfterGuid) {
		this.businessAfterGuid = businessAfterGuid;
	}

	public String getTrackingAfterType() {
		return trackingAfterType;
	}

	public void setTrackingAfterType(String trackingAfterType) {
		this.trackingAfterType = trackingAfterType;
	}

	public Integer getLegalReturnStatus() {
		return legalReturnStatus;
	}

	public void setLegalReturnStatus(Integer legalReturnStatus) {
		this.legalReturnStatus = legalReturnStatus;
	}

	@Override
	protected Serializable pkVal() {
		return this.carBusinessId;
	}

	@Override
	public String toString() {
		return "CarBusinessAfter{" +
			", carBusinessId=" + carBusinessId +
			", carBusinessAfterId=" + carBusinessAfterId +
			", paratype=" + paratype +
			", CustomerName=" + CustomerName +
			", operatorName=" + operatorName +
			", operatorDept=" + operatorDept +
			", createTime=" + createTime +
			", modifyTime=" + modifyTime +
			", isDelete=" + isDelete +
			", repaymentType=" + repaymentType +
			", borrowMoney=" + borrowMoney +
			", oddcorpus=" + oddcorpus +
			", installmentNumDate=" + installmentNumDate +
			", installmentNum=" + installmentNum +
			", currentPrincipa=" + currentPrincipa +
			", currentAccrual=" + currentAccrual +
			", punitiveRateShould=" + punitiveRateShould +
			", punitiveRateFact=" + punitiveRateFact +
			", overdueDays=" + overdueDays +
			", borrowDate=" + borrowDate +
			", carBusinessAfterType=" + carBusinessAfterType +
			", carBusinessAfterDefer=" + carBusinessAfterDefer +
			", isDefer=" + isDefer +
			", carBusinessDeferId=" + carBusinessDeferId +
			", otherMoney=" + otherMoney +
			", factMoney=" + factMoney +
			", lawyerMoney=" + lawyerMoney +
			", createdate=" + createdate +
			", updateDate=" + updateDate +
			", isCollection=" + isCollection +
			", repayedFlag=" + repayedFlag +
			", reserve2=" + reserve2 +
			", reserve3=" + reserve3 +
			", reserve4=" + reserve4 +
			", reserve5=" + reserve5 +
			", ModifyUser=" + ModifyUser +
			", workflowstatus=" + workflowstatus +
			", reserve6=" + reserve6 +
			", reserve7=" + reserve7 +
			", reserve8=" + reserve8 +
			", reserve9=" + reserve9 +
			", reserve10=" + reserve10 +
			", TrackRecord=" + TrackRecord +
			", collectionDate=" + collectionDate +
			", badDebtMark=" + badDebtMark +
			", settleMark=" + settleMark +
			", factReplayMoney=" + factReplayMoney +
			", fatctReplayDate=" + fatctReplayDate +
			", factPrincipa=" + factPrincipa +
			", factAccrual=" + factAccrual +
			", overdueMoney=" + overdueMoney +
			", currentBreach=" + currentBreach +
			", repayService=" + repayService +
			", factService=" + factService +
			", remark=" + remark +
			", paymentType=" + paymentType +
			", currentOtherMoney=" + currentOtherMoney +
			", confirmFlag=" + confirmFlag +
			", collectionUser=" + collectionUser +
			", collectionRemark=" + collectionRemark +
			", outId=" + outId +
			", isLossSettle=" + isLossSettle +
			", financeConfirmedDate=" + financeConfirmedDate +
			", financeConfirmedUser=" + financeConfirmedUser +
			", autoWithholdingConfirmedDate=" + autoWithholdingConfirmedDate +
			", autoWithholdingConfirmedUser=" + autoWithholdingConfirmedUser +
			", financeBankId=" + financeBankId +
			", accountantConfirmStatus=" + accountantConfirmStatus +
			", accountantConfirmUser=" + accountantConfirmUser +
			", accountantConfirmDate=" + accountantConfirmDate +
			", tuandaiDistributeFundStatus=" + tuandaiDistributeFundStatus +
			", issueAfterType=" + issueAfterType +
			", tuandaiAdvanceStatus=" + tuandaiAdvanceStatus +
			", tuandaiProfitStatus=" + tuandaiProfitStatus +
			", tuandaiDistributeFundRemark=" + tuandaiDistributeFundRemark +
			", businessAfterGuid=" + businessAfterGuid +
			", trackingAfterType=" + trackingAfterType +
			", legalReturnStatus=" + legalReturnStatus +
			"}";
	}
}
