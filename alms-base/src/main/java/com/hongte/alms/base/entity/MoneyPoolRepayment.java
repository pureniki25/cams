package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;

import java.math.BigDecimal;
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
 * @since 2018-03-23
 */
@ApiModel
@TableName("tb_money_pool_repayment")
public class MoneyPoolRepayment extends Model<MoneyPoolRepayment> {

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
     * 原信贷业务款项池关联表主键ID，对应tb_money_pool_carbusiness.id
     */
	@TableField("xd_matching_id")
	@ApiModelProperty(required= true,value = "原信贷业务款项池关联表主键ID，对应tb_money_pool_carbusiness.id")
	private Integer xdMatchingId;
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
    /**
     * 记账金额(元)
     */
	@TableField("account_money")
	@ApiModelProperty(required= true,value = "记账金额(元)")
	private BigDecimal accountMoney;
    /**
     * 转入时间
     */
	@TableField("trade_date")
	@ApiModelProperty(required= true,value = "转入时间")
	private Date tradeDate;
    /**
     * 创建人角色,客户/财务
     */
	@TableField("create_user_role")
	@ApiModelProperty(required= true,value = "创建人角色,客户/财务")
	private String createUserRole;


	/**
	 * @param repayInfo
	 */
	public MoneyPoolRepayment(RepaymentRegisterInfoDTO repayInfo) {
		super();
		if (repayInfo==null) {
			throw new RuntimeException("repayInfo is null!!!");
		}
		if (repayInfo.getAcceptBank()!=null&&!repayInfo.getAcceptBank().equals("")) {
			this.bankAccount = repayInfo.getAcceptBank();
		}
		if (repayInfo.getCert()!=null&&!repayInfo.getCert().equals("")) {
			this.certificatePictureUrl = repayInfo.getCert();
		}
		if(repayInfo.getFactRepaymentUser()!=null&&!repayInfo.getFactRepaymentUser().equals("")) {
			this.factTransferName = repayInfo.getFactRepaymentUser();
		}
		if(repayInfo.getRemark()!=null&&!repayInfo.getRemark().equals("")) {
			this.remark = repayInfo.getRemark() ;
		}
		if (repayInfo.getRepaymentDate()!=null&&!repayInfo.getRepaymentDate().equals("")) {
			this.tradeDate = DateUtil.getDate(repayInfo.getRepaymentDate());
		}
		if (repayInfo.getRepaymentMoney()!=null&&!repayInfo.getRepaymentMoney().equals("")) {
			this.accountMoney = new BigDecimal(repayInfo.getRepaymentMoney());
		}
		if (repayInfo.getTradePlace()!=null&&!repayInfo.getTradePlace().equals("")) {
			this.tradePlace = repayInfo.getTradePlace();
		}
		if (repayInfo.getUserId()!=null&&!repayInfo.getUserId().equals("")) {
			this.createUser = repayInfo.getUserId();
		}
		if (repayInfo.getTradeType()!=null&&!repayInfo.getTradeType().equals("")) {
			this.tradeType = repayInfo.getTradeType();
		}
	}
	
	public void update(RepaymentRegisterInfoDTO repayInfo) {
		if (repayInfo==null) {
			throw new RuntimeException("repayInfo is null!!!");
		}
		if (this.state.equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
			throw new RuntimeException( "财务确认已还款,不可以再编辑"); 
		}
		if (this.state.equals(RepayRegisterFinanceStatus.财务指定银行流水.toString())) {
			throw new RuntimeException("财务指定银行流水,不可以再编辑"); 
		}
		
		if (repayInfo.getAcceptBank()!=null&&!repayInfo.getAcceptBank().equals("")) {
			this.bankAccount = repayInfo.getAcceptBank();
		}
		if (repayInfo.getCert()!=null&&!repayInfo.getCert().equals("")) {
			this.certificatePictureUrl = repayInfo.getCert();
		}
		if(repayInfo.getFactRepaymentUser()!=null&&!repayInfo.getFactRepaymentUser().equals("")) {
			this.factTransferName = repayInfo.getFactRepaymentUser();
		}
		if(repayInfo.getRemark()!=null) {
			this.remark = repayInfo.getRemark() ;
		}
		if (repayInfo.getRepaymentDate()!=null&&!repayInfo.getRepaymentDate().equals("")) {
			this.tradeDate = DateUtil.getDate(repayInfo.getRepaymentDate());
		}
		if (repayInfo.getRepaymentMoney()!=null&&!repayInfo.getRepaymentMoney().equals("")) {
			this.accountMoney = new BigDecimal(repayInfo.getRepaymentMoney());
		}
		if (repayInfo.getTradePlace()!=null&&!repayInfo.getTradePlace().equals("")) {
			this.tradePlace = repayInfo.getTradePlace();
		}
		if (repayInfo.getUserId()!=null&&!repayInfo.getUserId().equals("")) {
			this.createUser = repayInfo.getUserId();
		}
		if (repayInfo.getTradeType()!=null&&!repayInfo.getTradeType().equals("")) {
			this.tradeType = repayInfo.getTradeType();
		}
		
		if (this.state.equals(RepayRegisterFinanceStatus.还款登记被拒绝.toString())
				||this.state.equals(RepayRegisterFinanceStatus.还款待确认.toString())) {
			this.state = RepayRegisterFinanceStatus.未关联银行流水.toString();
		}
	}

	/**
	 * 
	 */
	public MoneyPoolRepayment() {
		// TODO Auto-generated constructor stub
	}

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

	public Integer getXdMatchingId() {
		return xdMatchingId;
	}

	public void setXdMatchingId(Integer xdMatchingId) {
		this.xdMatchingId = xdMatchingId;
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

	public BigDecimal getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getCreateUserRole() {
		return createUserRole;
	}

	public void setCreateUserRole(String createUserRole) {
		this.createUserRole = createUserRole;
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
			", xdMatchingId=" + xdMatchingId +
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
			", accountMoney=" + accountMoney +
			", tradeDate=" + tradeDate +
			", createUserRole=" + createUserRole +
			"}";
	}
}
