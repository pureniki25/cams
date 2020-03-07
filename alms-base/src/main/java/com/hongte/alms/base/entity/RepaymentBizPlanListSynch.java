package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 业务还款计划列表
 * </p>
 *
 * @author 刘正全
 * @since 2018-11-06
 */
@ApiModel
@TableName("tb_repayment_biz_plan_list_synch")
public class RepaymentBizPlanListSynch extends Model<RepaymentBizPlanListSynch> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款计划列表ID(主键)
     */
    @TableId("plan_list_id")
	@ApiModelProperty(required= true,value = "还款计划列表ID(主键)")
	private String planListId;
    /**
     * 所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
     */
	@TableField("plan_id")
	@ApiModelProperty(required= true,value = "所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)")
	private String planId;
    /**
     * 还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "还款计划所属业务编号(若当前业务为展期，则存展期业务编号)")
	private String businessId;
    /**
     * 还款计划所属原业务编号
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "还款计划所属原业务编号")
	private String origBusinessId;
    /**
     * 当前还款计划期数，若期数为0，表示为展期还款计划第0期或者线下出款业务的第0期
     */
	@ApiModelProperty(required= true,value = "当前还款计划期数，若期数为0，表示为展期还款计划第0期或者线下出款业务的第0期")
	private Integer period;
    /**
     * 总批次期数，唯一，对应信贷系统的还款计划编号
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "总批次期数，唯一，对应信贷系统的还款计划编号")
	private String afterId;
    /**
     * 应还日期
     */
	@TableField("due_date")
	@ApiModelProperty(required= true,value = "应还日期")
	private Date dueDate;
    /**
     * 总计划应还金额(元)，不含滞纳金
     */
	@TableField("total_borrow_amount")
	@ApiModelProperty(required= true,value = "总计划应还金额(元)，不含滞纳金")
	private BigDecimal totalBorrowAmount;
    /**
     * 总应还滞纳金(元)，每天零点由系统自动计算
     */
	@TableField("overdue_amount")
	@ApiModelProperty(required= true,value = "总应还滞纳金(元)，每天零点由系统自动计算")
	private BigDecimal overdueAmount;
    /**
     * 逾期天数，每天零点由系统自动计算
     */
	@TableField("overdue_days")
	@ApiModelProperty(required= true,value = "逾期天数，每天零点由系统自动计算")
	private BigDecimal overdueDays;
    /**
     * 当前还款状态，目前只有三种，分别为"还款中"，"逾期"，"已还款"
     */
	@TableField("current_status")
	@ApiModelProperty(required= true,value = "当前还款状态，目前只有三种，分别为还款中，逾期，已还款")
	private String currentStatus;
    /**
     * 当前还款子状态
     */
	@TableField("current_sub_status")
	@ApiModelProperty(required= true,value = "当前还款子状态")
	private String currentSubStatus;
    /**
     * 部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
     */
	@TableField("repay_status")
	@ApiModelProperty(required= true,value = "部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款")
	private Integer repayStatus;
    /**
     * 已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清
     */
	@TableField("repay_flag")
	@ApiModelProperty(required= true,value = "已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清")
	private Integer repayFlag;
    /**
     * 客户实还日期
     */
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "客户实还日期")
	private Date factRepayDate;
    /**
     * 财务确认还款操作日期
     */
	@TableField("finance_comfirm_date")
	@ApiModelProperty(required= true,value = "财务确认还款操作日期")
	private Date financeComfirmDate;
    /**
     * 财务还款确认人ID
     */
	@TableField("finance_confirm_user")
	@ApiModelProperty(required= true,value = "财务还款确认人ID")
	private String financeConfirmUser;
    /**
     * 财务还款确认人名称
     */
	@TableField("finance_confirm_user_name")
	@ApiModelProperty(required= true,value = "财务还款确认人名称")
	private String financeConfirmUserName;
    /**
     * 财务还款金额确认(1:已确认,0:未确认)
     */
	@TableField("confirm_flag")
	@ApiModelProperty(required= true,value = "财务还款金额确认(1:已确认,0:未确认)")
	private Integer confirmFlag;
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
     * 确认自动代扣的确认者姓名
     */
	@TableField("auto_withholding_confirmed_user_name")
	@ApiModelProperty(required= true,value = "确认自动代扣的确认者姓名")
	private String autoWithholdingConfirmedUserName;
    /**
     * 会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;
     */
	@TableField("accountant_confirm_status")
	@ApiModelProperty(required= true,value = "会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;")
	private Integer accountantConfirmStatus;
    /**
     * 会计确认人ID
     */
	@TableField("accountant_confirm_user")
	@ApiModelProperty(required= true,value = "会计确认人ID")
	private String accountantConfirmUser;
    /**
     * 会计确认人姓名
     */
	@TableField("accountant_confirm_user_name")
	@ApiModelProperty(required= true,value = "会计确认人姓名")
	private String accountantConfirmUserName;
    /**
     * 会计确认日期
     */
	@TableField("accountant_confirm_date")
	@ApiModelProperty(required= true,value = "会计确认日期")
	private Date accountantConfirmDate;
    /**
     * 减免金额
     */
	@TableField("derate_amount")
	@ApiModelProperty(required= true,value = "减免金额")
	private BigDecimal derateAmount;
    /**
     * 还款备注
     */
	@ApiModelProperty(required= true,value = "还款备注")
	private String remark;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
    /**
     * 来源类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("src_type")
	@ApiModelProperty(required= true,value = "来源类型：1.信贷生成，2.贷后管理生成")
	private Integer srcType;
    /**
     * tb_basic_business表资产端业务编号
     */
	@TableField("business_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表资产端业务编号")
	private String businessIdExt;
    /**
     * tb_basic_business业务表表地区id
     */
	@TableField("district_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business业务表表地区id")
	private String districtIdExt;
    /**
     * tb_basic_business表业务地区名
     */
	@TableField("district_name_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表业务地区名")
	private String districtNameExt;
    /**
     * tb_basic_business表公司id
     */
	@TableField("company_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表公司id")
	private String companyIdExt;
    /**
     * tb_basic_business表公司名
     */
	@TableField("company_name_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表公司名")
	private String companyNameExt;
    /**
     * tb_basic_business表业务主办人ID
     */
	@TableField("operator_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表业务主办人ID")
	private String operatorIdExt;
    /**
     * tb_basic_business表业务主办人姓名
     */
	@TableField("operator_name_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表业务主办人姓名")
	private String operatorNameExt;
    /**
     * tb_basic_business表主借款人的客户姓名
     */
	@TableField("customer_name_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表主借款人的客户姓名")
	private String customerNameExt;
    /**
     * tb_basic_business表业务类型ID(对应tb_basic_business_type的ID)
     */
	@TableField("business_type_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表业务类型ID(对应tb_basic_business_type的ID)")
	private Integer businessTypeExt;
    /**
     * tb_basic_business_type业务类型名称
     */
	@TableField("business_type_name_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business_type业务类型名称")
	private String businessTypeNameExt;
    /**
     * tb_basic_business表借款金额(元)
     */
	@TableField("borrow_money_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business表借款金额(元)")
	private BigDecimal borrowMoneyExt;
    /**
     * 逾期天数
     */
	@TableField("delay_days_ext")
	@ApiModelProperty(required= true,value = "逾期天数")
	private Integer delayDaysExt;
    /**
     * tb_collection_status贷后催收表电催专员用户id
     */
	@TableField("phone_staff_ext")
	@ApiModelProperty(required= true,value = "tb_collection_status贷后催收表电催专员用户id")
	private String phoneStaffExt;
    /**
     * tb_collection_status贷后催收表催收专员用户id
     */
	@TableField("visit_staff_ext")
	@ApiModelProperty(required= true,value = "tb_collection_status贷后催收表催收专员用户id")
	private String visitStaffExt;
    /**
     * 还款状态
     */
	@TableField("status_name_ext")
	@ApiModelProperty(required= true,value = "还款状态")
	private String statusNameExt;
    /**
     * tb_repayment_biz_plan表还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期
     */
	@TableField("plan_status_ext")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan表还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期")
	private Integer planStatusExt;
    /**
     * tb_collection_status表贷后状态，1：电催，50：催收中，100：已移交法务，200：已关闭
     */
	@TableField("collection_status_ext")
	@ApiModelProperty(required= true,value = "tb_collection_status表贷后状态，1：电催，50：催收中，100：已移交法务，200：已关闭")
	private Integer collectionStatusExt;
    /**
     * tb_repayment_biz_plan生成还款计划对应的借款期限
     */
	@TableField("borrow_limit_ext")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan生成还款计划对应的借款期限")
	private Integer borrowLimitExt;
    /**
     * tb_basic_business还款方式id还款方式ID，1：到期还本息，2：每月付息到期还本，4：等本等息，5：等额本息，9：分期还本付息
     */
	@TableField("repayment_type_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business还款方式id还款方式ID，1：到期还本息，2：每月付息到期还本，4：等本等息，5：等额本息，9：分期还本付息")
	private Integer repaymentTypeIdExt;
    /**
     * tb_five_level_classify_business_change_log分类名称
     */
	@TableField("class_name_ext")
	@ApiModelProperty(required= true,value = "tb_five_level_classify_business_change_log分类名称")
	private String classNameExt;
    /**
     * tb_repayment_biz_plan 当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号，若是原业务，则为空。）
     */
	@TableField("original_business_id_ext")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan 当前还款计划对应的原业务编号（若为展期还款计划，则存展期业务对应的原业务编号，若是原业务，则为空。）")
	private String originalBusinessIdExt;
    /**
     * tb_basic_business_customer[客户手机号码，当客户类型为企业时，此字段保存联系人手机号码]
     */
	@TableField("phone_number_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business_customer[客户手机号码，当客户类型为企业时，此字段保存联系人手机号码]")
	private String phoneNumberExt;
    /**
     * tb_basic_business业务所属子类型，若无则为空
     */
	@TableField("business_ctype_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business业务所属子类型，若无则为空")
	private String businessCtypeExt;
    /**
     * tb_repayment_biz_plan_list_detail实还金额(元)
     */
	@TableField("fact_amount_ext")
	@ApiModelProperty(required= true,value = "tb_repayment_biz_plan_list_detail实还金额(元)")
	private BigDecimal factAmountExt;
    /**
     * tb_basic_company父级区域ID
     */
	@TableField("area_pid_ext")
	@ApiModelProperty(required= true,value = "tb_basic_company父级区域ID")
	private String areaPidExt;
    /**
     * tb_basic_company资产端区域ID
     */
	@TableField("area_id_ext")
	@ApiModelProperty(required= true,value = "tb_basic_company资产端区域ID")
	private String areaIdExt;
    /**
     * tb_tuandai_project_info真实姓名
     */
	@TableField("real_name_ext")
	@ApiModelProperty(required= true,value = "tb_tuandai_project_info真实姓名")
	private String realNameExt;
    /**
     * tb_money_pool_repayment转入时间
     */
	@TableField("trade_date_ext")
	@ApiModelProperty(required= true,value = "tb_money_pool_repayment转入时间")
	private Date tradeDateExt;
    /**
     * 是否支持代扣描述
     */
	@TableField("can_withhold_desc_ext")
	@ApiModelProperty(required= true,value = "是否支持代扣描述")
	private String canWithholdDescExt;
    /**
     * tb_basic_business_customer证件号
     */
	@TableField("identify_card_ext")
	@ApiModelProperty(required= true,value = "tb_basic_business_customer证件号")
	private String identifyCardExt;
    /**
     * 是否展期还款计划,0:否，1:是
     */
	@TableField("is_defer_ext")
	@ApiModelProperty(required= true,value = "是否展期还款计划,0:否，1:是")
	private Integer isDeferExt;
    /**
     * 还款登记状态 null或0：未登记 ，1：部分登记，2：线上部分足额登记, 3:全款足额登记
     */
	@TableField("register_status")
	@ApiModelProperty(required= true,value = "还款登记状态 null或0：未登记 ，1：部分登记，2：线上部分足额登记, 3:全款足额登记")
	private Integer registerStatus;
    /**
     * 最后一次登记还款时间
     */
	@TableField("register_time")
	@ApiModelProperty(required= true,value = "最后一次登记还款时间")
	private Date registerTime;


	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotalBorrowAmount() {
		return totalBorrowAmount;
	}

	public void setTotalBorrowAmount(BigDecimal totalBorrowAmount) {
		this.totalBorrowAmount = totalBorrowAmount;
	}

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public BigDecimal getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(BigDecimal overdueDays) {
		this.overdueDays = overdueDays;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentSubStatus() {
		return currentSubStatus;
	}

	public void setCurrentSubStatus(String currentSubStatus) {
		this.currentSubStatus = currentSubStatus;
	}

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}

	public Integer getRepayFlag() {
		return repayFlag;
	}

	public void setRepayFlag(Integer repayFlag) {
		this.repayFlag = repayFlag;
	}

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public Date getFinanceComfirmDate() {
		return financeComfirmDate;
	}

	public void setFinanceComfirmDate(Date financeComfirmDate) {
		this.financeComfirmDate = financeComfirmDate;
	}

	public String getFinanceConfirmUser() {
		return financeConfirmUser;
	}

	public void setFinanceConfirmUser(String financeConfirmUser) {
		this.financeConfirmUser = financeConfirmUser;
	}

	public String getFinanceConfirmUserName() {
		return financeConfirmUserName;
	}

	public void setFinanceConfirmUserName(String financeConfirmUserName) {
		this.financeConfirmUserName = financeConfirmUserName;
	}

	public Integer getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
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

	public String getAutoWithholdingConfirmedUserName() {
		return autoWithholdingConfirmedUserName;
	}

	public void setAutoWithholdingConfirmedUserName(String autoWithholdingConfirmedUserName) {
		this.autoWithholdingConfirmedUserName = autoWithholdingConfirmedUserName;
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

	public String getAccountantConfirmUserName() {
		return accountantConfirmUserName;
	}

	public void setAccountantConfirmUserName(String accountantConfirmUserName) {
		this.accountantConfirmUserName = accountantConfirmUserName;
	}

	public Date getAccountantConfirmDate() {
		return accountantConfirmDate;
	}

	public void setAccountantConfirmDate(Date accountantConfirmDate) {
		this.accountantConfirmDate = accountantConfirmDate;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getSrcType() {
		return srcType;
	}

	public void setSrcType(Integer srcType) {
		this.srcType = srcType;
	}

	public String getBusinessIdExt() {
		return businessIdExt;
	}

	public void setBusinessIdExt(String businessIdExt) {
		this.businessIdExt = businessIdExt;
	}

	public String getDistrictIdExt() {
		return districtIdExt;
	}

	public void setDistrictIdExt(String districtIdExt) {
		this.districtIdExt = districtIdExt;
	}

	public String getDistrictNameExt() {
		return districtNameExt;
	}

	public void setDistrictNameExt(String districtNameExt) {
		this.districtNameExt = districtNameExt;
	}

	public String getCompanyIdExt() {
		return companyIdExt;
	}

	public void setCompanyIdExt(String companyIdExt) {
		this.companyIdExt = companyIdExt;
	}

	public String getCompanyNameExt() {
		return companyNameExt;
	}

	public void setCompanyNameExt(String companyNameExt) {
		this.companyNameExt = companyNameExt;
	}

	public String getOperatorIdExt() {
		return operatorIdExt;
	}

	public void setOperatorIdExt(String operatorIdExt) {
		this.operatorIdExt = operatorIdExt;
	}

	public String getOperatorNameExt() {
		return operatorNameExt;
	}

	public void setOperatorNameExt(String operatorNameExt) {
		this.operatorNameExt = operatorNameExt;
	}

	public String getCustomerNameExt() {
		return customerNameExt;
	}

	public void setCustomerNameExt(String customerNameExt) {
		this.customerNameExt = customerNameExt;
	}

	public Integer getBusinessTypeExt() {
		return businessTypeExt;
	}

	public void setBusinessTypeExt(Integer businessTypeExt) {
		this.businessTypeExt = businessTypeExt;
	}

	public String getBusinessTypeNameExt() {
		return businessTypeNameExt;
	}

	public void setBusinessTypeNameExt(String businessTypeNameExt) {
		this.businessTypeNameExt = businessTypeNameExt;
	}

	public BigDecimal getBorrowMoneyExt() {
		return borrowMoneyExt;
	}

	public void setBorrowMoneyExt(BigDecimal borrowMoneyExt) {
		this.borrowMoneyExt = borrowMoneyExt;
	}

	public Integer getDelayDaysExt() {
		return delayDaysExt;
	}

	public void setDelayDaysExt(Integer delayDaysExt) {
		this.delayDaysExt = delayDaysExt;
	}

	public String getPhoneStaffExt() {
		return phoneStaffExt;
	}

	public void setPhoneStaffExt(String phoneStaffExt) {
		this.phoneStaffExt = phoneStaffExt;
	}

	public String getVisitStaffExt() {
		return visitStaffExt;
	}

	public void setVisitStaffExt(String visitStaffExt) {
		this.visitStaffExt = visitStaffExt;
	}

	public String getStatusNameExt() {
		return statusNameExt;
	}

	public void setStatusNameExt(String statusNameExt) {
		this.statusNameExt = statusNameExt;
	}

	public Integer getPlanStatusExt() {
		return planStatusExt;
	}

	public void setPlanStatusExt(Integer planStatusExt) {
		this.planStatusExt = planStatusExt;
	}

	public Integer getCollectionStatusExt() {
		return collectionStatusExt;
	}

	public void setCollectionStatusExt(Integer collectionStatusExt) {
		this.collectionStatusExt = collectionStatusExt;
	}

	public Integer getBorrowLimitExt() {
		return borrowLimitExt;
	}

	public void setBorrowLimitExt(Integer borrowLimitExt) {
		this.borrowLimitExt = borrowLimitExt;
	}

	public Integer getRepaymentTypeIdExt() {
		return repaymentTypeIdExt;
	}

	public void setRepaymentTypeIdExt(Integer repaymentTypeIdExt) {
		this.repaymentTypeIdExt = repaymentTypeIdExt;
	}

	public String getClassNameExt() {
		return classNameExt;
	}

	public void setClassNameExt(String classNameExt) {
		this.classNameExt = classNameExt;
	}

	public String getOriginalBusinessIdExt() {
		return originalBusinessIdExt;
	}

	public void setOriginalBusinessIdExt(String originalBusinessIdExt) {
		this.originalBusinessIdExt = originalBusinessIdExt;
	}

	public String getPhoneNumberExt() {
		return phoneNumberExt;
	}

	public void setPhoneNumberExt(String phoneNumberExt) {
		this.phoneNumberExt = phoneNumberExt;
	}

	public String getBusinessCtypeExt() {
		return businessCtypeExt;
	}

	public void setBusinessCtypeExt(String businessCtypeExt) {
		this.businessCtypeExt = businessCtypeExt;
	}

	public BigDecimal getFactAmountExt() {
		return factAmountExt;
	}

	public void setFactAmountExt(BigDecimal factAmountExt) {
		this.factAmountExt = factAmountExt;
	}

	public String getAreaPidExt() {
		return areaPidExt;
	}

	public void setAreaPidExt(String areaPidExt) {
		this.areaPidExt = areaPidExt;
	}

	public String getAreaIdExt() {
		return areaIdExt;
	}

	public void setAreaIdExt(String areaIdExt) {
		this.areaIdExt = areaIdExt;
	}

	public String getRealNameExt() {
		return realNameExt;
	}

	public void setRealNameExt(String realNameExt) {
		this.realNameExt = realNameExt;
	}

	public Date getTradeDateExt() {
		return tradeDateExt;
	}

	public void setTradeDateExt(Date tradeDateExt) {
		this.tradeDateExt = tradeDateExt;
	}

	public String getCanWithholdDescExt() {
		return canWithholdDescExt;
	}

	public void setCanWithholdDescExt(String canWithholdDescExt) {
		this.canWithholdDescExt = canWithholdDescExt;
	}

	public String getIdentifyCardExt() {
		return identifyCardExt;
	}

	public void setIdentifyCardExt(String identifyCardExt) {
		this.identifyCardExt = identifyCardExt;
	}

	public Integer getIsDeferExt() {
		return isDeferExt;
	}

	public void setIsDeferExt(Integer isDeferExt) {
		this.isDeferExt = isDeferExt;
	}

	public Integer getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(Integer registerStatus) {
		this.registerStatus = registerStatus;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.planListId;
	}

	@Override
	public String toString() {
		return "RepaymentBizPlanListSynch{" +
			", planListId=" + planListId +
			", planId=" + planId +
			", businessId=" + businessId +
			", origBusinessId=" + origBusinessId +
			", period=" + period +
			", afterId=" + afterId +
			", dueDate=" + dueDate +
			", totalBorrowAmount=" + totalBorrowAmount +
			", overdueAmount=" + overdueAmount +
			", overdueDays=" + overdueDays +
			", currentStatus=" + currentStatus +
			", currentSubStatus=" + currentSubStatus +
			", repayStatus=" + repayStatus +
			", repayFlag=" + repayFlag +
			", factRepayDate=" + factRepayDate +
			", financeComfirmDate=" + financeComfirmDate +
			", financeConfirmUser=" + financeConfirmUser +
			", financeConfirmUserName=" + financeConfirmUserName +
			", confirmFlag=" + confirmFlag +
			", autoWithholdingConfirmedDate=" + autoWithholdingConfirmedDate +
			", autoWithholdingConfirmedUser=" + autoWithholdingConfirmedUser +
			", autoWithholdingConfirmedUserName=" + autoWithholdingConfirmedUserName +
			", accountantConfirmStatus=" + accountantConfirmStatus +
			", accountantConfirmUser=" + accountantConfirmUser +
			", accountantConfirmUserName=" + accountantConfirmUserName +
			", accountantConfirmDate=" + accountantConfirmDate +
			", derateAmount=" + derateAmount +
			", remark=" + remark +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", srcType=" + srcType +
			", businessIdExt=" + businessIdExt +
			", districtIdExt=" + districtIdExt +
			", districtNameExt=" + districtNameExt +
			", companyIdExt=" + companyIdExt +
			", companyNameExt=" + companyNameExt +
			", operatorIdExt=" + operatorIdExt +
			", operatorNameExt=" + operatorNameExt +
			", customerNameExt=" + customerNameExt +
			", businessTypeExt=" + businessTypeExt +
			", businessTypeNameExt=" + businessTypeNameExt +
			", borrowMoneyExt=" + borrowMoneyExt +
			", delayDaysExt=" + delayDaysExt +
			", phoneStaffExt=" + phoneStaffExt +
			", visitStaffExt=" + visitStaffExt +
			", statusNameExt=" + statusNameExt +
			", planStatusExt=" + planStatusExt +
			", collectionStatusExt=" + collectionStatusExt +
			", borrowLimitExt=" + borrowLimitExt +
			", repaymentTypeIdExt=" + repaymentTypeIdExt +
			", classNameExt=" + classNameExt +
			", originalBusinessIdExt=" + originalBusinessIdExt +
			", phoneNumberExt=" + phoneNumberExt +
			", businessCtypeExt=" + businessCtypeExt +
			", factAmountExt=" + factAmountExt +
			", areaPidExt=" + areaPidExt +
			", areaIdExt=" + areaIdExt +
			", realNameExt=" + realNameExt +
			", tradeDateExt=" + tradeDateExt +
			", canWithholdDescExt=" + canWithholdDescExt +
			", identifyCardExt=" + identifyCardExt +
			", isDeferExt=" + isDeferExt +
			", registerStatus=" + registerStatus +
			", registerTime=" + registerTime +
			"}";
	}
}
