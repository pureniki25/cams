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
 * 车辆拍卖信息登记
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@ApiModel
@TableName("tb_car_auction_reg")
public class CarAuctionReg extends Model<CarAuctionReg> {

    private static final long serialVersionUID = 1L;

    /**
     * 拍卖登记id
     */
    @TableId("reg_id")
	@ApiModelProperty(required= true,value = "拍卖登记id")
	private String regId;
    /**
     * 资产端业务编号 
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 用户姓名 
     */
	@TableField("reg_name")
	@ApiModelProperty(required= true,value = "用户姓名 ")
	private String regName;
    /**
     * 身份证号码 
     */
	@TableField("reg_cert_id")
	@ApiModelProperty(required= true,value = "身份证号码 ")
	private String regCertId;
    /**
     * 联系方式 
     */
	@TableField("reg_tel")
	@ApiModelProperty(required= true,value = "联系方式 ")
	private String regTel;
    /**
     * 是否缴纳保证金 
     */
	@TableField("is_pay_deposit")
	@ApiModelProperty(required= true,value = "是否缴纳保证金 ")
	private Boolean isPayDeposit;
    /**
     * 出价金额
     */
	@TableField("offer_amount")
	@ApiModelProperty(required= true,value = "出价金额")
	private BigDecimal offerAmount;
    /**
     * 是否竞拍成功 
     */
	@TableField("is_auction_success")
	@ApiModelProperty(required= true,value = "是否竞拍成功 ")
	private Boolean isAuctionSuccess;
    /**
     * 成交价格
     */
	@TableField("trans_price")
	@ApiModelProperty(required= true,value = "成交价格")
	private BigDecimal transPrice;
    /**
     * 创建时间 
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间 ")
	private Date createTime;
    /**
     * 创建人 
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人 ")
	private String createUser;


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

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getRegCertId() {
		return regCertId;
	}

	public void setRegCertId(String regCertId) {
		this.regCertId = regCertId;
	}

	public String getRegTel() {
		return regTel;
	}

	public void setRegTel(String regTel) {
		this.regTel = regTel;
	}

	public Boolean getPayDeposit() {
		return isPayDeposit;
	}

	public void setPayDeposit(Boolean isPayDeposit) {
		this.isPayDeposit = isPayDeposit;
	}

	public BigDecimal getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(BigDecimal offerAmount) {
		this.offerAmount = offerAmount;
	}

	public Boolean getAuctionSuccess() {
		return isAuctionSuccess;
	}

	public void setAuctionSuccess(Boolean isAuctionSuccess) {
		this.isAuctionSuccess = isAuctionSuccess;
	}

	public BigDecimal getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(BigDecimal transPrice) {
		this.transPrice = transPrice;
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

	@Override
	protected Serializable pkVal() {
		return this.regId;
	}

	@Override
	public String toString() {
		return "CarAuctionReg{" +
			", regId=" + regId +
			", businessId=" + businessId +
			", regName=" + regName +
			", regCertId=" + regCertId +
			", regTel=" + regTel +
			", isPayDeposit=" + isPayDeposit +
			", offerAmount=" + offerAmount +
			", isAuctionSuccess=" + isAuctionSuccess +
			", transPrice=" + transPrice +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
