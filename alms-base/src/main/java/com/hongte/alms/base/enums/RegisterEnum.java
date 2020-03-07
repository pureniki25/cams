package com.hongte.alms.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;


/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum RegisterEnum  {

    NO_REGISTER("0","未登记"),
    PART_REGISTER("1","部分登记"),
    ONLINE_REGISTER("2","线上部分足额登记"),
    ALL_REGISTER("3","全款足额登记");
    


    private String value;
    private String name;

    RegisterEnum( String value,  String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }
  
    @JsonValue
    public String getName(){
        return this.name;
    }

    

}
