package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/22
 */
public enum FlagLockEnum implements IEnum {
    OPEN(0,"开"),
    CLOSE(1,"关");

    private Integer value;
    private String desc;

    FlagLockEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }



    public static String nameOf(Integer key){
        for(FlagLockEnum d : FlagLockEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static FlagLockEnum getByKey(Integer key){
        for(FlagLockEnum d : FlagLockEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(FlagLockEnum d : FlagLockEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }



}
