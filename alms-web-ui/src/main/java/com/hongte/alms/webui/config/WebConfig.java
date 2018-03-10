package com.hongte.alms.webui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源映射
 * @author 黄咏康
 * @since 2017/7/16
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //将所有/** 访问都映射到classpath:/static/ 目录下
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//    }

//    @Configuration
//    public class MyWebAppConfigurer
//            extends WebMvcConfigurerAdapter {
//
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/myres/**").addResourceLocations("classpath:/myres/");
            super.addResourceHandlers(registry);
        }
    //调用本地service，需要解开注释
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                .maxAge(3600);
//    }


//    private

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //将所有/** 访问都映射到classpath:/static/ 目录下
////        registry.addResourceHandler("/excelDownload/**").addResourceLocations("file:${ht.excel.file.save.path}");
//        registry.addResourceHandler("/excelDownload/**").addResourceLocations("file:D:\\testFile\\");
//        super.addResourceHandlers(registry);
//    }
}
