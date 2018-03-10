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
 * 车辆拍卖信息
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@ApiModel
@TableName("tb_car_auction")
public class CarAuction extends Model<CarAuction> {

    private static final long serialVersionUID = 1L;
    
    @TableId("auction_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String auctionId;
    
    /**
     * 资产端业务编号 
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 起拍价
     */
	@TableField("starting_price")
	@ApiModelProperty(required= true,value = "起拍价")
	private BigDecimal startingPrice;
    /**
     * 保证金
     */
	@ApiModelProperty(required= true,value = "保证金")
	private BigDecimal deposit;
    /**
     * 加价幅度
     */
	@TableField("fare_range")
	@ApiModelProperty(required= true,value = "加价幅度")
	private BigDecimal fareRange;
    /**
     * 保留价
     */
	@TableField("reserve_price")
	@ApiModelProperty(required= true,value = "保留价")
	private BigDecimal reservePrice;
    /**
     * 拍卖开始时间 
     */
	@TableField("auction_start_time")
	@ApiModelProperty(required= true,value = "拍卖开始时间 ")
	private Date auctionStartTime;
    /**
     * 拍卖截止时间 
     */
	@TableField("auction_end_time")
	@ApiModelProperty(required= true,value = "拍卖截止时间 ")
	private Date auctionEndTime;
    /**
     * 交易类型 01:债权转让,02:交易过户 
     */
	@TableField("trans_type")
	@ApiModelProperty(required= true,value = "交易类型 01:债权转让,02:交易过户 ")
	private String transType;
    /**
     * 优先购权人
     */
	@TableField("priority_purchaser")
	@ApiModelProperty(required= true,value = "优先购权人")
	private String priorityPurchaser;
    /**
     * 取货方式 01:门店自提 02：运费自付
     */
	@TableField("take_way")
	@ApiModelProperty(required= true,value = "取货方式 01:门店自提 02：运费自付")
	private String takeWay;
    /**
     * 竞买开始时间 
     */
	@TableField("buy_start_time")
	@ApiModelProperty(required= true,value = "竞买开始时间 ")
	private Date buyStartTime;
    /**
     * 竞买结束时间 
     */
	@TableField("buy_end_time")
	@ApiModelProperty(required= true,value = "竞买结束时间 ")
	private Date buyEndTime;
    /**
     * 拍卖物所在位置
     */
	@TableField("auction_position")
	@ApiModelProperty(required= true,value = "拍卖物所在位置")
	private String auctionPosition;
    /**
     * 处置单位
     */
	@TableField("handle_unit")
	@ApiModelProperty(required= true,value = "处置单位")
	private String handleUnit;
    /**
     * 单位地址
     */
	@TableField("unit_addr")
	@ApiModelProperty(required= true,value = "单位地址")
	private String unitAddr;
    /**
     * 咨询人
     */
	@ApiModelProperty(required= true,value = "咨询人")
	private String consultant;
    /**
     * 咨询电话
     */
	@TableField("consultant_tel")
	@ApiModelProperty(required= true,value = "咨询电话")
	private String consultantTel;
    /**
     * 账户姓名
     */
	@TableField("acount_name")
	@ApiModelProperty(required= true,value = "账户姓名")
	private String acountName;
    /**
     * 银行卡号
     */
	@TableField("acount_num")
	@ApiModelProperty(required= true,value = "银行卡号")
	private String acountNum;
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

	@ApiModelProperty(required= true,value = "状态")
	private String status;
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public BigDecimal getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(BigDecimal startingPrice) {
		this.startingPrice = startingPrice;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getFareRange() {
		return fareRange;
	}

	public void setFareRange(BigDecimal fareRange) {
		this.fareRange = fareRange;
	}

	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	public Date getAuctionStartTime() {
		return auctionStartTime;
	}

	public void setAuctionStartTime(Date auctionStartTime) {
		this.auctionStartTime = auctionStartTime;
	}

	public Date getAuctionEndTime() {
		return auctionEndTime;
	}

	public void setAuctionEndTime(Date auctionEndTime) {
		this.auctionEndTime = auctionEndTime;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getPriorityPurchaser() {
		return priorityPurchaser;
	}

	public void setPriorityPurchaser(String priorityPurchaser) {
		this.priorityPurchaser = priorityPurchaser;
	}

	public String getTakeWay() {
		return takeWay;
	}

	public void setTakeWay(String takeWay) {
		this.takeWay = takeWay;
	}

	public Date getBuyStartTime() {
		return buyStartTime;
	}

	public void setBuyStartTime(Date buyStartTime) {
		this.buyStartTime = buyStartTime;
	}

	public Date getBuyEndTime() {
		return buyEndTime;
	}

	public void setBuyEndTime(Date buyEndTime) {
		this.buyEndTime = buyEndTime;
	}

	public String getAuctionPosition() {
		return auctionPosition;
	}

	public void setAuctionPosition(String auctionPosition) {
		this.auctionPosition = auctionPosition;
	}

	public String getHandleUnit() {
		return handleUnit;
	}

	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}

	public String getUnitAddr() {
		return unitAddr;
	}

	public void setUnitAddr(String unitAddr) {
		this.unitAddr = unitAddr;
	}

	public String getConsultant() {
		return consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public String getConsultantTel() {
		return consultantTel;
	}

	public void setConsultantTel(String consultantTel) {
		this.consultantTel = consultantTel;
	}

	public String getAcountName() {
		return acountName;
	}

	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}

	public String getAcountNum() {
		return acountNum;
	}

	public void setAcountNum(String acountNum) {
		this.acountNum = acountNum;
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

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "CarAuction{" +
			"  auctionId=" + auctionId +
			", businessId=" + businessId +
			", startingPrice=" + startingPrice +
			", deposit=" + deposit +
			", fareRange=" + fareRange +
			", reservePrice=" + reservePrice +
			", auctionStartTime=" + auctionStartTime +
			", auctionEndTime=" + auctionEndTime +
			", transType=" + transType +
			", priorityPurchaser=" + priorityPurchaser +
			", takeWay=" + takeWay +
			", buyStartTime=" + buyStartTime +
			", buyEndTime=" + buyEndTime +
			", auctionPosition=" + auctionPosition +
			", handleUnit=" + handleUnit +
			", unitAddr=" + unitAddr +
			", consultant=" + consultant +
			", consultantTel=" + consultantTel +
			", acountName=" + acountName +
			", acountNum=" + acountNum +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", status=" + status +
			"}";
	}
	
}
