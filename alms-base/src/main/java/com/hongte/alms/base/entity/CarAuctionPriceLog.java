package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 竞价记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-19
 */
@ApiModel
@TableName("tb_car_auction_price_log")
public class CarAuctionPriceLog extends Model<CarAuctionPriceLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 竞拍记录id 
     */
	@ApiModelProperty(required= true,value = "竞拍记录id ")
	private String id;
    /**
     * 竞拍id 
     */
	@TableField("auction_id")
	@ApiModelProperty(required= true,value = "竞拍id ")
	private String auctionId;
    /**
     * 竞拍人手机
     */
	@TableField("bidder_tel")
	@ApiModelProperty(required= true,value = "竞拍人手机")
	private String bidderTel;
    /**
     * 联系方式 
     */
	@ApiModelProperty(required= true,value = "联系方式 ")
	private String price;
    /**
     * 创建时间 
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间 ")
	private Date createTime;
    /**
     * 身份证号码
     */
	@TableField("bidder_cert_id")
	@ApiModelProperty(required= true,value = "身份证号码")
	private String bidderCertId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getBidderTel() {
		return bidderTel;
	}

	public void setBidderTel(String bidderTel) {
		this.bidderTel = bidderTel;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBidderCertId() {
		return bidderCertId;
	}

	public void setBidderCertId(String bidderCertId) {
		this.bidderCertId = bidderCertId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CarAuctionPriceLog{" +
			", id=" + id +
			", auctionId=" + auctionId +
			", bidderTel=" + bidderTel +
			", price=" + price +
			", createTime=" + createTime +
			", bidderCertId=" + bidderCertId +
			"}";
	}
}
