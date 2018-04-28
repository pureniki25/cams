package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 应还项目所属分类对应fee_id枚举
 */
public enum RepayPlanItemTypeFeeIdEnum implements IEnum {
    //应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，
    // 70：违约金，80：中介费，90：押金类费用，100：冲应收
    //本金：b69a84dc-ed67-4c9f-80bf-89ee8efd5167，
    // 利息：556bce4f-f3a9-4b7a-a8b1-43368bebb49c，
    // 滞纳金：79069922-e13a-4229-8656-2a1e19b44879，
    // 冲应收：adede422-4293-4456-8517-5b4c8874b700，
    // 展期未结清服务费：f6b645e8-480b-11e7-8ed5-000c2928bb0d，
    // 展期未结清其他费用：3a401d0a-480c-11e7-8ed5-000c2928bb0d
    PRINCIPAL("b69a84dc-ed67-4c9f-80bf-89ee8efd5167",10,"本金")
    ,INTEREST("556bce4f-f3a9-4b7a-a8b1-43368bebb49c",20,"利息")
    ,OVER_DUE_AMONT("79069922-e13a-4229-8656-2a1e19b44879",60,"滞纳金")
    ,RUSH_RECEIVABLES("adede422-4293-4456-8517-5b4c8874b700",100,"冲应收")
//    INTEREST(20,"利息"),
//    SUB_COMPANY_CHARGE(30,"资产端分公司服务费"),
//    BOND_COMPANY_CHARGE(40,"担保公司费用"),
//    PLAT_CHARGE(50,"资金端平台服务费"),
//    OVER_DUE_AMONT(60,"滞纳金"),
//    PENALTY_AMONT(70,"违约金"),
//    AGENCY_FEE(80,"中介费"),
//    DEPOSIT_FEE(90,"押金类费用")
//    ,RUSH_RECEIVABLES(100,"冲应收")
    ;

    private String value;
    private  Integer typeValue;
    private String desc;

    RepayPlanItemTypeFeeIdEnum(final String value, final Integer typeValue, final String desc) {
        this.value = value;
        this.typeValue = typeValue;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }


    public Integer getTypeValue() {
        return this.typeValue;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }



}
