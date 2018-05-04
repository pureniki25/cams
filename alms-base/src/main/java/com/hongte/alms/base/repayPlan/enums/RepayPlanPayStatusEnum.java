package com.hongte.alms.base.repayPlan.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/5/4
 * 还款计划还款状态枚举   对应 tb_repayment_biz_plan    plan_status   状态
 */
public enum RepayPlanPayStatusEnum implements IEnum {

//0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期
    REPAYINF(0,"还款中"),
    PAYED_EARLY(10,"提前结清"),
    PAYED(20,"已结清"),
    PAYED_LOSS(30,"亏损结清"),
    RENEWED(50,"已申请展期");

    private Integer value;
    private String desc;

    RepayPlanPayStatusEnum(final int value, final String desc) {
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
        for(RepayPlanPayStatusEnum d : RepayPlanPayStatusEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanPayStatusEnum getByKey(Integer key){
        for(RepayPlanPayStatusEnum d : RepayPlanPayStatusEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanPayStatusEnum d : RepayPlanPayStatusEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }






}
