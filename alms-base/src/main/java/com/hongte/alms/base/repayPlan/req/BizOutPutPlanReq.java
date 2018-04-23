package com.hongte.alms.base.repayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/4/23
 * 业务出款计划信息
 */
@ApiModel("业务出款计划信息")
public class BizOutPutPlanReq {


    /**
     * 业务编号
     */
    @TableField("business_id")
    @ApiModelProperty(required= true,value = "业务编号")
    private String businessId;
    /**
     * 计划出款时间
     */
    @TableField("output_time")
    @ApiModelProperty(required= true,value = "计划出款时间")
    private Date outputTime;
    /**
     * 转账类型
     */
    @TableField("output_type")
    @ApiModelProperty(required= true,value = "转账类型")
    private String outputType;
    /**
     * 计划出款金额
     */
    @TableField("output_money")
    @ApiModelProperty(required= true,value = "计划出款金额")
    private BigDecimal outputMoney;
    /**
     * 开户银行
     */
    @TableField("bank_name")
    @ApiModelProperty(required= true,value = "开户银行")
    private String bankName;
    /**
     * 支行名称
     */
    @TableField("bank_sub_name")
    @ApiModelProperty(required= true,value = "支行名称")
    private String bankSubName;
    /**
     * 开户名
     */
    @TableField("customer_name")
    @ApiModelProperty(required= true,value = "开户名")
    private String customerName;
    /**
     * 银行卡号
     */
    @TableField("bank_account")
    @ApiModelProperty(required= true,value = "银行卡号")
    private String bankAccount;
    /**
     * 银行卡归属地省
     */
    @TableField("bank_province")
    @ApiModelProperty(required= true,value = "银行卡归属地省")
    private String bankProvince;
    /**
     * 银行卡归属地市
     */
    @TableField("bank_city")
    @ApiModelProperty(required= true,value = "银行卡归属地市")
    private String bankCity;
    /**
     * 出款备注
     */
    @ApiModelProperty(required= true,value = "出款备注")
    private String remark;
    /**
     * 委托收款人平台ID
     */
    @TableField("creditorId_platformUserId")
    @ApiModelProperty(required= true,value = "委托收款人平台ID")
    private String creditorIdPlatformUserId;
    /**
     * 委托收款人姓名
     */
    @TableField("creditor_name")
    @ApiModelProperty(required= true,value = "委托收款人姓名")
    private String creditorName;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(required= true,value = "创建时间")
    private Date createTime;
    /**
     * 创建用户
     */
    @TableField("create_user")
    @ApiModelProperty(required= true,value = "创建用户")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(required= true,value = "更新时间")
    private Date updateTime;
    /**
     * 更新用户
     */
    @TableField("update_user")
    @ApiModelProperty(required= true,value = "更新用户")
    private String updateUser;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }


    public Date getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(Date outputTime) {
        this.outputTime = outputTime;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public BigDecimal getOutputMoney() {
        return outputMoney;
    }

    public void setOutputMoney(BigDecimal outputMoney) {
        this.outputMoney = outputMoney;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankSubName() {
        return bankSubName;
    }

    public void setBankSubName(String bankSubName) {
        this.bankSubName = bankSubName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreditorIdPlatformUserId() {
        return creditorIdPlatformUserId;
    }

    public void setCreditorIdPlatformUserId(String creditorIdPlatformUserId) {
        this.creditorIdPlatformUserId = creditorIdPlatformUserId;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
