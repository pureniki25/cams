package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款计划创建者枚举
 */
public enum RepayPlanCreateSysEnum implements IEnum {
//    来源类型：1.信贷生成，2.贷后管理生成

    XINDAI(1,"信贷系统")
    ,ALMS(2,"贷后管理系统")
    ,NIWOJR(3,"你我金融")
    ,ALMS_SPLIT(4,"贷后拆标")
    ;

    private Integer value;
    private String desc;

    RepayPlanCreateSysEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String nameOf(Integer key){
        for(RepayPlanCreateSysEnum d : RepayPlanCreateSysEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanCreateSysEnum getByKey(Integer key){
        for(RepayPlanCreateSysEnum d : RepayPlanCreateSysEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanCreateSysEnum d : RepayPlanCreateSysEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;
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
