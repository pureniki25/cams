package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 还款计划标额外费率信息计算方式枚举
 */
public enum PeayPlanProjExtRatCalEnum implements IEnum {
  //  费用计算方式:1.借款金额*费率值；2剩余本金*费率值;3,1*费率值

    BY_BORROW_MONEY(1,"借款金额*费率值"),
    BY_REMIND_PRICIPAL(2,"剩余本金*费率值"),
    RATE(3,"1*费率值");

    private Integer value;
    private String desc;

    PeayPlanProjExtRatCalEnum(final int value, final String desc) {
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
        for(PeayPlanProjExtRatCalEnum d : PeayPlanProjExtRatCalEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static PeayPlanProjExtRatCalEnum getByKey(Integer key){
        for(PeayPlanProjExtRatCalEnum d : PeayPlanProjExtRatCalEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(PeayPlanProjExtRatCalEnum d : PeayPlanProjExtRatCalEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
