package com.hongte.alms.base.assets.car.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dengzhiming
 * @date 2018/6/1 17:25
 */
public class BusinessBidsVo {
    @ApiModelProperty(value = "id", name = "id")
    private String id;

    @ApiModelProperty(value = "序号", name = "seqNum")
    private int seqNum;

    @ApiModelProperty(value="用户姓名",name="userName")
    private String userName;

    @ApiModelProperty(value="身份证号码",name="idCard")
    private String idCard;


    @ApiModelProperty(value="联系方式",name="telephone")
    private String   telephone;

    @ApiModelProperty(value = "报价时间", name = "bidTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bidTime;

    @ApiModelProperty(value = "报价金额", name = "bidPrice")
    private BigDecimal bidPrice;

    @ApiModelProperty(value="备注",name="remark")
    private  String  remark;

    @ApiModelProperty(value="是否竞拍成功",name="isAuctionSuccess")
    private boolean isAuctionSuccess;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isAuctionSuccess() {
        return isAuctionSuccess;
    }

    public void setAuctionSuccess(boolean auctionSuccess) {
        isAuctionSuccess = auctionSuccess;
    }

}
