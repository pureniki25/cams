package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 线下还款账户配置表
 * </p>
 *
 * @author 王继光
 * @since 2018-03-05
 */
@ApiModel
@TableName("tb_department_bank")
public class DepartmentBank extends Model<DepartmentBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,guid
     */
    @TableId("account_id")
	@ApiModelProperty(required= true,value = "主键,guid")
	private String accountId;
    /**
     * 信贷账户ID
     */
	@TableField("xd_bank_id")
	@ApiModelProperty(required= true,value = "信贷账户ID")
	private Integer xdBankId;
    /**
     * 分公司id
     */
	@TableField("dept_id")
	@ApiModelProperty(required= true,value = "分公司id")
	private String deptId;
    /**
     * 线下还款账户名
     */
	@TableField("repayment_name")
	@ApiModelProperty(required= true,value = "线下还款账户名")
	private String repaymentName;
    /**
     * 线下还款账户号
     */
	@TableField("repayment_id")
	@ApiModelProperty(required= true,value = "线下还款账户号")
	private String repaymentId;
    /**
     * 线下还款账户所属开户行
     */
	@TableField("repayment_bank")
	@ApiModelProperty(required= true,value = "线下还款账户所属开户行")
	private String repaymentBank;
    /**
     * 线下还款账户支行名称
     */
	@TableField("repayment_sub_bank")
	@ApiModelProperty(required= true,value = "线下还款账户支行名称")
	private String repaymentSubBank;
    /**
     * 出借人
     */
	@ApiModelProperty(required= true,value = "出借人")
	private String lender;
    /**
     * 财务导入流水账号名
     */
	@TableField("finance_name")
	@ApiModelProperty(required= true,value = "财务导入流水账号名")
	private String financeName;
    /**
     * 账户主体编号,个人是身份证,企业是统一信用编码或者营业执照
     */
	@TableField("main_id")
	@ApiModelProperty(required= true,value = "账户主体编号,个人是身份证,企业是统一信用编码或者营业执照")
	private String mainId;
    /**
     * 账户主体类型,1;个人,2:企业
     */
	@TableField("main_type")
	@ApiModelProperty(required= true,value = "账户主体类型,1;个人,2:企业")
	private Integer mainType;
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
     * 银行卡预留手机号码
     */
	@TableField("phone_number")
	@ApiModelProperty(required= true,value = "银行卡预留手机号码")
	private String phoneNumber;
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
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;


	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getXdBankId() {
		return xdBankId;
	}

	public void setXdBankId(Integer xdBankId) {
		this.xdBankId = xdBankId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRepaymentName() {
		return repaymentName;
	}

	public void setRepaymentName(String repaymentName) {
		this.repaymentName = repaymentName;
	}

	public String getRepaymentId() {
		return repaymentId;
	}

	public void setRepaymentId(String repaymentId) {
		this.repaymentId = repaymentId;
	}

	public String getRepaymentBank() {
		return repaymentBank;
	}

	public void setRepaymentBank(String repaymentBank) {
		this.repaymentBank = repaymentBank;
	}

	public String getRepaymentSubBank() {
		return repaymentSubBank;
	}

	public void setRepaymentSubBank(String repaymentSubBank) {
		this.repaymentSubBank = repaymentSubBank;
	}

	public String getLender() {
		return lender;
	}

	public void setLender(String lender) {
		this.lender = lender;
	}

	public String getFinanceName() {
		return financeName;
	}

	public void setFinanceName(String financeName) {
		this.financeName = financeName;
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

	@Override
	protected Serializable pkVal() {
		return this.accountId;
	}

	@Override
	public String toString() {
		return "DepartmentBank{" +
			", accountId=" + accountId +
			", xdBankId=" + xdBankId +
			", deptId=" + deptId +
			", repaymentName=" + repaymentName +
			", repaymentId=" + repaymentId +
			", repaymentBank=" + repaymentBank +
			", repaymentSubBank=" + repaymentSubBank +
			", lender=" + lender +
			", financeName=" + financeName +
			", mainId=" + mainId +
			", mainType=" + mainType +
			", bankProvince=" + bankProvince +
			", bankCity=" + bankCity +
			", phoneNumber=" + phoneNumber +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
