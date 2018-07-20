package com.hongte.alms.base.RepayPlan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentSettleMoneyDto {

    private BigDecimal money;
    private String projPlanId;
    private Integer repayStatus; //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款

    private Integer planItemType; //应还项目所属分类，10：本金，20：利息，25：利差，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点（返点都是不线上分账的）


}
