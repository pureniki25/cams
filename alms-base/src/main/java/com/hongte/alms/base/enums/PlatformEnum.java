package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2018/3/26
 */
public enum PlatformEnum implements IEnum {

    YB_FORM(0,"易宝代扣"),
    YS_FORM(1,"银盛绑卡"),
    FY_FORM(2,"富友绑卡"),
    BF_FORM(3,"宝付代扣"),
    AN_FORM(4,"爱农代扣"),
    YH_FORM(5,"银行代扣"),
    KQ_FORM(6,"快钱代扣");


    private int value;
    private String name;

    PlatformEnum(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }
    public Integer getPlatformId(){
        return this.value;
    }
    @JsonValue
    public String getName(){
        return this.name;
    }
    
	public static PlatformEnum getByKey(Integer key){
		for(PlatformEnum d : PlatformEnum.values()){
			if(key==d.value){
				return d;
			}
		}
		return null;

	}
	
	/**
	 * 根据key获取代扣平台名称
	 * 
	 * @param value
	 * @return
	 * @author huweiqian
	 */
	public static String getNameByKey(Integer value) {

		if (value == null) {
			return null;
		}

		PlatformEnum[] platformEnums = values();
		for (PlatformEnum platformEnum : platformEnums) {
			if (platformEnum.getPlatformId().intValue() == value.intValue()) {
				return platformEnum.getName();
			}
		}
		return null;
	}
}
