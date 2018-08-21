package com.hongte.alms.base.vo.cams;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 账户明细表
 * </p>
 *
 * @author 张鹏
 * @since 2018-01-19
 */
@ApiModel
public class AccountList extends Model<AccountList> {

	private static final long serialVersionUID = 1L;

	/**
	 * 明细编号
	 */
	@ApiModelProperty(required = true, value = "明细编号")
	private Integer listId;
	/**
	 * 账户编号
	 */
	@ApiModelProperty(required = true, value = "账户编号")
	@Length(max = 36)
	private String accountId;
	/**
	 * 记账日期
	 */
	@ApiModelProperty(required = true, value = "记账日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull
	@Past
	private Date accountTime;
	/**
	 * 业务编号
	 */
	@ApiModelProperty(required = true, value = "业务编号")
	@Length(max = 50)
	private String businessId;
	/**
	 * 标的id
	 */
	@ApiModelProperty(required = true, value = "标的id")
	@Length(max = 50)
	private String issueId;
	/**
	 * 期数
	 */
	@ApiModelProperty(required = true, value = "期数")
	@Length(max = 50)
	private String afterId;
	/**
	 * 展期编号
	 */
	@ApiModelProperty(required = true, value = "展期编号")
	@Length(max = 50)
	private String exhibitionId;
	/**
	 * 借款人
	 */
	@ApiModelProperty(required = true, value = "借款人")
	@Length(max = 50)
	private String customerName;
	/**
	 * 记账金额
	 */
	@ApiModelProperty(required = true, value = "记账金额")
	@NotNull
	@Min(0)
	private BigDecimal amount;
	/**
	 * 账户余额
	 */
	@ApiModelProperty(required = true, value = "账户余额")
	@NotNull
	@Min(0)
	private BigDecimal balance;
	/**
	 * 银行账号
	 */
	@ApiModelProperty(required = true, value = "银行账号")
	@Length(max = 50)
	@Pattern(regexp = "(\\d|\\s){10,25}", message = "银行卡格式错误")
	private String bankCardNo;
	/**
	 * 交易活动,0满标分润,1提现放款...
	 */
	@ApiModelProperty(required = true, value = "交易活动,0满标分润,1提现放款...")
	@NotNull
	@Min(0)
	private Integer actionId;
	/**
	 * 收支类型,0支出,1收入
	 */
	@ApiModelProperty(required = true, value = "收支类型,0支出,1收入")
	@NotNull
	@Range(min = 0, max = 1)
	private Integer inOut;

	/**
	 * 导入方式,0自动导入,1手动导入
	 */
	@ApiModelProperty(required= true,value = "导入方式,0自动导入,1手动导入")
	private Integer importType;

	/**
	 * 创建用户
	 */
	@ApiModelProperty(required = true, value = "创建用户")
	@Length(max = 50)
	private String createUser;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required = true, value = "创建时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 经办人（默认是当前用户）
	 */
	@ApiModelProperty(required = true, value = "经办人（默认是当前用户）")

	@Length(max = 50)
	private String operator;
	/**
	 * 状态
	 */
	@ApiModelProperty(required = true, value = "状态")
	@Min(0)
	private Integer statusFlag;
	/**
	 * 备注
	 */
	@ApiModelProperty(required = true, value = "备注")
	@Length(max = 200)
	private String remark;
    /**
     * 流水备注
     */
    @ApiModelProperty(required = true, value = "流水备注")
    @Length(max = 1000)
    private String memo;
	/**
	 * 是否业务交易明细,1是,0否
	 */
	@ApiModelProperty(required = true, value = "是否业务交易明细,1是,0否")
	@NotNull
	@Range(min = 0, max = 1)
	private Integer businessFlag;
	/**
	 * 还款类型,0非还款流水，1手动还款，2宝付代扣，3易宝代扣，4银行代扣
	 */
	@ApiModelProperty(required = true, value = "还款类型,0非还款流水，1手动还款，2宝付代扣，3易宝代扣，4银行代扣")
	@NotNull
	@Min(0)
	private Integer repayType;
	/**
	 * 目标账户编号
	 */
	@ApiModelProperty(required = true, value = "目标账户编号")
	@Length(max = 36)
	private String targetAccountId;
	/**
	 * 目标银行账号
	 */
	@ApiModelProperty(required = true, value = "目标银行账号")
	@Length(max = 50)
	@Pattern(regexp = "(\\d|\\s){10,25}", message = "银行卡格式错误")
	private String targetBankCardNo;
	/**
	 * 分公司id
	 */
	@ApiModelProperty(required= true,value = "分公司id")
	private String branchId;
	/**
	 * 分公司名称
	 */
	@ApiModelProperty(required= true,value = "分公司名称")
	private String branchName;
	/**
	 * 业务类型id
	 */
	@ApiModelProperty(required= true,value = "业务类型id")
	private String businessTypeId;
	/**
	 * 业务类型
	 */
	@ApiModelProperty(required= true,value = "业务类型")
	private String businessType;
	/**
	 * 外联id
	 */
	@ApiModelProperty(required= true,value = "外联id")
	private String externalId;
	/**
	 * 流水批次
	 */
	@ApiModelProperty(required= true,value = "流水批次")
	private String batchId;
	/**
	 * 更新用户
	 */
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(required= true,value = "更新时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 关联日志id
	 */
	@ApiModelProperty(required= true,value = "关联日志id")
	private String logId;

	/**
	 * 账户名称
	 */
	@ApiModelProperty(required= true,value = "账户名称")
	private String accountName;
	/**
	 * 开户行
	 */
	@ApiModelProperty(required= true,value = "开户行")
	private String openBank;
	/**
	 * 所属资产端
	 */
	@ApiModelProperty(required= true,value = "所属资产端")
	private Integer businessFrom;

	public Date getSegmentationDate() {
		return segmentationDate;
	}

	public void setSegmentationDate(Date segmentationDate) {
		this.segmentationDate = segmentationDate;
	}

	/**
	 * 数据分割时间点
	 */
	@ApiModelProperty(required= true,value = "数据分割时间点")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date segmentationDate;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getExhibitionId() {
		return exhibitionId;
	}

	public void setExhibitionId(String exhibitionId) {
		this.exhibitionId = exhibitionId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

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

	public Integer getImportType() {
		return importType;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getBusinessFlag() {
		return businessFlag;
	}

	public void setBusinessFlag(Integer businessFlag) {
		this.businessFlag = businessFlag;
	}

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(String targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public String getTargetBankCardNo() {
		return targetBankCardNo;
	}

	public void setTargetBankCardNo(String targetBankCardNo) {
		this.targetBankCardNo = targetBankCardNo;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getBusinessFrom() {
		return businessFrom;
	}

	public void setBusinessFrom(Integer businessFrom) {
		this.businessFrom = businessFrom;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	@Override
	protected Serializable pkVal() {
		return this.listId;
	}

	@Override
	public String toString() {
		return "AccountList{" +
				"listId=" + listId +
				", accountId='" + accountId + '\'' +
				", accountTime=" + accountTime +
				", businessId='" + businessId + '\'' +
				", issueId='" + issueId + '\'' +
				", afterId='" + afterId + '\'' +
				", exhibitionId='" + exhibitionId + '\'' +
				", customerName='" + customerName + '\'' +
				", amount=" + amount +
				", balance=" + balance +
				", bankCardNo='" + bankCardNo + '\'' +
				", actionId=" + actionId +
				", inOut=" + inOut +
				", importType=" + importType +
				", createUser='" + createUser + '\'' +
				", createTime=" + createTime +
				", operator='" + operator + '\'' +
				", statusFlag=" + statusFlag +
				", remark='" + remark + '\'' +
				", businessFlag=" + businessFlag +
				", repayType=" + repayType +
				", targetAccountId='" + targetAccountId + '\'' +
				", targetBankCardNo='" + targetBankCardNo + '\'' +
				", branchId='" + branchId + '\'' +
				", branchName='" + branchName + '\'' +
				", businessTypeId='" + businessTypeId + '\'' +
				", businessType='" + businessType + '\'' +
				", externalId='" + externalId + '\'' +
				", batchId='" + batchId + '\'' +
				", updateUser='" + updateUser + '\'' +
				", updateTime=" + updateTime +
				", logId='" + logId + '\'' +
				", accountName='" + accountName + '\'' +
				", openBank='" + openBank + '\'' +
				", businessFrom=" + businessFrom +
				", segmentationDate=" + segmentationDate +
				'}';
	}
}
