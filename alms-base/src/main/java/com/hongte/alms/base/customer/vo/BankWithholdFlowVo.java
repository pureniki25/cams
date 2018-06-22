package com.hongte.alms.base.customer.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="贷后客户银行代扣流水前端对象",description="贷后客户银行代扣流水前端对象")
@Data
public class BankWithholdFlowVo {
    private String merchOrderId; //流水号
    private String platformName; //支付公司
    private String currentAmount;//代扣金额
    private Integer repayStatus; //订单状态
    //平台会员编号
    private String bankCode; //银行编码
    private String createTime; //请求时间
    private String updateTime;//完成时间



}
