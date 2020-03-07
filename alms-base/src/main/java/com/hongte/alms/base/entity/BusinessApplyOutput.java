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
 * 业务出款申请表
 * </p>
 *
 * @author 刘正全
 * @since 2018-08-02
 */
@ApiModel
@TableName("tb_business_apply_output")
public class BusinessApplyOutput extends Model<BusinessApplyOutput> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务单号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "业务单号")
	private String businessId;
    /**
     * 费用项目ID
     */
	@TableField("fee_item_id")
	@ApiModelProperty(required= true,value = "费用项目ID")
	private String feeItemId;
    /**
     * 费用项目名称
     */
	@TableField("fee_item_name")
	@ApiModelProperty(required= true,value = "费用项目名称")
	private String feeItemName;
    /**
     * 费用类型ID
     */
	@TableField("fee_type_id")
	@ApiModelProperty(required= true,value = "费用类型ID")
	private String feeTypeId;
    /**
     * 费用分类名称
     */
	@TableField("fee_type_name")
	@ApiModelProperty(required= true,value = "费用分类名称")
	private String feeTypeName;
    /**
     * [业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]
     */
	@TableField("fee_value")
	@ApiModelProperty(required= true,value = "[业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]")
	private BigDecimal feeValue;
    /**
     * 创建者
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建者")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新者
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新者")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 退费申请状态，0或null：未申请退费，1：已申请退费
     */
	@TableField(exist = false)
	@ApiModelProperty(required= true,value = "退费申请状态，0或null：未申请退费，1：已申请退费")
	private Integer RefundStatus;
    /**
     * 退费申请备注
     */
	@TableField(exist = false)
	@ApiModelProperty(required= true,value = "退费申请备注")
	private String RefundRemark;
    /**
     * 对应的提现编号
     */
	@TableField(exist = false)
	@ApiModelProperty(required= true,value = "对应的提现编号")
	private Integer WithdrawId;
    /**
     * 费用收取方式，1为按比例，2为按固定金额
     */
	@TableField("fee_charging_type")
	@ApiModelProperty(required= true,value = "费用收取方式，1为按比例，2为按固定金额")
	private Integer feeChargingType;
    /**
     * 【弃用】系统默认匹配的费用比例（当收取方式为2时，此字段存零）
     */
	@TableField("system_default_rate")
	@ApiModelProperty(required= true,value = "【弃用】系统默认匹配的费用比例（当收取方式为2时，此字段存零）")
	private BigDecimal systemDefaultRate;
    /**
     * 【弃用】系统默认匹配的费用金额，元
     */
	@TableField("system_default_money")
	@ApiModelProperty(required= true,value = "【弃用】系统默认匹配的费用金额，元")
	private BigDecimal systemDefaultMoney;
    /**
     * [业务实收费用值，根据还款计划，此字段将在收取费用后或者客户每期还款后，累加实收的费用值]
     */
	@TableField("fact_fee_value")
	@ApiModelProperty(required= true,value = "[业务实收费用值，根据还款计划，此字段将在收取费用后或者客户每期还款后，累加实收的费用值]")
	private BigDecimal factFeeValue;
    /**
     * [是否一次收取，1为按月收取，2为一次收取]
     */
	@TableField("is_one_time_charge")
	@ApiModelProperty(required= true,value = "[是否一次收取，1为按月收取，2为一次收取]")
	private Integer isOneTimeCharge;
    /**
     * 放款去处 1:提到银行卡  2：转到可用金额
     */
	@TableField("withdraw_place")
	@ApiModelProperty(required= true,value = "放款去处 1:提到银行卡  2：转到可用金额")
	private Integer withdrawPlace;
    /**
     * [标记该项费用是否单独收取，null或0:不单独收取，1:单独收取]
     */
	@TableField("output_flag")
	@ApiModelProperty(required= true,value = "[标记该项费用是否单独收取，null或0:不单独收取，1:单独收取]")
	private Integer outputFlag;
    /**
     * [标记该项费用的还款类型，1:期初收取,2:期末收取]
     */
	@TableField("repayment_flag")
	@ApiModelProperty(required= true,value = "[标记该项费用的还款类型，1:期初收取,2:期末收取]")
	private Integer repaymentFlag;
    /**
     * [是否P2P主标收取,1为是，0为否]
     */
	@TableField("is_P2P_mainmark_collect")
	@ApiModelProperty(required= true,value = "[是否P2P主标收取,1为是，0为否]")
	private Integer isP2PMainmarkCollect;
    /**
     * [利率,1为年利率，2为月利率，3为日利率]
     */
	@TableField("interest_rate")
	@ApiModelProperty(required= true,value = "[利率,1为年利率，2为月利率，3为日利率]")
	private Integer interestRate;
    /**
     * [是否允许修改,1为是，0为否]
     */
	@TableField("is_allow_modify")
	@ApiModelProperty(required= true,value = "[是否允许修改,1为是，0为否]")
	private Integer isAllowModify;
    /**
     * [是否可记为收入,1为是，0为否]
     */
	@TableField("is_record_income")
	@ApiModelProperty(required= true,value = "[是否可记为收入,1为是，0为否]")
	private Integer isRecordIncome;
    /**
     * [费用是否可退,0或null表示不可退，1表示可退]
     */
	@TableField("can_refund")
	@ApiModelProperty(required= true,value = "[费用是否可退,0或null表示不可退，1表示可退]")
	private Integer canRefund;
    /**
     * [是否设置期限范围]
     */
	@TableField("is_term_range")
	@ApiModelProperty(required= true,value = "[是否设置期限范围]")
	private Integer isTermRange;
    /**
     * [可退剩余金额]
     */
	@TableField("can_refund_money")
	@ApiModelProperty(required= true,value = "[可退剩余金额]")
	private BigDecimal canRefundMoney;
    /**
     * [关联每一项退费信息ID, tb_refund_info ID]
     */
	@TableField("refund_info_id")
	@ApiModelProperty(required= true,value = "[关联每一项退费信息ID, tb_refund_info ID]")
	private String refundInfoId;
    /**
     * 系统默认匹配的费用比例
     */
	@TableField("new_system_default_rate")
	@ApiModelProperty(required= true,value = "系统默认匹配的费用比例")
	private BigDecimal newSystemDefaultRate;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getFeeItemId() {
		return feeItemId;
	}

	public void setFeeItemId(String feeItemId) {
		this.feeItemId = feeItemId;
	}

	public String getFeeItemName() {
		return feeItemName;
	}

	public void setFeeItemName(String feeItemName) {
		this.feeItemName = feeItemName;
	}

	public String getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(String feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public BigDecimal getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
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

	public Integer getRefundStatus() {
		return RefundStatus;
	}

	public void setRefundStatus(Integer RefundStatus) {
		this.RefundStatus = RefundStatus;
	}

	public String getRefundRemark() {
		return RefundRemark;
	}

	public void setRefundRemark(String RefundRemark) {
		this.RefundRemark = RefundRemark;
	}

	public Integer getWithdrawId() {
		return WithdrawId;
	}

	public void setWithdrawId(Integer WithdrawId) {
		this.WithdrawId = WithdrawId;
	}

	public Integer getFeeChargingType() {
		return feeChargingType;
	}

	public void setFeeChargingType(Integer feeChargingType) {
		this.feeChargingType = feeChargingType;
	}

	public BigDecimal getSystemDefaultRate() {
		return systemDefaultRate;
	}

	public void setSystemDefaultRate(BigDecimal systemDefaultRate) {
		this.systemDefaultRate = systemDefaultRate;
	}

	public BigDecimal getSystemDefaultMoney() {
		return systemDefaultMoney;
	}

	public void setSystemDefaultMoney(BigDecimal systemDefaultMoney) {
		this.systemDefaultMoney = systemDefaultMoney;
	}

	public BigDecimal getFactFeeValue() {
		return factFeeValue;
	}

	public void setFactFeeValue(BigDecimal factFeeValue) {
		this.factFeeValue = factFeeValue;
	}

	public Integer getIsOneTimeCharge() {
		return isOneTimeCharge;
	}

	public void setIsOneTimeCharge(Integer isOneTimeCharge) {
		this.isOneTimeCharge = isOneTimeCharge;
	}

	public Integer getWithdrawPlace() {
		return withdrawPlace;
	}

	public void setWithdrawPlace(Integer withdrawPlace) {
		this.withdrawPlace = withdrawPlace;
	}

	public Integer getOutputFlag() {
		return outputFlag;
	}

	public void setOutputFlag(Integer outputFlag) {
		this.outputFlag = outputFlag;
	}

	public Integer getRepaymentFlag() {
		return repaymentFlag;
	}

	public void setRepaymentFlag(Integer repaymentFlag) {
		this.repaymentFlag = repaymentFlag;
	}

	public Integer getIsP2PMainmarkCollect() {
		return isP2PMainmarkCollect;
	}

	public void setIsP2PMainmarkCollect(Integer isP2PMainmarkCollect) {
		this.isP2PMainmarkCollect = isP2PMainmarkCollect;
	}

	public Integer getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Integer interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getIsAllowModify() {
		return isAllowModify;
	}

	public void setIsAllowModify(Integer isAllowModify) {
		this.isAllowModify = isAllowModify;
	}

	public Integer getIsRecordIncome() {
		return isRecordIncome;
	}

	public void setIsRecordIncome(Integer isRecordIncome) {
		this.isRecordIncome = isRecordIncome;
	}

	public Integer getCanRefund() {
		return canRefund;
	}

	public void setCanRefund(Integer canRefund) {
		this.canRefund = canRefund;
	}

	public Integer getIsTermRange() {
		return isTermRange;
	}

	public void setIsTermRange(Integer isTermRange) {
		this.isTermRange = isTermRange;
	}

	public BigDecimal getCanRefundMoney() {
		return canRefundMoney;
	}

	public void setCanRefundMoney(BigDecimal canRefundMoney) {
		this.canRefundMoney = canRefundMoney;
	}

	public String getRefundInfoId() {
		return refundInfoId;
	}

	public void setRefundInfoId(String refundInfoId) {
		this.refundInfoId = refundInfoId;
	}

	public BigDecimal getNewSystemDefaultRate() {
		return newSystemDefaultRate;
	}

	public void setNewSystemDefaultRate(BigDecimal newSystemDefaultRate) {
		this.newSystemDefaultRate = newSystemDefaultRate;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "BusinessApplyOutput{" +
			", businessId=" + businessId +
			", feeItemId=" + feeItemId +
			", feeItemName=" + feeItemName +
			", feeTypeId=" + feeTypeId +
			", feeTypeName=" + feeTypeName +
			", feeValue=" + feeValue +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", RefundStatus=" + RefundStatus +
			", RefundRemark=" + RefundRemark +
			", WithdrawId=" + WithdrawId +
			", feeChargingType=" + feeChargingType +
			", systemDefaultRate=" + systemDefaultRate +
			", systemDefaultMoney=" + systemDefaultMoney +
			", factFeeValue=" + factFeeValue +
			", isOneTimeCharge=" + isOneTimeCharge +
			", withdrawPlace=" + withdrawPlace +
			", outputFlag=" + outputFlag +
			", repaymentFlag=" + repaymentFlag +
			", isP2PMainmarkCollect=" + isP2PMainmarkCollect +
			", interestRate=" + interestRate +
			", isAllowModify=" + isAllowModify +
			", isRecordIncome=" + isRecordIncome +
			", canRefund=" + canRefund +
			", isTermRange=" + isTermRange +
			", canRefundMoney=" + canRefundMoney +
			", refundInfoId=" + refundInfoId +
			", newSystemDefaultRate=" + newSystemDefaultRate +
			"}";
	}
}
