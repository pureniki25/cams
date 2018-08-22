package com.hongte.alms.base.vo.cams;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AccountBalanceChange {

    @ApiModelProperty(value = "actionId")
    //@NotNull@Min(0)
	private Integer actionId;

    @ApiModelProperty(value = "inOut")
    //@NotNull@Range(min = 0, max = 1)
    private Integer inOut;

    @Length(max=36)
    @NotEmpty
    @ApiModelProperty(value = "accountId")
    private String accountId;

    @NotEmpty@Pattern(regexp = "(\\d|\\s){10,50}", message = "银行卡格式错误")
    @ApiModelProperty(value = "bankCardNo")
    private String bankCardNo;

    @NotNull
    @Past
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "accountTime")
    private Date accountTime;

    @NotNull
    @ApiModelProperty(value = "amount")
    @DecimalMin("0")
    private BigDecimal amount;

    @Length(max=50)
    @NotEmpty
    @ApiModelProperty(value = "operator")
    private String operator;

    @Length(max=200)
    @ApiModelProperty(value = "remark")
    private String remark="";

    @Length(max=50)
    //@NotEmpty
    @ApiModelProperty(value = "createUser")
    private String createUser;

    @ApiModelProperty(required= true,value = "是否业务交易明细,1是,0否")
	//@Range(min=0,max=1)
	private Integer businessFlag=0;


    //private String businessTypeId="核心账务";
    //private String businessType="核心账务";

    public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
    
    public Integer getInOut() {
        return inOut;
    }

    public void setInOut(Integer inOut) {
        this.inOut = inOut;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo =  bankCardNo;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
         this.createUser = createUser;
    }

    public Integer getBusinessFlag() {
		return businessFlag;
	}

	public void setBusinessFlag(Integer businessFlag) {
		this.businessFlag = businessFlag;
	}
}
