package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 竞拍人信息登记
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-10
 */
@ApiModel
@TableName("tb_car_auction_bidder")
public class CarAuctionBidder extends Model<CarAuctionBidder> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户姓名 
     */
	@TableField("bidder_name")
	@ApiModelProperty(required= true,value = "用户姓名 ")
	private String bidderName;
    /**
     * 身份证号码 
     */
	@TableField("bidder_cert_id")
	@ApiModelProperty(required= true,value = "身份证号码 ")
	private String bidderCertId;
    /**
     * 联系方式 
     */
    @TableId("bidder_tel")
	@ApiModelProperty(required= true,value = "联系方式 ")
	private String bidderTel;
    /**
     * 转账账户 
     */
	@TableField("trans_account_name")
	@ApiModelProperty(required= true,value = "转账账户 ")
	private String transAccountName;
    /**
     * 转账卡号
     */
	@TableField("trans_account_num")
	@ApiModelProperty(required= true,value = "转账卡号")
	private String transAccountNum;
    /**
     * 转账银行
     */
	@TableField("trans_bank")
	@ApiModelProperty(required= true,value = "转账银行")
	private String transBank;
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
    /**
     * 更新时间 
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间 ")
	private Date updateTime;
    /**
     * 更新人 
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人 ")
	private String updateUser;


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
		return this.bidderTel;
	}

	@Override
	public String toString() {
		return "CarAuctionBidder{" +
			", bidderName=" + bidderName +
			", bidderCertId=" + bidderCertId +
			", bidderTel=" + bidderTel +
			", transAccountName=" + transAccountName +
			", transAccountNum=" + transAccountNum +
			", transBank=" + transBank +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
