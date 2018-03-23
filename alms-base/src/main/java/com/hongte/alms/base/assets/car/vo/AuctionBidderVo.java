package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;


public class AuctionBidderVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
    /**
     * 用户姓名 
     */
	@ApiModelProperty(required= true,value = "用户姓名 ")
	private String bidderName;
    /**
     * 身份证号码 
     */
	@ApiModelProperty(required= true,value = "身份证号码 ")
	private String bidderCertId;
    /**
     * 联系方式 
     */
	@ApiModelProperty(required= true,value = "联系方式 ")
	private String bidderTel;
    /**
     * 转账账户 
     */
	@ApiModelProperty(required= true,value = "转账账户 ")
	private String transAccountName;
    /**
     * 转账卡号
     */
	@ApiModelProperty(required= true,value = "转账卡号")
	private String transAccountNum;
    /**
     * 转账银行
     */
	@ApiModelProperty(required= true,value = "转账银行")
	private String transBank;
	
    /**
     * 拍卖登记id
     */
	@ApiModelProperty(required= true,value = "拍卖登记id")
	private String regId;
    /**
     * 资产端业务编号 
     */
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 是否缴纳保证金 
     */
	@ApiModelProperty(required= true,value = "是否缴纳保证金 ")
	private Boolean isPayDeposit;
    /**
     * 出价金额
     */
	@ApiModelProperty(required= true,value = "出价金额")
	private BigDecimal offerAmount;
    /**
     * 是否竞拍成功 
     */
	@ApiModelProperty(required= true,value = "是否竞拍成功 ")
	private Boolean isAuctionSuccess;
    /**
     * 成交价格
     */
	@ApiModelProperty(required= true,value = "成交价格")
	private BigDecimal transPrice;
    /**
     * 竞拍id
     */
	@ApiModelProperty(required= true,value = "竞拍id")
	private String auctionId;
    /**
     * 更新人
     */
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@ApiModelProperty(required= true,value = "更新时间")
	@JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	private Date updateTime;
	
	
	private String payDepositStr;
	
	private String auctionSuccessStr;
	
	public String getBidderName() {
		return bidderName;
	}
	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
	public String getBidderCertId() {
		return bidderCertId;
	}
	public void setBidderCertId(String bidderCertId) {
		this.bidderCertId = bidderCertId;
	}
	public String getBidderTel() {
		return bidderTel;
	}
	public void setBidderTel(String bidderTel) {
		this.bidderTel = bidderTel;
	}
	public String getTransAccountName() {
		return transAccountName;
	}
	public void setTransAccountName(String transAccountName) {
		this.transAccountName = transAccountName;
	}
	public String getTransAccountNum() {
		return transAccountNum;
	}
	public void setTransAccountNum(String transAccountNum) {
		this.transAccountNum = transAccountNum;
	}
	public String getTransBank() {
		return transBank;
	}
	public void setTransBank(String transBank) {
		this.transBank = transBank;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public Boolean getIsPayDeposit() {
		return isPayDeposit;
	}
	public void setIsPayDeposit(Boolean isPayDeposit) {
		this.isPayDeposit = isPayDeposit;
	}
	public BigDecimal getOfferAmount() {
		return offerAmount;
	}
	public void setOfferAmount(BigDecimal offerAmount) {
		this.offerAmount = offerAmount;
	}
	public Boolean getIsAuctionSuccess() {
		return isAuctionSuccess;
	}
	public void setIsAuctionSuccess(Boolean isAuctionSuccess) {
		this.isAuctionSuccess = isAuctionSuccess;
	}
	public BigDecimal getTransPrice() {
		return transPrice;
	}
	public void setTransPrice(BigDecimal transPrice) {
		this.transPrice = transPrice;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
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
	public String getPayDepositStr() {
		return payDepositStr;
	}
	public void setPayDepositStr(String payDepositStr) {
		this.payDepositStr = payDepositStr;
	}
	public String getAuctionSuccessStr() {
		return auctionSuccessStr;
	}
	public void setAuctionSuccessStr(String auctionSuccessStr) {
		this.auctionSuccessStr = auctionSuccessStr;
	}
	
}
