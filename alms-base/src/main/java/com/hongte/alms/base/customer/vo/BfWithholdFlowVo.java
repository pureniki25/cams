package com.hongte.alms.base.customer.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="贷后客户宝付代扣流水前端对象",description="贷后客户宝付代扣流水前端对象")
@Data
public class BfWithholdFlowVo {
    private String merchOrderId; //宝付订单号
    private String thirdOrderId; //商户支付订单号
    private String merchantAccount;//商户号


    private String currentAmount;//代扣金额
    private Integer repayStatus; //订单状态

    private String bankCode; //银行编码
    private String createTime; //请求时间
    private String updateTime;//完成时间

}
