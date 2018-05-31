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



}
