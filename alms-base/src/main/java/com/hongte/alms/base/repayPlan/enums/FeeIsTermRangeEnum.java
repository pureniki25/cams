package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 是否是分段收费标志位
 */
public enum FeeIsTermRangeEnum implements IEnum {


    YES(1,"是"),
    NO(0,"否");

    private Integer value;
    private String desc;

    FeeIsTermRangeEnum(final int value, final String desc) {
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
        for(FeeIsTermRangeEnum d : FeeIsTermRangeEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static FeeIsTermRangeEnum getByKey(Integer key){
        for(FeeIsTermRangeEnum d : FeeIsTermRangeEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(FeeIsTermRangeEnum d : FeeIsTermRangeEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
