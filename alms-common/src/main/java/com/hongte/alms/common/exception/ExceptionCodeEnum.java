package com.hongte.alms.common.exception;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author zengkun
 * @since 2018/6/7
 */
public enum ExceptionCodeEnum  implements IEnum {

    NULL(250,"对象为空异常"),
    PAREMTER_ERROR(450,"输入参数错误")




   ;

    private Integer value;
    private String desc;

    ExceptionCodeEnum(final int value, final String desc) {
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
        for(ExceptionCodeEnum d : ExceptionCodeEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static ExceptionCodeEnum getByKey(Integer key){
        for(ExceptionCodeEnum d : ExceptionCodeEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(ExceptionCodeEnum d : ExceptionCodeEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }



}
