package com.hongte.alms.base.vo.cams;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 账户信息实体类
 * @author liuzq
 * @since 2018-01-19
 */
@ApiModel
public class Account {

	private static final long serialVersionUID = 1L;

	public void handleAccountModifiedEvent(Account originalAccount) {
		// if (StringUtil.isNullOrEmpty(this.getAccountId())) {
		// 	this.setAccountId(originalAccount.getAccountId());
		// }

		// if (StringUtil.isNullOrEmpty(this.getAccountNo())) {
		// 	this.setAccountNo(originalAccount.getAccountNo());
		// }
		//应对信贷更新
		this.setAccountId(originalAccount.getAccountId());
		this.setAccountNo(originalAccount.getAccountNo());

		this.setCreateTime(originalAccount.getCreateTime());
		this.setCreateUser(originalAccount.getCreateUser());

		this.setAccountAmount(originalAccount.getAccountAmount());
		this.setFrozenAmount(originalAccount.getFrozenAmount());

		this.setDepositAmount(originalAccount.getDepositAmount());	

		this.setUpdateTime(DateUtil.getNow());

		if (this.getStatusFlag() == null) {
			this.setStatusFlag(originalAccount.getStatusFlag());
		}
	}

	public void handleAccountListCreatedEvent(AccountList flow) {
		if (flow.getInOut() == 1) {
			this.setAccountAmount(this.getAccountAmount().add(flow.getAmount()));

		} else {
			this.setAccountAmount(this.getAccountAmount().subtract(flow.getAmount()));

		}

		this.setUpdateTime(DateUtil.getNow());
	}

	public void handleAccountListCanceledEvent(AccountList flow) {
		if (flow.getInOut() == 0) {
			this.setAccountAmount(this.getAccountAmount().add(flow.getAmount()));

		} else {
			this.setAccountAmount(this.getAccountAmount().subtract(flow.getAmount()));

		}

		this.setUpdateTime(DateUtil.getNow());
	}

	public void handleBankCardModifiedEvent(AccountBankCard originalBankCard, AccountBankCard currentBankCard) {

		handleBankCardChangedEvent(originalBankCard.getStatusFlag(),
				new BigDecimal(0).subtract(originalBankCard.getFrozenAmount()),
				new BigDecimal(0).subtract(originalBankCard.getAccountAmount()));

		handleBankCardChangedEvent(currentBankCard.getStatusFlag(), currentBankCard.getFrozenAmount(),
				currentBankCard.getAccountAmount());

		this.setUpdateTime(DateUtil.getNow());
	}

	public void handleBankCardCreatedEvent(AccountBankCard currentBankCard) {
		handleBankCardChangedEvent(currentBankCard.getStatusFlag(), currentBankCard.getFrozenAmount(),
				currentBankCard.getAccountAmount());

		this.setUpdateTime(DateUtil.getNow());

	}

	public void handleBankCardDeletedEvent(AccountBankCard originalBankCard) {
		handleBankCardChangedEvent(originalBankCard.getStatusFlag(),
				new BigDecimal(0).subtract(originalBankCard.getFrozenAmount()),
				new BigDecimal(0).subtract(originalBankCard.getAccountAmount()));

		this.setUpdateTime(DateUtil.getNow());
	}

	private void handleBankCardChangedEvent(Integer status, BigDecimal frozen, BigDecimal balance) {
		status = status == null ? 1 : status;
		frozen = frozen == null ? new BigDecimal(0) : frozen;
		balance = balance == null ? new BigDecimal(0) : balance;

		if (this.getFrozenAmount() == null) {
			this.setFrozenAmount(new BigDecimal(0));
		}

		if (this.getAccountAmount() == null) {
			this.setAccountAmount(new BigDecimal(0));
		}
		switch (status) {
		case 0:
			/*
			if (!isInsertOrOriginal) {//新增时如果是关闭状态，则不必处理余额
				this.setFrozenAmount(this.getFrozenAmount().subtract(frozen));
				this.setAccountAmount(this.getAccountAmount().subtract(balance));
			}
			*/
			break;
		case 1:
			this.setFrozenAmount(this.getFrozenAmount().add(frozen));
			this.setAccountAmount(this.getAccountAmount().add(balance));
			break;
		case 2:
			this.setFrozenAmount(this.getFrozenAmount().add(frozen).add(balance));
			break;
		}
	}

	/**
	 * 账户编号GUID
	 */
	@TableId("account_id")
	@ApiModelProperty(value = "账户编号GUID")
	@Length(max = 36)
	private String accountId;
	/**
	 * 账户号码,帐号标识
	 */
	@TableField("account_no")
	@ApiModelProperty(required = true, value = "账户号码,帐号标识")
	@Length(max = 50)
	private String accountNo;
	/**
	 * 账户类型,0借款人账户,1分公司账户,2资金渠道账户,3担保公司账户,4合作渠道账户,5中介机构账户,6内部账户,7业务员账户,8投资人账户,9贷充值账户
	 */
	@TableField("account_type")
	@ApiModelProperty(required = true, value = "账户类型,0借款人账户,1分公司账户,2资金渠道账户,3担保公司账户,4合作渠道账户,5中介机构账户,6内部账户,7业务员账户,8投资人账户,9贷充值账户")
	@NotNull
	@Min(0)
	private Integer accountType;
	/**
	 * 账户名称
	 */
	@TableField("account_name")
	@ApiModelProperty(required = true, value = "账户名称")
	@NotEmpty
	@Length(max = 50)
	private String accountName;
	/**
	 * 账户主体编号,个人是身份证,企业是统一信用编码或者营业执照
	 */
	@TableField("main_id")
	@ApiModelProperty(required = true, value = "账户主体编号,个人是身份证,企业是统一信用编码或者营业执照")
	@NotEmpty
	@Length(max = 50)
	private String mainId;
	/**
	 * 账户主体类型,1;个人,2:企业
	 */
	@TableField("main_type")
	@ApiModelProperty(required = true, value = "账户主体类型,1;个人,2:企业")
	@NotNull
	@Min(1)
	private Integer mainType;
	/**
	 * 所属资产端,1:鸿特,2:一点车贷...
	 */
	@TableField("business_from")
	@ApiModelProperty(required = true, value = "所属资产端,1:鸿特,2:一点车贷...")
	@NotNull
	@Min(1)
	private Integer businessFrom;
	/**
	 * 所属分公司编号
	 */
	@TableField(value = "branch_id")
	@ApiModelProperty(required = true, value = "所属分公司编号")
	@Length(max = 50)
	private String branchId;
	/**
	 * 所属分公司名称
	 */
	@TableField("branch_name")
	@ApiModelProperty(required = true, value = "所属分公司名称")
	@Length(max = 50)
	private String branchName;
	/**
	 * 存管余额
	 */
	@TableField("deposit_amount")
	@ApiModelProperty(required = true, value = "存管余额")
	//@NotNull
	@Min(0)
	private BigDecimal depositAmount;
	/**
	 * 可用余额
	 */
	@TableField("account_amount")
	@ApiModelProperty(required = true, value = "可用余额")
	//@NotNull
	@Min(0)
	private BigDecimal accountAmount;
	/**
	 * 冻结余额
	 */
	@TableField("frozen_amount")
	@ApiModelProperty(required = true, value = "冻结余额")
	//@NotNull
	@Min(0)
	private BigDecimal frozenAmount;
	/**
	 * 账户状态,1:启用,0:禁用
	 */
	@TableField("status_flag")
	@ApiModelProperty(required = true, value = "账户状态,1:启用,0:禁用")
	@Min(0)
	private Integer statusFlag;

	/**
	 * 创建用户
	 */
	@TableField("create_user")
	@ApiModelProperty(required = true, value = "创建用户")
	@Length(max = 50)
	private String createUser;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@ApiModelProperty(required = true, value = "创建时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 更新用户
	 */
	@TableField("update_user")
	@ApiModelProperty(required = true, value = "更新用户")
	@Length(max = 50)
	private String updateUser;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@ApiModelProperty(required = true, value = "更新时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**
	 * 备注
	 */
	@ApiModelProperty(required = true, value = "备注")
	@Length(max = 200)
	private String remark;

	/**
	 * 存管ID
	 */
	@TableField("depository_id")
	@ApiModelProperty(required= true,value = "存管ID")
	private String depositoryId;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public Integer getMainType() {
		return mainType;
	}

	public void setMainType(Integer mainType) {
		this.mainType = mainType;
	}

	public Integer getBusinessFrom() {
		return businessFrom;
	}

	public void setBusinessFrom(Integer businessFrom) {
		this.businessFrom = businessFrom;
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

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Account{" + ", accountId=" + accountId + ", accountNo=" + accountNo + ", accountType=" + accountType
				+ ", accountName=" + accountName + ", mainId=" + mainId + ", mainType=" + mainType + ", businessFrom="
				+ businessFrom + ", branchId=" + branchId + ", branchName=" + branchName + ", accountAmount="
				+ accountAmount +", depositAmount=" + depositAmount + ", frozenAmount=" + frozenAmount + ", statusFlag=" + statusFlag + ", createUser="
				+ createUser + ", createTime=" + createTime + ", updateUser=" + updateUser + ", updateTime="
				+ updateTime + ", remark=" + remark + "}";
	}

	public void setDepositoryId(String depositoryId) {
		this.depositoryId = depositoryId;
	}

	public String getDepositoryId() {
		return depositoryId;
	}
}
