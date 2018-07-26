package com.hongte.alms.base.RepayPlan.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/7/26
 * 还款计划详情应还项合计Dto
 */
@Data
public class PlanListDetailShowPayDto  implements Serializable {

    //费用id
    private  String feelId;

    //费用名
    private String feelName;

    //应还项
    private BigDecimal showPayMoney ;

    //分润顺序
    private Integer  shareProfitIndex;


}
