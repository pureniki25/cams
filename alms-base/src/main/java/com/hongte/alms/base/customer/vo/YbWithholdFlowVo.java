package com.hongte.alms.base.customer.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="贷后客户易宝代扣流水前端对象",description="贷后客户宝付代扣流水前端对象")
@Data
public class YbWithholdFlowVo {

    private String merchOrderId; //易宝订单号
    private String thirdOrderId; //易宝支付订单号
    private String merchantAccount;//易宝商户号


    private String currentAmount;//代扣金额
    private Integer repayStatus; //订单状态

    private String bankCode; //银行编码
    private String createTime; //交易时间
    //商品名称
    //支付卡类型
//    private String updateTime;//完成时间

}
