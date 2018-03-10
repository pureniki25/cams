package com.hongte.alms.timer.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.EventListener;

/**
 * @author zengkun
 * @since 2018/1/29
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
//    @Bean
//    public FilterRegistrationBean getDemoFilter(){
//        DemoFilter demoFilter=new DemoFilter();
//        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
//        registrationBean.setFilter(demoFilter);
//        List<String> urlPatterns=new ArrayList<String>();
//        urlPatterns.add("/*");//拦截路径，可以添加多个
//        registrationBean.setUrlPatterns(urlPatterns);
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//    @Bean
//    public ServletRegistrationBean getDemoServlet(){
//        DemoServlet demoServlet=new DemoServlet();
//        ServletRegistrationBean registrationBean=new ServletRegistrationBean();
//        registrationBean.setServlet(demoServlet);
//        List<String> urlMappings=new ArrayList<String>();
//        urlMappings.add("/demoservlet");////访问，可以添加多个
//        registrationBean.setUrlMappings(urlMappings);
//        registrationBean.setLoadOnStartup(1);
//        return registrationBean;
//    }
/*    @Bean
    public ServletListenerRegistrationBean<EventListener> getDemoListener(){
        ServletListenerRegistrationBean<EventListener> registrationBean
                =new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new QuartzServletContextListener());
//		registrationBean.setOrder(1);
        return registrationBean;
    }*/


}
