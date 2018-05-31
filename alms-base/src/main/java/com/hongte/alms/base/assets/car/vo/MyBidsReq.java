package com.hongte.alms.base.assets.car.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dengzhiming
 * @date 2018/5/30 14:01
 */
public class MyBidsReq {
    @ApiModelProperty(required= true,value = "用户Id")
    private String userId;
    @ApiModelProperty(required= true,value = "拍卖id")
    private String auctionId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }


}
