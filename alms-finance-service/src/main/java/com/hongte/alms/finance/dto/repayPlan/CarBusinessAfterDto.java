package com.hongte.alms.finance.dto.repayPlan;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 贷后管理主表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-11
 */
@ApiModel
@TableName("tb_car_business_after")
public class CarBusinessAfterDto extends Model<CarBusinessAfterDto> {

    private static final long serialVersionUID = 1L;


	@ApiModelProperty(required= true,value = "信贷还款计划详情")
	private List<CarBusinessAfterDetailDto> carBizDetailDtos;

    /**
     * 业务编号
     */
    @TableId("car_business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String carBusinessId;
    /**
     * 当前还款期数
     */
	@TableField("car_business_after_id")
	@ApiModelProperty(required= true,value = "当前还款期数")
	private String carBusinessAfterId;
    /**
     * 业务类型
     */
	@ApiModelProperty(required= true,value = "业务类型")
	private String paratype;
    /**
     * 弃用
     */
	@TableField("Customer_Name")
	@ApiModelProperty(required= true,value = "弃用")
	private String CustomerName;
    /**
     * 业务主办人
     */
	@TableField("operator_name")
	@ApiModelProperty(required= true,value = "业务主办人")
	private String operatorName;
    /**
     * 业务主办人部门
     */
	@TableField("operator_dept")
	@ApiModelProperty(required= true,value = "业务主办人部门")
	private String operatorDept;
    /**
     * 新建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "新建时间")
	private Date createTime;
    /**
     * 修改时间
     */
	@TableField("modify_time")
	@ApiModelProperty(required= true,value = "修改时间")
	private Date modifyTime;
    /**
     * 是否删除
     */
	@TableField("is_delete")
	@ApiModelProperty(required= true,value = "是否删除")
	private Boolean isDelete;
    /**
     * 还款方式
     */
	@TableField("repayment_type")
	@ApiModelProperty(required= true,value = "还款方式")
	private String repaymentType;
    /**
     * 借款金额
     */
	@TableField("borrow_money")
	@ApiModelProperty(required= true,value = "借款金额")
	private String borrowMoney;
    /**
     * 剩余本金
     */
	@ApiModelProperty(required= true,value = "剩余本金")
	private String oddcorpus;
    /**
     * installment_num_date
     */
	@TableField("installment_num_date")
	@ApiModelProperty(required= true,value = "installment_num_date")
	private String installmentNumDate;
    /**
     * installment_num
     */
	@TableField("installment_num")
	@ApiModelProperty(required= true,value = "installment_num")
	private String installmentNum;
    /**
     * 本期应还本金
     */
	@TableField("current_Principa")
	@ApiModelProperty(required= true,value = "本期应还本金")
	private BigDecimal currentPrincipa;
    /**
     * 本期应还利息
     */
	@TableField("current_accrual")
	@ApiModelProperty(required= true,value = "本期应还利息")
	private String currentAccrual;
    /**
     * 罚息应收
     */
	@TableField("punitive_rate_should")
	@ApiModelProperty(required= true,value = "罚息应收")
	private String punitiveRateShould;
    /**
     * 罚息实收
     */
	@TableField("punitive_rate_fact")
	@ApiModelProperty(required= true,value = "罚息实收")
	private String punitiveRateFact;
    /**
     * 逾期天数
     */
	@ApiModelProperty(required= true,value = "逾期天数")
	private String overdueDays;
    /**
     * 还款日期
     */
	@TableField("borrow_date")
	@ApiModelProperty(required= true,value = "还款日期")
	private Date borrowDate;
    /**
     * 还款状态分类：还款中，已还款，逾期
     */
	@TableField("car_business_after_type")
	@ApiModelProperty(required= true,value = "还款状态分类：还款中，已还款，逾期")
	private String carBusinessAfterType;
    /**
     * 展期业务编号
     */
	@TableField("car_business_after_defer")
	@ApiModelProperty(required= true,value = "展期业务编号")
	private String carBusinessAfterDefer;
    /**
     * 是否展期
     */
	@TableField("is_defer")
	@ApiModelProperty(required= true,value = "是否展期")
	private Boolean isDefer;
    /**
     * 当前展期期数
     */
	@TableField("car_business_defer_id")
	@ApiModelProperty(required= true,value = "当前展期期数")
	private String carBusinessDeferId;
    /**
     * 其他费用
     */
	@TableField("other_money")
	@ApiModelProperty(required= true,value = "其他费用")
	private String otherMoney;
    /**
     * 应收款调整实收款
     */
	@TableField("fact_money")
	@ApiModelProperty(required= true,value = "应收款调整实收款")
	private String factMoney;
    /**
     * 应收款调整律师费
     */
	@TableField("lawyer_money")
	@ApiModelProperty(required= true,value = "应收款调整律师费")
	private String lawyerMoney;
    /**
     * 新增时间
     */
	@ApiModelProperty(required= true,value = "新增时间")
	private Date createdate;
    /**
     * 更新时间
     */
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateDate;
    /**
     * 是否催款
     */
	@TableField("is_collection")
	@ApiModelProperty(required= true,value = "是否催款")
	private Boolean isCollection;
    /**
     * 已还款类型0:还款中1:财务确认已还款 2:自动匹配已还款 3:财务确认全部结清,4:财务代扣已还款,5:自动代扣已还款,6:标识展期已还款,7:当期部分已还款,8:用户APP主动还款,9:代扣全部结清
     */
	@TableField("repayed_flag")
	@ApiModelProperty(required= true,value = "已还款类型0:还款中1:财务确认已还款 2:自动匹配已还款 3:财务确认全部结清,4:财务代扣已还款,5:自动代扣已还款,6:标识展期已还款,7:当期部分已还款,8:用户APP主动还款,9:代扣全部结清")
	private Integer repayedFlag;
    /**
     * 还款状态还款中,逾期,还款待确认,还款登记被财务拒绝,催款中,已拖车登记,已移交法务,已还款,坏账,退租申请,全部结清
     */
	@TableField("reserve_2")
	@ApiModelProperty(required= true,value = "还款状态还款中,逾期,还款待确认,还款登记被财务拒绝,催款中,已拖车登记,已移交法务,已还款,坏账,退租申请,全部结清")
	private String reserve2;
    /**
     * 还款登记拒绝原因
     */
	@TableField("reserve_3")
	@ApiModelProperty(required= true,value = "还款登记拒绝原因")
	private String reserve3;
    /**
     * reserve_4
     */
	@TableField("reserve_4")
	@ApiModelProperty(required= true,value = "reserve_4")
	private String reserve4;
    /**
     * 应收实收差额 线上业务调团贷网还款接口将此差额加到本金中
     */
	@TableField("reserve_5")
	@ApiModelProperty(required= true,value = "应收实收差额 线上业务调团贷网还款接口将此差额加到本金中")
	private String reserve5;
    /**
     * 修改人
     */
	@ApiModelProperty(required= true,value = "修改人")
	private String ModifyUser;
    /**
     * 流程子节点顺序
     */
	@ApiModelProperty(required= true,value = "流程子节点顺序")
	private String workflowstatus;
    /**
     * 流程子节点名称
     */
	@TableField("reserve_6")
	@ApiModelProperty(required= true,value = "流程子节点名称")
	private String reserve6;
    /**
     * reserve_7
     */
	@TableField("reserve_7")
	@ApiModelProperty(required= true,value = "reserve_7")
	private String reserve7;
    /**
     * reserve_8
     */
	@TableField("reserve_8")
	@ApiModelProperty(required= true,value = "reserve_8")
	private String reserve8;
    /**
     * 贷后状态颜色
     */
	@TableField("reserve_9")
	@ApiModelProperty(required= true,value = "贷后状态颜色")
	private String reserve9;
    /**
     * reserve_10
     */
	@TableField("reserve_10")
	@ApiModelProperty(required= true,value = "reserve_10")
	private String reserve10;
    /**
     * TrackRecord
     */
	@ApiModelProperty(required= true,value = "TrackRecord")
	private String TrackRecord;
    /**
     * 催款日期
     */
	@ApiModelProperty(required= true,value = "催款日期")
	private Date collectionDate;
    /**
     * bad_debt_mark
     */
	@TableField("bad_debt_mark")
	@ApiModelProperty(required= true,value = "bad_debt_mark")
	private String badDebtMark;
    /**
     * 结清备注
     */
	@TableField("settle_mark")
	@ApiModelProperty(required= true,value = "结清备注")
	private String settleMark;
    /**
     * fact_replayMoney
     */
	@TableField("fact_replayMoney")
	@ApiModelProperty(required= true,value = "fact_replayMoney")
	private String factReplayMoney;
    /**
     * 实际还款日期
     */
	@TableField("fatct_replayDate")
	@ApiModelProperty(required= true,value = "实际还款日期")
	private Date fatctReplayDate;
    /**
     * 实际归还本金
     */
	@TableField("fact_principa")
	@ApiModelProperty(required= true,value = "实际归还本金")
	private String factPrincipa;
    /**
     * 实际归还利息
     */
	@TableField("fact_accrual")
	@ApiModelProperty(required= true,value = "实际归还利息")
	private String factAccrual;
    /**
     * 实收滞纳金
     */
	@TableField("overdue_money")
	@ApiModelProperty(required= true,value = "实收滞纳金")
	private String overdueMoney;
    /**
     * 本期应收滞纳金，每天零点由系统自动计算
     */
	@TableField("current_Breach")
	@ApiModelProperty(required= true,value = "本期应收滞纳金，每天零点由系统自动计算")
	private String currentBreach;
    /**
     * 本期应还咨询服务费
     */
	@TableField("repay_service")
	@ApiModelProperty(required= true,value = "本期应还咨询服务费")
	private BigDecimal repayService;
    /**
     * 实际还款服务费
     */
	@TableField("fact_service")
	@ApiModelProperty(required= true,value = "实际还款服务费")
	private BigDecimal factService;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 支付方式
     */
	@TableField("payment_type")
	@ApiModelProperty(required= true,value = "支付方式")
	private String paymentType;
    /**
     * 计划其他费用
     */
	@TableField("current_other_money")
	@ApiModelProperty(required= true,value = "计划其他费用")
	private BigDecimal currentOtherMoney;
    /**
     * 财务还款金额确认(1:已确认,0:未确认)
     */
	@TableField("confirm_flag")
	@ApiModelProperty(required= true,value = "财务还款金额确认(1:已确认,0:未确认)")
	private Integer confirmFlag;
    /**
     * 电话催收人
     */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "电话催收人")
	private String collectionUser;
    /**
     * 电催分配备注
     */
	@TableField("collection_remark")
	@ApiModelProperty(required= true,value = "电催分配备注")
	private String collectionRemark;
    /**
     * 出款编号
     */
	@TableField("out_id")
	@ApiModelProperty(required= true,value = "出款编号")
	private Integer outId;
    /**
     * 是否亏损结清 空或者0：不是亏损结清 1:是亏损结清
     */
	@TableField("is_loss_settle")
	@ApiModelProperty(required= true,value = "是否亏损结清 空或者0：不是亏损结清 1:是亏损结清")
	private Integer isLossSettle;
    /**
     * 财务还款确认日期
     */
	@TableField("finance_confirmed_date")
	@ApiModelProperty(required= true,value = "财务还款确认日期")
	private Date financeConfirmedDate;
    /**
     * 财务还款确认人ID
     */
	@TableField("finance_confirmed_user")
	@ApiModelProperty(required= true,value = "财务还款确认人ID")
	private String financeConfirmedUser;
    /**
     * 财务确认自动代扣日期
     */
	@TableField("auto_withholding_confirmed_date")
	@ApiModelProperty(required= true,value = "财务确认自动代扣日期")
	private Date autoWithholdingConfirmedDate;
    /**
     * 确认自动代扣的确认者ID
     */
	@TableField("auto_withholding_confirmed_user")
	@ApiModelProperty(required= true,value = "确认自动代扣的确认者ID")
	private String autoWithholdingConfirmedUser;
    /**
     * 财务还款确认的时候选择的还款银行
     */
	@TableField("finance_bank_id")
	@ApiModelProperty(required= true,value = "财务还款确认的时候选择的还款银行")
	private Integer financeBankId;
    /**
     * 会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
     */
	@TableField("accountant_confirm_status")
	@ApiModelProperty(required= true,value = "会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;")
	private Integer accountantConfirmStatus;
    /**
     * 会计确认人
     */
	@TableField("accountant_confirm_user")
	@ApiModelProperty(required= true,value = "会计确认人")
	private String accountantConfirmUser;
    /**
     * 会计确认日期
     */
	@TableField("accountant_confirm_date")
	@ApiModelProperty(required= true,value = "会计确认日期")
	private Date accountantConfirmDate;
    /**
     * null或0：未执行垫付操作，1:本期平台垫付已结清，2：本期平台垫付未结清，3：本期不需要还垫付
     */
	@TableField("tuandai_advance_status")
	@ApiModelProperty(required= true,value = "null或0：未执行垫付操作，1:本期平台垫付已结清，2：本期平台垫付未结清，3：本期不需要还垫付")
	private Integer tuandaiAdvanceStatus;
    /**
     * null或0：未执行分润操作，1:本期分润已还清，2：本期分润未结清，3：本期不需要还分润
     */
	@TableField("tuandai_profit_status")
	@ApiModelProperty(required= true,value = "null或0：未执行分润操作，1:本期分润已还清，2：本期分润未结清，3：本期不需要还分润")
	private Integer tuandaiProfitStatus;
    /**
     * 资金充值状态，null或0:资金未充值，1:所有标资金充值成功或自动充值成功，2：资金充值处理中，3：所有标资金充值失败，4：资金部分标充值成功
     */
	@TableField("tuandai_distribute_fund_status")
	@ApiModelProperty(required= true,value = "资金充值状态，null或0:资金未充值，1:所有标资金充值成功或自动充值成功，2：资金充值处理中，3：所有标资金充值失败，4：资金部分标充值成功")
	private Integer tuandaiDistributeFundStatus;
    /**
     * 资金分发备注
     */
	@TableField("tuandai_distribute_fund_remark")
	@ApiModelProperty(required= true,value = "资金分发备注")
	private String tuandaiDistributeFundRemark;
    /**
     * 平台还款状态：未还款，已代偿，已还款
     */
	@TableField("issue_after_type")
	@ApiModelProperty(required= true,value = "平台还款状态：未还款，已代偿，已还款")
	private String issueAfterType;
    /**
     * 还款计划guid
     */
	@TableField("business_after_guid")
	@ApiModelProperty(required= true,value = "还款计划guid")
	private String businessAfterGuid;
    /**
     * 贷后跟踪状态:电催、催收、诉讼 
     */
	@TableField("tracking_after_type")
	@ApiModelProperty(required= true,value = "贷后跟踪状态:电催、催收、诉讼 ")
	private String trackingAfterType;
    /**
     * 是否移交法务后被退回
     */
	@TableField("legal_return_status")
	@ApiModelProperty(required= true,value = "是否移交法务后被退回")
	private Integer legalReturnStatus;
    /**
     * 本息还款状态 0:未还款 1:本息已还款 2:本期已结清
     */
	@TableField("interest_paid")
	@ApiModelProperty(required= true,value = "本息还款状态 0:未还款 1:本息已还款 2:本期已结清")
	private Integer interestPaid;


	public String getCarBusinessId() {
		return carBusinessId;
	}

	public void setCarBusinessId(String carBusinessId) {
		this.carBusinessId = carBusinessId;
	}

	public String getCarBusinessAfterId() {
		return carBusinessAfterId;
	}

	public void setCarBusinessAfterId(String carBusinessAfterId) {
		this.carBusinessAfterId = carBusinessAfterId;
	}

	public String getParatype() {
		return paratype;
	}

	public void setParatype(String paratype) {
		this.paratype = paratype;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorDept() {
		return operatorDept;
	}

	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Boolean getDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(String borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public String getOddcorpus() {
		return oddcorpus;
	}

	public void setOddcorpus(String oddcorpus) {
		this.oddcorpus = oddcorpus;
	}

	public String getInstallmentNumDate() {
		return installmentNumDate;
	}

	public void setInstallmentNumDate(String installmentNumDate) {
		this.installmentNumDate = installmentNumDate;
	}

	public String getInstallmentNum() {
		return installmentNum;
	}

	public void setInstallmentNum(String installmentNum) {
		this.installmentNum = installmentNum;
	}

	public BigDecimal getCurrentPrincipa() {
		return currentPrincipa;
	}

	public void setCurrentPrincipa(BigDecimal currentPrincipa) {
		this.currentPrincipa = currentPrincipa;
	}

	public String getCurrentAccrual() {
		return currentAccrual;
	}

	public void setCurrentAccrual(String currentAccrual) {
		this.currentAccrual = currentAccrual;
	}

	public String getPunitiveRateShould() {
		return punitiveRateShould;
	}

	public void setPunitiveRateShould(String punitiveRateShould) {
		this.punitiveRateShould = punitiveRateShould;
	}

	public String getPunitiveRateFact() {
		return punitiveRateFact;
	}

	public void setPunitiveRateFact(String punitiveRateFact) {
		this.punitiveRateFact = punitiveRateFact;
	}

	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getCarBusinessAfterType() {
		return carBusinessAfterType;
	}

	public void setCarBusinessAfterType(String carBusinessAfterType) {
		this.carBusinessAfterType = carBusinessAfterType;
	}

	public String getCarBusinessAfterDefer() {
		return carBusinessAfterDefer;
	}

	public void setCarBusinessAfterDefer(String carBusinessAfterDefer) {
		this.carBusinessAfterDefer = carBusinessAfterDefer;
	}

	public Boolean getDefer() {
		return isDefer;
	}

	public void setDefer(Boolean isDefer) {
		this.isDefer = isDefer;
	}

	public String getCarBusinessDeferId() {
		return carBusinessDeferId;
	}

	public void setCarBusinessDeferId(String carBusinessDeferId) {
		this.carBusinessDeferId = carBusinessDeferId;
	}

	public String getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(String otherMoney) {
		this.otherMoney = otherMoney;
	}

	public String getFactMoney() {
		return factMoney;
	}

	public void setFactMoney(String factMoney) {
		this.factMoney = factMoney;
	}

	public String getLawyerMoney() {
		return lawyerMoney;
	}

	public void setLawyerMoney(String lawyerMoney) {
		this.lawyerMoney = lawyerMoney;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Boolean getCollection() {
		return isCollection;
	}

	public void setCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Integer getRepayedFlag() {
		return repayedFlag;
	}

	public void setRepayedFlag(Integer repayedFlag) {
		this.repayedFlag = repayedFlag;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getModifyUser() {
		return ModifyUser;
	}

	public void setModifyUser(String ModifyUser) {
		this.ModifyUser = ModifyUser;
	}

	public String getWorkflowstatus() {
		return workflowstatus;
	}

	public void setWorkflowstatus(String workflowstatus) {
		this.workflowstatus = workflowstatus;
	}

	public String getReserve6() {
		return reserve6;
	}

	public void setReserve6(String reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve7() {
		return reserve7;
	}

	public void setReserve7(String reserve7) {
		this.reserve7 = reserve7;
	}

	public String getReserve8() {
		return reserve8;
	}

	public void setReserve8(String reserve8) {
		this.reserve8 = reserve8;
	}

	public String getReserve9() {
		return reserve9;
	}

	public void setReserve9(String reserve9) {
		this.reserve9 = reserve9;
	}

	public String getReserve10() {
		return reserve10;
	}

	public void setReserve10(String reserve10) {
		this.reserve10 = reserve10;
	}

	public String getTrackRecord() {
		return TrackRecord;
	}

	public void setTrackRecord(String TrackRecord) {
		this.TrackRecord = TrackRecord;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public String getBadDebtMark() {
		return badDebtMark;
	}

	public void setBadDebtMark(String badDebtMark) {
		this.badDebtMark = badDebtMark;
	}

	public String getSettleMark() {
		return settleMark;
	}

	public void setSettleMark(String settleMark) {
		this.settleMark = settleMark;
	}

	public String getFactReplayMoney() {
		return factReplayMoney;
	}

	public void setFactReplayMoney(String factReplayMoney) {
		this.factReplayMoney = factReplayMoney;
	}

	public Date getFatctReplayDate() {
		return fatctReplayDate;
	}

	public void setFatctReplayDate(Date fatctReplayDate) {
		this.fatctReplayDate = fatctReplayDate;
	}

	public String getFactPrincipa() {
		return factPrincipa;
	}

	public void setFactPrincipa(String factPrincipa) {
		this.factPrincipa = factPrincipa;
	}

	public String getFactAccrual() {
		return factAccrual;
	}

	public void setFactAccrual(String factAccrual) {
		this.factAccrual = factAccrual;
	}

	public String getOverdueMoney() {
		return overdueMoney;
	}

	public void setOverdueMoney(String overdueMoney) {
		this.overdueMoney = overdueMoney;
	}

	public String getCurrentBreach() {
		return currentBreach;
	}

	public void setCurrentBreach(String currentBreach) {
		this.currentBreach = currentBreach;
	}

	public BigDecimal getRepayService() {
		return repayService;
	}

	public void setRepayService(BigDecimal repayService) {
		this.repayService = repayService;
	}

	public BigDecimal getFactService() {
		return factService;
	}

	public void setFactService(BigDecimal factService) {
		this.factService = factService;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getCurrentOtherMoney() {
		return currentOtherMoney;
	}

	public void setCurrentOtherMoney(BigDecimal currentOtherMoney) {
		this.currentOtherMoney = currentOtherMoney;
	}

	public Integer getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
	}

	public String getCollectionRemark() {
		return collectionRemark;
	}

	public void setCollectionRemark(String collectionRemark) {
		this.collectionRemark = collectionRemark;
	}

	public Integer getOutId() {
		return outId;
	}

	public void setOutId(Integer outId) {
		this.outId = outId;
	}

	public Integer getIsLossSettle() {
		return isLossSettle;
	}

	public void setIsLossSettle(Integer isLossSettle) {
		this.isLossSettle = isLossSettle;
	}

	public Date getFinanceConfirmedDate() {
		return financeConfirmedDate;
	}

	public void setFinanceConfirmedDate(Date financeConfirmedDate) {
		this.financeConfirmedDate = financeConfirmedDate;
	}

	public String getFinanceConfirmedUser() {
		return financeConfirmedUser;
	}

	public void setFinanceConfirmedUser(String financeConfirmedUser) {
		this.financeConfirmedUser = financeConfirmedUser;
	}

	public Date getAutoWithholdingConfirmedDate() {
		return autoWithholdingConfirmedDate;
	}

	public void setAutoWithholdingConfirmedDate(Date autoWithholdingConfirmedDate) {
		this.autoWithholdingConfirmedDate = autoWithholdingConfirmedDate;
	}

	public String getAutoWithholdingConfirmedUser() {
		return autoWithholdingConfirmedUser;
	}

	public void setAutoWithholdingConfirmedUser(String autoWithholdingConfirmedUser) {
		this.autoWithholdingConfirmedUser = autoWithholdingConfirmedUser;
	}

	public Integer getFinanceBankId() {
		return financeBankId;
	}

	public void setFinanceBankId(Integer financeBankId) {
		this.financeBankId = financeBankId;
	}

	public Integer getAccountantConfirmStatus() {
		return accountantConfirmStatus;
	}

	public void setAccountantConfirmStatus(Integer accountantConfirmStatus) {
		this.accountantConfirmStatus = accountantConfirmStatus;
	}

	public String getAccountantConfirmUser() {
		return accountantConfirmUser;
	}

	public void setAccountantConfirmUser(String accountantConfirmUser) {
		this.accountantConfirmUser = accountantConfirmUser;
	}

	public Date getAccountantConfirmDate() {
		return accountantConfirmDate;
	}

	public void setAccountantConfirmDate(Date accountantConfirmDate) {
		this.accountantConfirmDate = accountantConfirmDate;
	}

	public Integer getTuandaiAdvanceStatus() {
		return tuandaiAdvanceStatus;
	}

	public void setTuandaiAdvanceStatus(Integer tuandaiAdvanceStatus) {
		this.tuandaiAdvanceStatus = tuandaiAdvanceStatus;
	}

	public Integer getTuandaiProfitStatus() {
		return tuandaiProfitStatus;
	}

	public void setTuandaiProfitStatus(Integer tuandaiProfitStatus) {
		this.tuandaiProfitStatus = tuandaiProfitStatus;
	}

	public Integer getTuandaiDistributeFundStatus() {
		return tuandaiDistributeFundStatus;
	}

	public void setTuandaiDistributeFundStatus(Integer tuandaiDistributeFundStatus) {
		this.tuandaiDistributeFundStatus = tuandaiDistributeFundStatus;
	}

	public String getTuandaiDistributeFundRemark() {
		return tuandaiDistributeFundRemark;
	}

	public void setTuandaiDistributeFundRemark(String tuandaiDistributeFundRemark) {
		this.tuandaiDistributeFundRemark = tuandaiDistributeFundRemark;
	}

	public String getIssueAfterType() {
		return issueAfterType;
	}

	public void setIssueAfterType(String issueAfterType) {
		this.issueAfterType = issueAfterType;
	}

	public String getBusinessAfterGuid() {
		return businessAfterGuid;
	}

	public void setBusinessAfterGuid(String businessAfterGuid) {
		this.businessAfterGuid = businessAfterGuid;
	}

	public String getTrackingAfterType() {
		return trackingAfterType;
	}

	public void setTrackingAfterType(String trackingAfterType) {
		this.trackingAfterType = trackingAfterType;
	}

	public Integer getLegalReturnStatus() {
		return legalReturnStatus;
	}

	public void setLegalReturnStatus(Integer legalReturnStatus) {
		this.legalReturnStatus = legalReturnStatus;
	}

	public Integer getInterestPaid() {
		return interestPaid;
	}

	public void setInterestPaid(Integer interestPaid) {
		this.interestPaid = interestPaid;
	}

	@Override
	protected Serializable pkVal() {
		return this.carBusinessId;
	}


	public List<CarBusinessAfterDetailDto> getCarBizDetailDtos() {
		return carBizDetailDtos;
	}

	public void setCarBizDetailDtos(List<CarBusinessAfterDetailDto> carBizDetailDtos) {
		this.carBizDetailDtos = carBizDetailDtos;
	}
}
