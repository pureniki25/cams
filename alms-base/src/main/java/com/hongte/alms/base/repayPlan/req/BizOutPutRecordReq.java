package com.hongte.alms.base.repayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/4/23
 * 业务出款记录信息
 */
@ApiModel("业务出款记录信息")
public class BizOutPutRecordReq {


    /**
     * 业务编号
     */
    @TableField("business_id")
    @ApiModelProperty(required= true,value = "业务编号")
    private String businessId;
    /**
     * 上标编号，若线下出款则为空
     */
    @TableField("project_id")
    @ApiModelProperty(required= true,value = "上标编号，若线下出款则为空")
    private String projectId;
    /**
     * 实际出款金额
     */
    @TableField("fact_output_money")
    @ApiModelProperty(required= true,value = "实际出款金额")
    private BigDecimal factOutputMoney;
    /**
     * 实际出款时间
     */
    @TableField("fact_output_date")
    @ApiModelProperty(required= true,value = "实际出款时间")
    private Date factOutputDate;
    /**
     * 出款经手人ID
     */
    @TableField("output_user_id")
    @ApiModelProperty(required= true,value = "出款经手人ID")
    private String outputUserId;
    /**
     * 出款经手人姓名
     */
    @TableField("output_user_name")
    @ApiModelProperty(required= true,value = "出款经手人姓名")
    private String outputUserName;
    /**
     * 银行卡号
     */
    @TableField("bank_card")
    @ApiModelProperty(required= true,value = "银行卡号")
    private String bankCard;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    @ApiModelProperty(required= true,value = "银行名称")
    private String bankName;
    /**
     * 0：线下出款，1：借款人提现，2：保证金提现，3：押金提现，4：期初收费
     */
    @TableField("withdraw_type")
    @ApiModelProperty(required= true,value = "0：线下出款，1：借款人提现，2：保证金提现，3：押金提现，4：期初收费")
    private Integer withdrawType;
    /**
     * 放款去处(线下出款均为1)，1:提到银行卡  2：转到可用金额
     */
    @TableField("withdraw_place")
    @ApiModelProperty(required= true,value = "放款去处(线下出款均为1)，1:提到银行卡  2：转到可用金额")
    private Integer withdrawPlace;
    /**
     * 提现状态说明
     */
    @TableField("withdraw_strStatus")
    @ApiModelProperty(required= true,value = "提现状态说明")
    private String withdrawStrStatus;
    /**
     * 提现流水号，若线下出款则为空
     */
    @ApiModelProperty(required= true,value = "提现流水号，若线下出款则为空")
    @TableField("XDWithDrawId")
    private String XDWithDrawId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(required= true,value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(required= true,value = "创建人")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(required= true,value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(required= true,value = "更新人")
    private String updateUser;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getFactOutputMoney() {
        return factOutputMoney;
    }

    public void setFactOutputMoney(BigDecimal factOutputMoney) {
        this.factOutputMoney = factOutputMoney;
    }

    public Date getFactOutputDate() {
        return factOutputDate;
    }

    public void setFactOutputDate(Date factOutputDate) {
        this.factOutputDate = factOutputDate;
    }

    public String getOutputUserId() {
        return outputUserId;
    }

    public void setOutputUserId(String outputUserId) {
        this.outputUserId = outputUserId;
    }

    public String getOutputUserName() {
        return outputUserName;
    }

    public void setOutputUserName(String outputUserName) {
        this.outputUserName = outputUserName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
    }

    public Integer getWithdrawPlace() {
        return withdrawPlace;
    }

    public void setWithdrawPlace(Integer withdrawPlace) {
        this.withdrawPlace = withdrawPlace;
    }

    public String getWithdrawStrStatus() {
        return withdrawStrStatus;
    }

    public void setWithdrawStrStatus(String withdrawStrStatus) {
        this.withdrawStrStatus = withdrawStrStatus;
    }

    public String getXDWithDrawId() {
        return XDWithDrawId;
    }

    public void setXDWithDrawId(String XDWithDrawId) {
        this.XDWithDrawId = XDWithDrawId;
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
