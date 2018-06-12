package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 还款计划代扣日志流水表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-08
 */
@ApiModel
@TableName("tb_withholding_repayment_log")
public class WithholdingRepaymentLog extends Model<WithholdingRepaymentLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
	@TableId(value="log_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer logId;
    /**
     * 原业务单号
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "原业务单号")
	private String originalBusinessId;
    /**
     * 批次期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "批次期数")
	private String afterId;
    /**
     * 商户订单号
     */
	@TableField("merch_order_id")
	@ApiModelProperty(required= true,value = "商户订单号")
	private String merchOrderId;
    /**
     * 支付公司交易流水号
     */
	@TableField("third_order_id")
	@ApiModelProperty(required= true,value = "支付公司交易流水号")
	private String thirdOrderId;
    /**
     * 本次代扣金额(元)
     */
	@TableField("current_amount")
	@ApiModelProperty(required= true,value = "本次代扣金额(元)")
	private BigDecimal currentAmount;
    /**
     * 客户身份证号码
     */
	@TableField("identity_card")
	@ApiModelProperty(required= true,value = "客户身份证号码")
	private String identityCard;
    /**
     * 客户姓名
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "客户姓名")
	private String customerName;
    /**
     * 银行卡预留手机号码
     */
	@TableField("phone_number")
	@ApiModelProperty(required= true,value = "银行卡预留手机号码")
	private String phoneNumber;
    /**
     * 银行卡号
     */
	@TableField("bank_card")
	@ApiModelProperty(required= true,value = "银行卡号")
	private String bankCard;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 商户号
     */
	@TableField("merchant_account")
	@ApiModelProperty(required= true,value = "商户号")
	private String merchantAccount;
    /**
     * 代扣状态(1:成功,0:失败;2:处理中)
     */
	@TableField("repay_status")
	@ApiModelProperty(required= true,value = "代扣状态(1:成功,0:失败;2:处理中)")
	private Integer repayStatus;
    /**
     * [还款类型，null或0：正常还款，1:提前结清]
     */
	@TableField("settlement_type")
	@ApiModelProperty(required= true,value = "[还款类型，null或0：正常还款，1:提前结清]")
	private Integer settlementType;
    /**
     * 代扣平台ID
     */
	@TableField("bind_platform_id")
	@ApiModelProperty(required= true,value = "代扣平台ID")
	private Integer bindPlatformId;
    /**
     * 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
     */
	@TableField("bool_part_repay")
	@ApiModelProperty(required= true,value = "表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣")
	private Integer boolPartRepay;
    /**
     * 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。  0:非最后一笔代扣，1:最后一笔代扣
     */
	@TableField("bool_last_repay")
	@ApiModelProperty(required= true,value = "表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。  0:非最后一笔代扣，1:最后一笔代扣")
	private Integer boolLastRepay;
    /**
     * 表示本期代扣总金额(若财务选择不含违约金代扣，则此字段不含违约金)，若本期为多笔代扣，此字段存本次多笔代扣的总金额，若非多笔代扣，则此字段存本次代扣总金额
     */
	@TableField("plan_total_repay_money")
	@ApiModelProperty(required= true,value = "表示本期代扣总金额(若财务选择不含违约金代扣，则此字段不含违约金)，若本期为多笔代扣，此字段存本次多笔代扣的总金额，若非多笔代扣，则此字段存本次代扣总金额")
	private BigDecimal planTotalRepayMoney;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
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
	
	
	   /**
     * 银行代码
     */
	@TableField("bank_code")
	@ApiModelProperty(required= true,value = " 银行代码")
	private String bankCode;
	
	
	   /**
  * 银行名称
  */
	@TableField("bank_name")
	@ApiModelProperty(required= true,value = " 银行名称")
	private String bankName;

   
   
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getMerchOrderId() {
		return merchOrderId;
	}

	public void setMerchOrderId(String merchOrderId) {
		this.merchOrderId = merchOrderId;
	}

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMerchantAccount() {
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}

	public Integer getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(Integer settlementType) {
		this.settlementType = settlementType;
	}

	public Integer getBindPlatformId() {
		return bindPlatformId;
	}

	public void setBindPlatformId(Integer bindPlatformId) {
		this.bindPlatformId = bindPlatformId;
	}

	public Integer getBoolPartRepay() {
		return boolPartRepay;
	}

	public void setBoolPartRepay(Integer boolPartRepay) {
		this.boolPartRepay = boolPartRepay;
	}

	public Integer getBoolLastRepay() {
		return boolLastRepay;
	}

	public void setBoolLastRepay(Integer boolLastRepay) {
		this.boolLastRepay = boolLastRepay;
	}

	public BigDecimal getPlanTotalRepayMoney() {
		return planTotalRepayMoney;
	}

	public void setPlanTotalRepayMoney(BigDecimal planTotalRepayMoney) {
		this.planTotalRepayMoney = planTotalRepayMoney;
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
		return this.logId;
	}

	@Override
	public String toString() {
		return "WithholdingRepaymentLog{" +
			", logId=" + logId +
			", originalBusinessId=" + originalBusinessId +
			", afterId=" + afterId +
			", merchOrderId=" + merchOrderId +
			", thirdOrderId=" + thirdOrderId +
			", currentAmount=" + currentAmount +
			", identityCard=" + identityCard +
			", customerName=" + customerName +
			", phoneNumber=" + phoneNumber +
			", bankCard=" + bankCard +
			", remark=" + remark +
			", merchantAccount=" + merchantAccount +
			", repayStatus=" + repayStatus +
			", settlementType=" + settlementType +
			", bindPlatformId=" + bindPlatformId +
			", boolPartRepay=" + boolPartRepay +
			", boolLastRepay=" + boolLastRepay +
			", planTotalRepayMoney=" + planTotalRepayMoney +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
