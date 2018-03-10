package com.hongte.alms.base.vo.module.api;

public class RepayDetailResultRespData {

	private String planDetailId;// 应还项目明细ID(主键)
	private String planListId;// 所属还款计划列表ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
	private String businessId;// 还款计划所属业务ID(若当前业务为展期，则存展期业务编号)
	private String period;// 所属期数
	private String planAmount;// 项目计划应还总金额(元)
	private String planRate;// 项目计划应还比例(%)，如0.5%则存0.5，可空
	private String feeId;// 资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
	private String planItemName;// 应还项目名称
	private String planItemType;// 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
	private String accountStatus;// 分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户
	private String factAmount;// 实还金额(元)
	private String repaySource;// 还款来源，10：线下转账，20：线下代扣，30：银行代扣
	private String factRepayDetailDate;// 实还日期
	private String createDate;// 创建日期
	private String createUser;// 创建用户
	private String updateDate;// 更新日期
	private String updateUser;// 更新用户

	public String getPlanDetailId() {
		return planDetailId;
	}

	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
	}

	public String getPlanRate() {
		return planRate;
	}

	public void setPlanRate(String planRate) {
		this.planRate = planRate;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getPlanItemName() {
		return planItemName;
	}

	public void setPlanItemName(String planItemName) {
		this.planItemName = planItemName;
	}

	public String getPlanItemType() {
		return planItemType;
	}

	public void setPlanItemType(String planItemType) {
		this.planItemType = planItemType;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(String factAmount) {
		this.factAmount = factAmount;
	}

	public String getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(String repaySource) {
		this.repaySource = repaySource;
	}

	public String getFactRepayDetailDate() {
		return factRepayDetailDate;
	}

	public void setFactRepayDetailDate(String factRepayDetailDate) {
		this.factRepayDetailDate = factRepayDetailDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
