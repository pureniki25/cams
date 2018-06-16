package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 性别枚举
 */
public enum PepayPlanSexEnum implements IEnum {


    MAN(1,"男"),
    WOMEN(2,"女"),
    NONE(3,"未知");

    private Integer value;
    private String desc;

    PepayPlanSexEnum(final int value, final String desc) {
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
        for(PepayPlanSexEnum d : PepayPlanSexEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static PepayPlanSexEnum getByKey(Integer key){
        for(PepayPlanSexEnum d : PepayPlanSexEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(PepayPlanSexEnum d : PepayPlanSexEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
