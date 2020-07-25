package com.hongte.alms.base.vo.cams;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 帐号银行卡
 * </p>
 *
 * @author 张鹏
 * @since 2018-01-19
 */
@ApiModel
@TableName("tb_account_bank_card")
public class AccountBankCard extends Model<AccountBankCard> {

	private static final long serialVersionUID = 1L;

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

	public void handleBankCardCreatedEvent(Account owner) {
		//信贷 有可能为空
		if (this.getAccountId() == null) {
			this.setAccountId(owner.getAccountId());
		}
		this.setAccountName(owner.getAccountName());
		
		this.setAccountAmount(new BigDecimal(0));
		this.setFrozenAmount(new BigDecimal(0));

		this.setCreateTime(DateUtil.getNow());
	}

	/**
	 * 账户银行卡编号GUID
	 */
	@TableId("account_bank_id")
	@ApiModelProperty(required = true, value = "账户银行卡编号GUID")
	@Length(max = 36)
	private String accountBankId;
	/**
	 * 账户编号
	 */
	@TableField("account_id")
	@ApiModelProperty(required = true, value = "账户编号")
	@Length(max = 36)
	private String accountId;

	@TableField("account_name")
	@ApiModelProperty(required = true, value = "账户编号")
	private String accountName;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * 银行账号
	 */
	@TableField("bank_card_no")
	@ApiModelProperty(required = true, value = "银行账号")
	@Pattern(regexp = "(\\d|\\s){10,25}", message = "银行卡格式错误")
	private String bankCardNo;
	/**
	 * 开户行
	 */
	@TableField("open_bank")
	@ApiModelProperty(required = true, value = "开户行")
	@Length(max = 50)
	private String openBank;
	/**
	 * 开户支行
	 */
	@TableField("open_sub_bank")
	@ApiModelProperty(required = true, value = "开户支行")
	@Length(max = 50)
	private String openSubBank;
	/**
	 * 银行卡归属地省
	 */
	@TableField("bank_province")
	@ApiModelProperty(required = true, value = "银行卡归属地省")
	@Length(max = 50)
	private String bankProvince;
	/**
	 * 银行卡归属地市
	 */
	@TableField("bank_city")
	@ApiModelProperty(required = true, value = "银行卡归属地市")
	@Length(max = 50)
	private String bankCity;
	/**
	 * 银行卡预留手机号码
	 */
	@TableField("phone_number")
	@ApiModelProperty(required = true, value = "银行卡预留手机号码")
	@Pattern(regexp = "^((\\+86)|(86))?(1)\\d{10}$", message = "格式错误")
	private String phoneNumber;
	/**
	 * 可用余额
	 */
	@TableField("account_amount")
	@ApiModelProperty(required = true, value = "可用余额")
	@Min(0)
	private BigDecimal accountAmount;
	/**
	 * 冻结金额
	 */
	@TableField("frozen_amount")
	@ApiModelProperty(required = true, value = "冻结金额")
	@Min(0)
	private BigDecimal frozenAmount;
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
	 * 状态,1:可用,0:不可用
	 */
	@TableField("status_flag")
	@ApiModelProperty(required = true, value = "状态,1:可用,0:不可用")
	@Min(0)
	private Integer statusFlag;
	/**
	 * 备注
	 */
	@ApiModelProperty(required = true, value = "备注")
	@Length(max = 200)
	private String remark;

	/**
	 * 已注册存管,1.是,0:否
	 */
	@TableField("cunguan_flag")
	@ApiModelProperty(required = true, value = "已注册存管,1.是,0:否")
	@Min(0)
	private Integer cunguanFlag;

	public String getAccountBankId() {
		return accountBankId;
	}

	public void setAccountBankId(String accountBankId) {
		this.accountBankId = accountBankId;
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

		this.bankCardNo = bankCardNo;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getOpenSubBank() {
		return openSubBank;
	}

	public void setOpenSubBank(String openSubBank) {
		this.openSubBank = openSubBank;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public Integer getCunguanFlag() {
		return cunguanFlag;
	}

	public void setCunguanFlag(Integer cunguanFlag) {
		this.cunguanFlag = cunguanFlag;
	}

	@Override
	protected Serializable pkVal() {
		return this.accountBankId;
	}

	@Override
	public String toString() {
		return "AccountBankCard{" + ", accountBankId=" + accountBankId + ", accountId=" + accountId + ", bankCardNo="
				+ bankCardNo + ", openBank=" + openBank + ", openSubBank=" + openSubBank + ", bankProvince="
				+ bankProvince + ", bankCity=" + bankCity + ", phoneNumber=" + phoneNumber + ", accountAmount="
				+ accountAmount + ", frozenAmount=" + frozenAmount + ", createUser=" + createUser + ", createTime="
				+ createTime + ", updateUser=" + updateUser + ", updateTime=" + updateTime + ", statusFlag="
				+ statusFlag + ", remark=" + remark  + ", cunguanFlag=" + cunguanFlag + "}";
	}
}
