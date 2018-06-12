/*
 * 文件名  com.hongte.alms.account.config DateConfiguration
 * 版权 Copyright 2017 团贷网
 * 创建人 汤孝松
 * 创建时间 2017/11/21  10:12
 */
package com.hongte.alms.platrepay.config;

import com.hongte.alms.common.util.StringToDateConverter;
import com.hongte.alms.common.util.StringToDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * @author 汤孝松
 * 创建时间 2017/11/21
 */
@Configuration
public class DateConfiguration{

        @Autowired
        private RequestMappingHandlerAdapter handlerAdapter;

        /**
         * 增加字符串转日期的功能
         */
        @PostConstruct
        public void initEditableValidation() {
            ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
                    .getWebBindingInitializer();
            if (initializer.getConversionService() != null) {
                GenericConversionService genericConversionService = (GenericConversionService) initializer
                        .getConversionService();
                genericConversionService.addConverter(new StringToDateConverter());
            }

        }
}

