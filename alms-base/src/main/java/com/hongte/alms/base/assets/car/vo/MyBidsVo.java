package com.hongte.alms.base.assets.car.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dengzhiming
 * @date 2018/5/30 16:58
 */
public class MyBidsVo {
    @ApiModelProperty(value = "报价时间", name = "bidTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bidTime;

    @ApiModelProperty(value = "报价金额", name = "bidPrice")
    private BigDecimal bidPrice;

    @ApiModelProperty(value="身份证号码",name="idCard")
    private String idCard;

    @ApiModelProperty(value="用户姓名",name="userName")
    private String userName;

    @ApiModelProperty(value="联系方式",name="telephone")
    private String   telephone;

    @ApiModelProperty(value="备注",name="remark")
    private  String  remark;

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
