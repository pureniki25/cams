package com.hongte.alms.base.customer.vo;


public class CustomerRepayFlowDto {

    private String createTime; //登记日期
    private String moneyPoolId; //'流水编号'
    private String originalBusinessId; //'业务编号'

    private String operateName;//'客户名称'
    private String afterId;//'期数'
    private String companyName;//'所属分公司'
    private String accountMoney;//'转账金额'

    private String totalBorrowAmount;//'本期应还金额'
    private String factTransferName;//'实际转账人'
    private String tradeDate;//'转账日期'

    private String tradeType;//'交易类型'
    private String bankAccount;//'转入账号'

    private String state;//'状态'

    private String certificatePictureUrl;//凭证

    private String id;

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

    public String getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(String accountMoney) {
        this.accountMoney = accountMoney;
    }

    public String getTotalBorrowAmount() {
        return totalBorrowAmount;
    }

    public void setTotalBorrowAmount(String totalBorrowAmount) {
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

    public String getCertificatePictureUrl() {
        return certificatePictureUrl;
    }

    public void setCertificatePictureUrl(String certificatePictureUrl) {
        this.certificatePictureUrl = certificatePictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
