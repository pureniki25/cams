package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 应还项目所属分类枚举
 */
public enum RepayPlanItemTypeEnum implements IEnum {
    //应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，
    // 70：违约金，80：中介费，90：押金类费用，100：冲应收

    PRINCIPAL(10,"本金"),
    INTEREST(20,"利息"),
    SUB_COMPANY_CHARGE(30,"资产端分公司服务费"),
    BOND_COMPANY_CHARGE(40,"担保公司费用"),
    PLAT_CHARGE(50,"资金端平台服务费"),
    OVER_DUE_AMONT(60,"滞纳金"),
    PENALTY_AMONT(70,"违约金"),
    AGENCY_FEE(80,"中介费"),
    DEPOSIT_FEE(90,"押金类费用")
    ,RUSH_RECEIVABLES(100,"冲应收")
    ;

    private int value;
    private String desc;

    RepayPlanItemTypeEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
}
