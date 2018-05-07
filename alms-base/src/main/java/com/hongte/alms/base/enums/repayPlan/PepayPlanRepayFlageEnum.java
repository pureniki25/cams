package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 费用的还款类型枚举
 */
public enum PepayPlanRepayFlageEnum implements IEnum {

//1:期初收取,2:期末收取
    BEGIN(1,"期初收取"),
    END(2,"期末收取");

    private Integer value;
    private String desc;

    PepayPlanRepayFlageEnum(final int value, final String desc) {
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
        for(PepayPlanRepayFlageEnum d : PepayPlanRepayFlageEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static PepayPlanRepayFlageEnum getByKey(Integer key){
        for(PepayPlanRepayFlageEnum d : PepayPlanRepayFlageEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(PepayPlanRepayFlageEnum d : PepayPlanRepayFlageEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
