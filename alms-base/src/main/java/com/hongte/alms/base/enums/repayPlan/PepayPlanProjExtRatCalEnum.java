package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 还款计划标额外费率信息计算方式枚举
 */
public enum PepayPlanProjExtRatCalEnum implements IEnum {
  //  费用计算方式:1.借款金额*费率值；2剩余本金*费率值;3,1*费率值

    BY_BORROW_MONEY(1,"借款金额*费率值"),
    BY_REMIND_MONEY(2,"剩余本金*费率值")
    ,RATE_VALUE(3,"1*费率值")
    ,REMIND_PLAT_FEE(4,"剩余的平台服务费合计")
    ,BY_MONTH_COM_FEE(5,"费率值*月收分公司服务费")
    ,BY_MONTH_PLAT_FEE(6,"费率值*月收平台服务费")
    ,BY_REM_MONEY_AND_FEE(7,"(剩余本金*费率值) - 分公司服务费违约金 - 平台服务费违约金")  //剩余本金*费率值*剩余借款期数   得出的结果需与  剩余本金*6%  比较  取肖值
	,BY_CACLWAY_8(8,"(剩余本金*费率值*剩余借款期数) - 分公司服务费违约金 - 平台服务费违约金");
    private Integer value;
    private String desc;

    PepayPlanProjExtRatCalEnum(final int value, final String desc) {
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




    public static String nameOf(Integer key){
        for(PepayPlanProjExtRatCalEnum d : PepayPlanProjExtRatCalEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static PepayPlanProjExtRatCalEnum getByKey(Integer key){
        for(PepayPlanProjExtRatCalEnum d : PepayPlanProjExtRatCalEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(PepayPlanProjExtRatCalEnum d : PepayPlanProjExtRatCalEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
