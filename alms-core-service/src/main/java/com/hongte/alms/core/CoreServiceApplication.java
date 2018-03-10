/*
 * 文件名 almsServiceApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月17日 下午3:01:38 
 */
package com.hongte.alms.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 服务提供者<br>
 * 
 * @author 谭荣巧
 * @date 2017年11月17日 下午3:01:38
 * @since alms-service 1.0-SNAPSHOT
 */
@EnableFeignClients(basePackages = {"com.ht.ussp.client"})
@SpringCloudApplication
@MapperScan(basePackages = {"com.hongte.alms.core.mapper","com.hongte.alms.base"})
@EnableTransactionManagement 
@ComponentScan(basePackages= {"com.hongte.alms.core","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean"})
public class CoreServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreServiceApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    core-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}




}
