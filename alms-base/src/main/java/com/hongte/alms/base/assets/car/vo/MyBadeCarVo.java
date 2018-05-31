package com.hongte.alms.base.assets.car.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dengzhiming
 * @date 2018/5/30 19:33
 */
@ApiModel(value="我报过价的车辆返回对象",description="我报过价的车辆返回对象")
public class MyBadeCarVo {
    @ApiModelProperty(value="车辆品牌", name = "vehicleBrand")
    private String vehicleBrand;

    @ApiModelProperty(value="车辆型号", name = "carModel")
    private String carModel;

    @ApiModelProperty(value="拍卖Id", name = "auctionId")
    private String auctionId;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间", name = "startPriceDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startPriceDate;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间", name = "endPriceDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endPriceDate;

    @ApiModelProperty(value="我的报价", name = "myPrice")
    private BigDecimal myPrice;

    @ApiModelProperty(value="车辆位置", name = "position")
    private String position;

    @ApiModelProperty(value="业务编号", name = "businessId")
    private String businessId;

    /**
     * 图片列表
     */
    @ApiModelProperty(value = "图片列表", name = "docs")
    private List<AuctionDocVo> docs;

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

    public Date getStartPriceDate() {
        return startPriceDate;
    }

    public void setStartPriceDate(Date startPriceDate) {
        this.startPriceDate = startPriceDate;
    }

    public Date getEndPriceDate() {
        return endPriceDate;
    }

    public void setEndPriceDate(Date endPriceDate) {
        this.endPriceDate = endPriceDate;
    }

    public BigDecimal getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(BigDecimal myPrice) {
        this.myPrice = myPrice;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<AuctionDocVo> getDocs() {
        return docs;
    }

    public void setDocs(List<AuctionDocVo> docs) {
        this.docs = docs;
    }


}
