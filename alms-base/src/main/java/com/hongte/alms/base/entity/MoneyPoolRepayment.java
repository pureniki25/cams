package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 款项池业务关联表
 * </p>
 *
 * @author 王继光
 * @since 2018-03-02
 */
@ApiModel
@TableName("tb_money_pool_repayment")
public class MoneyPoolRepayment extends Model<MoneyPoolRepayment> {

    public MoneyPoolRepayment() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "自增主键")
	private Integer id;
    /**
     * 款项池ID，外键，对应tb_money_pool的money_pool_id
     */
	@TableField("money_pool_id")
	@ApiModelProperty(required= true,value = "款项池ID，外键，对应tb_money_pool的money_pool_id")
	private String moneyPoolId;
    /**
     * 原信贷款项池ID
     */
	@TableField("xd_pool_id")
	@ApiModelProperty(required= true,value = "原信贷款项池ID")
	private Integer xdPoolId;
    /**
     * 还款计划期数ID，外键，对应tb_repayment_biz_plan_list表的plan_list_id
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "还款计划期数ID，外键，对应tb_repayment_biz_plan_list表的plan_list_id")
	private String planListId;
    /**
     * 领取人ID
     */
	@TableField("operate_id")
	@ApiModelProperty(required= true,value = "领取人ID")
	private String operateId;
    /**
     * 领取人姓名
     */
	@TableField("operate_name")
	@ApiModelProperty(required= true,value = "领取人姓名")
	private String operateName;
    /**
     * 领取时间
     */
	@TableField("claim_date")
	@ApiModelProperty(required= true,value = "领取时间")
	private Date claimDate;
    /**
     * 状态
     */
	@ApiModelProperty(required= true,value = "状态")
	private String state;
    /**
     * 收入类型(1：收入，2：支出)
     */
	@TableField("income_type")
	@ApiModelProperty(required= true,value = "收入类型(1：收入，2：支出)")
	private Integer incomeType;
    /**
     * 转入账号
     */
	@TableField("bank_account")
	@ApiModelProperty(required= true,value = "转入账号")
	private String bankAccount;
    /**
     * 交易类型
     */
	@TableField("trade_type")
	@ApiModelProperty(required= true,value = "交易类型")
	private String tradeType;
    /**
     * 交易场所
     */
	@TableField("trade_place")
	@ApiModelProperty(required= true,value = "交易场所")
	private String tradePlace;
    /**
     * 实际转账人
     */
	@TableField("fact_transfer_name")
	@ApiModelProperty(required= true,value = "实际转账人")
	private String factTransferName;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 银行流水列表
     */
	@TableField("pool_list")
	@ApiModelProperty(required= true,value = "银行流水列表")
	private String poolList;
    /**
     * 凭证图片OSS完整地址
     */
	@TableField("certificate_picture_url")
	@ApiModelProperty(required= true,value = "凭证图片OSS完整地址")
	private String certificatePictureUrl;
    /**
     * 是否财务匹配流水，0：否，1：是
     */
	@TableField("is_finance_match")
	@ApiModelProperty(required= true,value = "是否财务匹配流水，0：否，1：是")
	private Integer isFinanceMatch;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 记录是否被删除，1已删除，0或null未删除
     */
	@TableField("is_deleted")
	@ApiModelProperty(required= true,value = "记录是否被删除，1已删除，0或null未删除")
	private Integer isDeleted;
    /**
     * 删除记录的操作人
     */
	@TableField("delete_user")
	@ApiModelProperty(required= true,value = "删除记录的操作人")
	private String deleteUser;
    /**
     * 删除时间
     */
	@TableField("delete_time")
	@ApiModelProperty(required= true,value = "删除时间")
	private Date deleteTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getOperateId() {
		return operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public Date getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(Integer incomeType) {
		this.incomeType = incomeType;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradePlace() {
		return tradePlace;
	}

	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}

	public String getFactTransferName() {
		return factTransferName;
	}

	public void setFactTransferName(String factTransferName) {
		this.factTransferName = factTransferName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPoolList() {
		return poolList;
	}

	public void setPoolList(String poolList) {
		this.poolList = poolList;
	}

	public String getCertificatePictureUrl() {
		return certificatePictureUrl;
	}

	public void setCertificatePictureUrl(String certificatePictureUrl) {
		this.certificatePictureUrl = certificatePictureUrl;
	}

	public Integer getIsFinanceMatch() {
		return isFinanceMatch;
	}

	public void setIsFinanceMatch(Integer isFinanceMatch) {
		this.isFinanceMatch = isFinanceMatch;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MoneyPoolRepayment{" +
			", id=" + id +
			", moneyPoolId=" + moneyPoolId +
			", xdPoolId=" + xdPoolId +
			", planListId=" + planListId +
			", operateId=" + operateId +
			", operateName=" + operateName +
			", claimDate=" + claimDate +
			", state=" + state +
			", incomeType=" + incomeType +
			", bankAccount=" + bankAccount +
			", tradeType=" + tradeType +
			", tradePlace=" + tradePlace +
			", factTransferName=" + factTransferName +
			", remark=" + remark +
			", poolList=" + poolList +
			", certificatePictureUrl=" + certificatePictureUrl +
			", isFinanceMatch=" + isFinanceMatch +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", isDeleted=" + isDeleted +
			", deleteUser=" + deleteUser +
			", deleteTime=" + deleteTime +
			"}";
	}

	public MoneyPoolRepayment(String moneyPoolId) {
		super();
		if (moneyPoolId==null) {
			throw new RuntimeException("moneyPoolId can't be null");
		}
		this.moneyPoolId = moneyPoolId ;
	}
	
}
