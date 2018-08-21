package com.hongte.alms.base.vo.withhold;

import lombok.Data;

@Data
public class WithholdLimitListVo {
    private String limitId;//限额id
    private String platformName;// 渠道名称
    private String bankName;//银行名称
    private String onceLimit;//单笔限额
    private String dayLimit;//单日限额
    private String monthLimit;//单月限额
    private String status;//启用状态 是否启用此代扣通道，0：关闭，1：启用
    private String subPlatformName;//银行代扣子渠道
}
