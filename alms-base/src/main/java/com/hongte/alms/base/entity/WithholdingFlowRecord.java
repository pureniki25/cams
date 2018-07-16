package com.hongte.alms.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>
 * 代扣平台代扣流水
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-16
 */

@ApiModel
@TableName("tb_withholding_flow_record")
public class WithholdingFlowRecord extends Model<WithholdingFlowRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer id;
    /**
     * 5银行代扣，3宝付代扣，0易宝代扣
     */
	@TableField("withholding_platform")
	@ApiModelProperty(required= true,value = "5银行代扣，3宝付代扣，0易宝代扣")
	private Integer withholdingPlatform;
    /**
     * 商户号
     */
	@TableField("merchant_no")
	@ApiModelProperty(required= true,value = "商户号")
	private String merchantNo;
    /**
     * 终端号
     */
	@TableField("terminal_no")
	@ApiModelProperty(required= true,value = "终端号")
	private String terminalNo;
    /**
     * 交易类型
     */
	@TableField("trade_type")
	@ApiModelProperty(required= true,value = "交易类型")
	private String tradeType;
    /**
     * 商品名称
     */
	@TableField("product_name")
	@ApiModelProperty(required= true,value = "商品名称")
	private String productName;
    /**
     * 商户订单号
     */
	@TableField("merchant_order_no")
	@ApiModelProperty(required= true,value = "商户订单号")
	private String merchantOrderNo;
    /**
     * 交易订单号
     */
	@TableField("trade_order_no")
	@ApiModelProperty(required= true,value = "交易订单号")
	private String tradeOrderNo;
    /**
     * 交易流水号
     */
	@TableField("trade_water_no")
	@ApiModelProperty(required= true,value = "交易流水号")
	private String tradeWaterNo;
    /**
     * 交易时间
     */
	@TableField("trade_date")
	@ApiModelProperty(required= true,value = "交易时间")
	private Date tradeDate;
    /**
     * 代扣金额
     */
	@ApiModelProperty(required= true,value = "代扣金额")
	private BigDecimal amount;
    /**
     * 手续费
     */
	@TableField("service_charge")
	@ApiModelProperty(required= true,value = "手续费")
	private BigDecimal serviceCharge;
    /**
     * 支付卡类型
     */
	@TableField("payment_card_type")
	@ApiModelProperty(required= true,value = "支付卡类型")
	private String paymentCardType;
    /**
     * 清算日期
     */
	@TableField("liquidation_date")
	@ApiModelProperty(required= true,value = "清算日期")
	private Date liquidationDate;
    /**
     * 代扣状态(1:成功,0:失败;2:处理中)
     */
	@TableField("withholding_status")
	@ApiModelProperty(required= true,value = "代扣状态(1:成功,0:失败;2:处理中)")
	private Integer withholdingStatus;
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
     * 银行代码
     */
	@TableField("bank_code")
	@ApiModelProperty(required= true,value = "银行代码")
	private String bankCode;
    /**
     * 银行名称
     */
	@TableField("bank_name")
	@ApiModelProperty(required= true,value = "银行名称")
	private String bankName;
    /**
     * 导入时间
     */
	@TableField("import_time")
	@ApiModelProperty(required= true,value = "导入时间")
	private Date importTime;
    /**
     * 导入系统
     */
	@TableField("import_system")
	@ApiModelProperty(required= true,value = "导入系统")
	private String importSystem;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWithholdingPlatform() {
		return withholdingPlatform;
	}

	public void setWithholdingPlatform(Integer withholdingPlatform) {
		this.withholdingPlatform = withholdingPlatform;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public String getTradeOrderNo() {
		return tradeOrderNo;
	}

	public void setTradeOrderNo(String tradeOrderNo) {
		this.tradeOrderNo = tradeOrderNo;
	}

	public String getTradeWaterNo() {
		return tradeWaterNo;
	}

	public void setTradeWaterNo(String tradeWaterNo) {
		this.tradeWaterNo = tradeWaterNo;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getPaymentCardType() {
		return paymentCardType;
	}

	public void setPaymentCardType(String paymentCardType) {
		this.paymentCardType = paymentCardType;
	}

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	public Integer getWithholdingStatus() {
		return withholdingStatus;
	}

	public void setWithholdingStatus(Integer withholdingStatus) {
		this.withholdingStatus = withholdingStatus;
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

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getImportSystem() {
		return importSystem;
	}

	public void setImportSystem(String importSystem) {
		this.importSystem = importSystem;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WithholdingFlowRecord{" +
			", id=" + id +
			", withholdingPlatform=" + withholdingPlatform +
			", merchantNo=" + merchantNo +
			", terminalNo=" + terminalNo +
			", tradeType=" + tradeType +
			", productName=" + productName +
			", merchantOrderNo=" + merchantOrderNo +
			", tradeOrderNo=" + tradeOrderNo +
			", tradeWaterNo=" + tradeWaterNo +
			", tradeDate=" + tradeDate +
			", amount=" + amount +
			", serviceCharge=" + serviceCharge +
			", paymentCardType=" + paymentCardType +
			", liquidationDate=" + liquidationDate +
			", withholdingStatus=" + withholdingStatus +
			", identityCard=" + identityCard +
			", customerName=" + customerName +
			", phoneNumber=" + phoneNumber +
			", bankCard=" + bankCard +
			", remark=" + remark +
			", bankCode=" + bankCode +
			", bankName=" + bankName +
			", importTime=" + importTime +
			", importSystem=" + importSystem +
			"}";
	}
}
