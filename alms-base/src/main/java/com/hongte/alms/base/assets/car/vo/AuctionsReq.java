package com.hongte.alms.base.assets.car.vo;

import java.util.Date;

import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.vo.PageRequest;

public class AuctionsReq extends PageRequest{

	/**
	 * 拍卖类型04 : 审核完成  05 : 拍卖结束
	 */
	private String type;

	/**
	 * 品牌
	 */
	private String vehicleBrand;
	/**
	 * 车辆型号
	 */
	private String carModel;
	/**
	 * 拍卖id
	 * */
	private String auctionId;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}


}
