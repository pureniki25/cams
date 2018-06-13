package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 财务款项池表
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
@ApiModel
@TableName("tb_money_pool")
public class MoneyPool extends Model<MoneyPool> {

    private static final long serialVersionUID = 1L;

    /**
     * 款项池编号guid
     */
    @TableId("money_pool_id")
	@ApiModelProperty(required= true,value = "款项池编号guid")
	private String moneyPoolId;
    /**
     * 原信贷款项池ID
     */
	@TableField("xd_pool_id")
	@ApiModelProperty(required= true,value = "原信贷款项池ID")
	private Integer xdPoolId;
    /**
     * 款项池
     */
	@TableField("pay_code")
	@ApiModelProperty(required= true,value = "款项池")
	private String payCode;
    /**
     * 汇款银行
     */
	@TableField("remit_bank")
	@ApiModelProperty(required= true,value = "汇款银行")
	private String remitBank;
    /**
     * 转入时间
     */
	@TableField("trade_date")
	@ApiModelProperty(required= true,value = "转入时间")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date tradeDate;
    /**
     * 交易类型
     */
	@TableField("trade_type")
	@ApiModelProperty(required= true,value = "交易类型")
	private String tradeType;
    /**
     * 摘要
     */
	@ApiModelProperty(required= true,value = "摘要")
	private String summary;
    /**
     * 交易场所
     */
	@TableField("trade_place")
	@ApiModelProperty(required= true,value = "交易场所")
	private String tradePlace;
    /**
     * 记账金额(元)
     */
	@TableField("account_money")
	@ApiModelProperty(required= true,value = "记账金额(元)")
	private BigDecimal accountMoney;
    /**
     * 收入类型，1：收入，2：支出
     */
	@TableField("income_type")
	@ApiModelProperty(required= true,value = "收入类型，1：收入，2：支出")
	private Integer incomeType;
    /**
     * 交易备注
     */
	@TableField("trade_remark")
	@ApiModelProperty(required= true,value = "交易备注")
	private String tradeRemark;
    /**
     * 领取人
     */
	@TableField("gainer_name")
	@ApiModelProperty(required= true,value = "领取人")
	private String gainerName;
    /**
     * 财务状态，"财务确认已还款"、"未关联银行流水"
     */
	@TableField("finance_status")
	@ApiModelProperty(required= true,value = "财务状态/财务确认已还款/未关联银行流水")
	private String financeStatus;
    /**
     * 流水状态，"完成"，"已领取","待领取"
     */
	@ApiModelProperty(required= true,value = "流水状态/完成/已领取/待领取")
	private String status;
    /**
     * 导入人ID
     */
	@TableField("import_user")
	@ApiModelProperty(required= true,value = "导入人ID")
	private String importUser;
    /**
     * 导入人姓名
     */
	@TableField("import_user_name")
	@ApiModelProperty(required= true,value = "导入人姓名")
	private String importUserName;
    /**
     * 导入时间
     */
	@TableField("import_time")
	@ApiModelProperty(required= true,value = "导入时间")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date importTime;
    /**
     * 转入账号
     */
	@TableField("accept_bank")
	@ApiModelProperty(required= true,value = "转入账号")
	private String acceptBank;
    /**
     * 是否手动添加(0或null为批量导入，1为手动添加)
     */
	@TableField("is_manual_create")
	@ApiModelProperty(required= true,value = "是否手动添加(0或null为批量导入，1为手动添加)")
	private Integer isManualCreate;
    /**
     * 是否暂收款流水(0或null不是，1是)
     */
	@TableField("is_temporary")
	@ApiModelProperty(required= true,value = "是否暂收款流水(0或null不是，1是)")
	private Integer isTemporary;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
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
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
    /**
     * 创建人的角色(客户/财务)
     */
	@TableField("create_user_role")
	@ApiModelProperty(required= true,value = "创建人的角色(客户/财务)")
	private String createUserRole;

	@TableField("last_status")
	private String lastStatus ;
	
	@TableField("last_finance_status")
	private String lastFinanceStatus ;

	public String getMoneyPoolId() {
		return moneyPoolId;
	}

	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}

	public Integer getXdPoolId() {
		return xdPoolId;
	}

	public void setXdPoolId(Integer xdPoolId) {
		this.xdPoolId = xdPoolId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getRemitBank() {
		return remitBank;
	}

	public void setRemitBank(String remitBank) {
		this.remitBank = remitBank;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTradePlace() {
		return tradePlace;
	}

	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}

	public BigDecimal getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}

	public Integer getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(Integer incomeType) {
		this.incomeType = incomeType;
	}

	public String getTradeRemark() {
		return tradeRemark;
	}

	public void setTradeRemark(String tradeRemark) {
		this.tradeRemark = tradeRemark;
	}

	public String getGainerName() {
		return gainerName;
	}

	public void setGainerName(String gainerName) {
		this.gainerName = gainerName;
	}

	public String getFinanceStatus() {
		return financeStatus;
	}

	public void setFinanceStatus(String financeStatus) {
		this.financeStatus = financeStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImportUser() {
		return importUser;
	}

	public void setImportUser(String importUser) {
		this.importUser = importUser;
	}

	public String getImportUserName() {
		return importUserName;
	}

	public void setImportUserName(String importUserName) {
		this.importUserName = importUserName;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getAcceptBank() {
		return acceptBank;
	}

	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}

	public Integer getIsManualCreate() {
		return isManualCreate;
	}

	public void setIsManualCreate(Integer isManualCreate) {
		this.isManualCreate = isManualCreate;
	}

	public Integer getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(Integer isTemporary) {
		this.isTemporary = isTemporary;
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

	public String getCreateUserRole() {
		return createUserRole;
	}

	public void setCreateUserRole(String createUserRole) {
		this.createUserRole = createUserRole;
	}

	@Override
	protected Serializable pkVal() {
		return this.moneyPoolId;
	}

	@Override
	public String toString() {
		return "MoneyPool{" +
			", moneyPoolId=" + moneyPoolId +
			", xdPoolId=" + xdPoolId +
			", payCode=" + payCode +
			", remitBank=" + remitBank +
			", tradeDate=" + tradeDate +
			", tradeType=" + tradeType +
			", summary=" + summary +
			", tradePlace=" + tradePlace +
			", accountMoney=" + accountMoney +
			", incomeType=" + incomeType +
			", tradeRemark=" + tradeRemark +
			", gainerName=" + gainerName +
			", financeStatus=" + financeStatus +
			", status=" + status +
			", importUser=" + importUser +
			", importUserName=" + importUserName +
			", importTime=" + importTime +
			", acceptBank=" + acceptBank +
			", isManualCreate=" + isManualCreate +
			", isTemporary=" + isTemporary +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createUserRole=" + createUserRole +
			"}";
	}

	/**
	 * @return the lastStatus
	 */
	public String getLastStatus() {
		return lastStatus;
	}

	/**
	 * @param lastStatus the lastStatus to set
	 */
	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	/**
	 * @return the lastFinanceStatus
	 */
	public String getLastFinanceStatus() {
		return lastFinanceStatus;
	}

	/**
	 * @param lastFinanceStatus the lastFinanceStatus to set
	 */
	public void setLastFinanceStatus(String lastFinanceStatus) {
		this.lastFinanceStatus = lastFinanceStatus;
	}

	
	
}
