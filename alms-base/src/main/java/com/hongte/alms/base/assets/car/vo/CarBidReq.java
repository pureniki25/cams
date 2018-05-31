package com.hongte.alms.base.assets.car.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author dengzhiming
 * @date 2018/5/26 16:20
 *
 */
@ApiModel(value="出价请求",description="出价请求")
public class CarBidReq {
    @ApiModelProperty(value="拍卖ID",name="auctionId",example="test" ,dataType = "String",required = true)
    private String auctionId;
    @ApiModelProperty(value="用户姓名",name="userName",example="test" ,dataType = "String",required = true)
    private String  userName;
    @ApiModelProperty(required= true,value = "用户Id")
    private String userId;
    @ApiModelProperty(value="身份证号码",name="idCard",example="test" ,dataType = "String",required = true)
    private String idCard;
    @ApiModelProperty(value="联系方式",name="telephone",example="13500000000" ,dataType = "String",required = true)
    private String   telephone;
    @ApiModelProperty(value="出价金额",name="amount",example="1000",required = true)
    private BigDecimal amount;
    @ApiModelProperty(value="备注",name="remark",example="test" ,dataType = "String")
    private  String  remark;

    public String getAuctionId() {
        return auctionId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserId() {
        return userId;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
