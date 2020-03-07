package com.hongte.alms.base.enums;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;

/**
 * 邮件配置信息枚举
 * @author huweiqian
 *
 */
public enum EmailInfoEnum implements IEnum {

	EMAIL_PASSWORD("EMAIL_PASSWORD"),
	EMAIL_ACCOUNT("EMAIL_ACCOUNT"),
	EMAIL_SERVICE_HOST("EMAIL_SERVICE_HOST"),
	EMAIL_SERVICE_PORT("EMAIL_SERVICE_PORT"),
	FROM_EMAIL_ADDRESSES("FROM_EMAIL_ADDRESSES"),
	TO_EMAIL_ADDRESSES("TO_EMAIL_ADDRESSES"),
	EMAIL_INFO("EMAIL_INFO"),
	;
	
    private String value;	// 参数类型

    EmailInfoEnum(final String value) {
        this.value = value;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

}
