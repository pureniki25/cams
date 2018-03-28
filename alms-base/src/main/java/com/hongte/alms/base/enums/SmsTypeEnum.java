package com.hongte.alms.base.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/22
 */
public enum SmsTypeEnum implements IEnum {

	CODE("code","验证码"),
	TRIGGER("trigger","触发短信"),
	MARKET("market","营销短信"),
	VOICE("voice","语音短信"),
	ALARM("alarm","预警短信"),
	NOTICE("notice","通知短信"),
	CS("cs","催收短信");

    private String value;
    private String desc;

    SmsTypeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
}
