package com.hongte.alms.base.assets.car.vo;

import java.util.Date;

import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.vo.PageRequest;

public class AuctionsReqVo extends PageRequest{

	

	private String type;//拍卖类型1 即将拍卖，2进行中，3已完成
	private String vehicleBrand;//品牌
	private String carModel;//车辆型号
	private Date 	currentDate;    
	private String priceID;//拍卖id
	private String telephone;//拍卖人手机号
	private String businessId;//业务编号
	private String bidderName;//竞价人姓名
	private String isPayDeposit;//是否缴纳保证金

	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
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
	public String getPriceID() {
		return priceID;
	}
	public void setPriceID(String priceID) {
		this.priceID = priceID;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBidderName() {
		return bidderName;
	}
	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
	public String getIsPayDeposit() {
		return isPayDeposit;
	}
	public void setIsPayDeposit(String isPayDeposit) {
		this.isPayDeposit = isPayDeposit;
	}
	
	
	
	
}
