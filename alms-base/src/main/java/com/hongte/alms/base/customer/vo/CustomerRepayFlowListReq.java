package com.hongte.alms.base.customer.vo;

import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(value="贷后客户还款登记流水查询对象",description="贷后客户还款登记流水查询对象")
public class CustomerRepayFlowListReq extends PageRequest {

    @ApiModelProperty(required= false,value = "[登记开始时间]")
    private String regStartTime;

    @ApiModelProperty(required= false,value = "[登记结束时间]")
    private String regEndTime;

    @ApiModelProperty(required= false,value = "[业务编号]")
    private String businessId;

    @ApiModelProperty(required= false,value = "[所属分公司]")
    private String companyId;

    @ApiModelProperty(required= false,value = "[状态]")
    private String state;

    @ApiModelProperty(required= false,value = "[转账开始时间]")
    private String accountStartTime;

    @ApiModelProperty(required= false,value = "[转账结束时间]")
    private String accountEndTime;

    @ApiModelProperty(required= false,value = "[客户姓名]")
    private String customerName;

    @ApiModelProperty(required= false,value = "[转入账号]")
    private String bankAccount;

    @ApiModelProperty(required= false,value = "[转账金额]")
    private BigDecimal accountMoney;

    public String getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(String regStartTime) {
        this.regStartTime = StringUtil.isEmpty(regStartTime) ? null : regStartTime;
    }

    public String getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(String regEndTime) {
        this.regEndTime = StringUtil.isEmpty(regEndTime) ? null : regEndTime;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = StringUtil.isEmpty(businessId) ? null : businessId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = StringUtil.isEmpty(companyId) ? null : companyId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = StringUtil.isEmpty(state) ? null : state;
    }

    public String getAccountStartTime() {
        return accountStartTime;
    }

    public void setAccountStartTime(String accountStartTime) {
        this.accountStartTime = StringUtil.isEmpty(accountStartTime) ? null : accountStartTime;
    }

    public String getAccountEndTime() {
        return accountEndTime;
    }

    public void setAccountEndTime(String accountEndTime) {
        this.accountEndTime = StringUtil.isEmpty(accountEndTime) ? null : accountEndTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = StringUtil.isEmpty(customerName) ? null : customerName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = StringUtil.isEmpty(bankAccount) ? null : bankAccount;
    }

    public BigDecimal getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(BigDecimal accountMoney) {
        this.accountMoney = accountMoney;
    }
}
