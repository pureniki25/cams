package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款计划借款期限单位
 */
public enum RepayPlanBorrowLimitUnitEnum implements IEnum {

//    1：月，2：天

    MONTH(1,"月"),
    DAY(2,"天");

    private Integer value;
    private String desc;

    RepayPlanBorrowLimitUnitEnum(final int value, final String desc) {
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
        for(RepayPlanBorrowLimitUnitEnum d : RepayPlanBorrowLimitUnitEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanBorrowLimitUnitEnum getByKey(Integer key){
        for(RepayPlanBorrowLimitUnitEnum d : RepayPlanBorrowLimitUnitEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanBorrowLimitUnitEnum d : RepayPlanBorrowLimitUnitEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }

}
