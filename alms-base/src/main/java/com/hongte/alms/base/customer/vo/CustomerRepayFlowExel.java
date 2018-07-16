package com.hongte.alms.base.customer.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.math.BigDecimal;

@ExcelTarget("customerRepayFlowExel")
public class CustomerRepayFlowExel implements Serializable {

    private static final long serialVersionUID = -1334491522287867479L;

    @Excel(name = "序号", orderNum = "1", isImportField = "true_st")
    private String id;

    @Excel(name = "登记日期", orderNum = "2", isImportField = "true_st")
    private String createTime; //登记日期
    @Excel(name = "流水编号", orderNum = "3", isImportField = "true_st")
    private String moneyPoolId; //'流水编号'
    @Excel(name = "业务编号", orderNum = "4", isImportField = "true_st")
    private String originalBusinessId; //'业务编号'
    @Excel(name = "客户名称", orderNum = "5", isImportField = "true_st")
    private String operateName;//'客户名称'

    @Excel(name = "期数", orderNum = "6", isImportField = "true_st")
    private String afterId;//'期数'
    @Excel(name = "所属分公司", orderNum = "7", isImportField = "true_st")
    private String companyName;//'所属分公司'
    @Excel(name = "转账金额", orderNum = "8", isImportField = "true_st")
    private Double accountMoney;//'转账金额'

    @Excel(name = "本期应还金额", orderNum = "9", isImportField = "true_st")
    private Double totalBorrowAmount;//'本期应还金额'
    @Excel(name = "实际转账人", orderNum = "10", isImportField = "true_st")
    private String factTransferName;//'实际转账人'
    @Excel(name = "转账日期", orderNum = "11", isImportField = "true_st")
    private String tradeDate;//'转账日期'

    @Excel(name = "交易类型", orderNum = "12", isImportField = "true_st")
    private String tradeType;//'交易类型'
    @Excel(name = "转入账号", orderNum = "13", isImportField = "true_st")
    private String bankAccount;//'转入账号'

    @Excel(name = "状态", orderNum = "14", replace = {"未审核_未关联银行流水", "已审核_财务确认已还款", "拒绝_还款登记被财务拒绝"}, isImportField = "true_st")
    private String state;//'状态'

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    public String getOriginalBusinessId() {
        return originalBusinessId;
    }

    public void setOriginalBusinessId(String originalBusinessId) {
        this.originalBusinessId = originalBusinessId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(Double accountMoney) {
        this.accountMoney = accountMoney;
    }

    public Double getTotalBorrowAmount() {
        return totalBorrowAmount;
    }

    public void setTotalBorrowAmount(Double totalBorrowAmount) {
        this.totalBorrowAmount = totalBorrowAmount;
    }

    public String getFactTransferName() {
        return factTransferName;
    }

    public void setFactTransferName(String factTransferName) {
        this.factTransferName = factTransferName;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
