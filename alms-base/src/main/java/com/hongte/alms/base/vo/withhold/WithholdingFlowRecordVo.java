package com.hongte.alms.base.vo.withhold;

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

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
/**
 * <p>
 * 代扣平台代扣流水
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-16
 */

@ApiModel
@ExcelTarget("WithholdingFlowRecordVo")
public class WithholdingFlowRecordVo  {


    /**
     * 主键/自增
     */
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer id;
    /**
     * 5银行代扣，3宝付代扣，0易宝代扣
     */
	@ApiModelProperty(required= true,value = "5银行代扣，3宝付代扣，0易宝代扣")
	private Integer withholdingPlatform;
    /**
     * 商户号
     */
	@ApiModelProperty(required= true,value = "商户号")
	@Excel(name = "商户号", orderNum = "6",  isImportField = "true_st")
	private String merchantNo;
    /**
     * 终端号
     */
	@ApiModelProperty(required= true,value = "终端号")
	@Excel(name = "终端号", orderNum = "7",  isImportField = "true_st")
	private String terminalNo;
    /**
     * 交易类型
     */
	@ApiModelProperty(required= true,value = "交易类型")
	@Excel(name = "交易类型", orderNum = "8",  isImportField = "true_st")
	private String tradeType;
    /**
     * 商品名称
     */
	@ApiModelProperty(required= true,value = "商品名称")
	private String productName;
    /**
     * 商户订单号
     */
	@ApiModelProperty(required= true,value = "商户订单号")
	@Excel(name = "商户订单号", orderNum = "5",  isImportField = "true_st")
	private String merchantOrderNo;
    /**
     * 交易订单号
     */
	@ApiModelProperty(required= true,value = "交易订单号")
	@Excel(name = "宝付订单号", orderNum = "1",  isImportField = "true_st")
	private String tradeOrderNo;
    /**
     * 交易流水号
     */
	@ApiModelProperty(required= true,value = "交易流水号")
	private String tradeWaterNo;
    /**
     * 交易时间
     */
	@ApiModelProperty(required= true,value = "交易时间")
	private Date tradeDate;
    /**
     * 代扣金额
     */
	@ApiModelProperty(required= true,value = "代扣金额")
	@Excel(name = "金额", orderNum = "9",  isImportField = "true_st")
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
	@ApiModelProperty(required= true,value = "支付卡类型")
	private String paymentCardType;
	/**
     * 支付卡号
     */
	@ApiModelProperty(required= true,value = "支付卡号")
	private String paymentCardNo;
    /**
     * 清算日期
     */
	@ApiModelProperty(required= true,value = "清算日期")
	@Excel(name = "清算日期", orderNum = "10",  isImportField = "true_st")
	private String liquidationDate;
    /**
     * 代扣状态(1:成功,0:失败;2:处理中)
     */
    @ApiModelProperty(required = true, value = "代扣状态(代扣状态: 成功，退款，撤销)")
    private String withholdingStatus;
    /**
     * 客户身份证号码
     */
	@ApiModelProperty(required= true,value = "客户身份证号码")
	private String identityCard;
    /**
     * 客户姓名
     */
	@ApiModelProperty(required= true,value = "客户姓名")
	private String customerName;
    /**
     * 银行卡预留手机号码
     */
	@ApiModelProperty(required= true,value = "银行卡预留手机号码")
	private String phoneNumber;
    /**
     * 银行卡号
     */
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
	@ApiModelProperty(required= true,value = "银行代码")
	private String bankCode;
    /**
     * 银行名称
     */
	@ApiModelProperty(required= true,value = "银行名称")
	private String bankName;
    /**
     * 导入时间
     */
	@ApiModelProperty(required= true,value = "导入时间")
	private Date importTime;
    /**
     * 导入系统
     */
	@ApiModelProperty(required= true,value = "导入系统")
	private String importSystem;

	@Excel(name = "业务编号", orderNum = "2",  isImportField = "true_st")
	private String originalBusinessId;
	
	@Excel(name = "期数", orderNum = "4",  isImportField = "true_st")
	private String afterId;
	@Excel(name = "客户姓名", orderNum = "3",  isImportField = "true_st")
	private String custName;
	
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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

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

	public String getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(String liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

    public String getWithholdingStatus() {
		return withholdingStatus;
	}

    public void setWithholdingStatus(String withholdingStatus) {
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

	public String getPaymentCardNo() {
		return paymentCardNo;
	}

	public void setPaymentCardNo(String paymentCardNo) {
		this.paymentCardNo = paymentCardNo;
	}


}
