/*
 * 文件名 almsServiceApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月17日 下午3:01:38 
 */
package com.hongte.alms.withhold;

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
 * 代扣服务提供者<br>
 * 
 * @author 曾坤
 * @date 2018年5月23日
 * @since alms-service 1.0-SNAPSHOT
 */
@EnableFeignClients(basePackages = {"com.ht.ussp.client","com.hongte.alms.base.feignClient","com.hongte.alms.withhold.feignClient"})
@SpringCloudApplication
@MapperScan(basePackages = {"com.hongte.alms.core.mapper","com.hongte.alms.base"})
@EnableTransactionManagement 
@ComponentScan(basePackages= {"com.hongte.alms.withhold","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean"})
public class WithholdServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(WithholdServiceApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    withhold-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}




}
