/*
 * 文件名 almsServiceApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月17日 下午3:01:38 
 */
package com.hongte.alms.scheduled;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * 服务提供者<br>
 * 
 * @author 谭荣巧
 * @date 2017年11月17日 下午3:01:38
 * @since alms-service 1.0-SNAPSHOT
 */
@EnableFeignClients(basePackages = {"com.ht.ussp.client","com.hongte.alms.scheduled.client", "com.hongte.alms.base.feignClient"})
@SpringCloudApplication
@EnableScheduling
@MapperScan(basePackages = {"com.hongte.alms.scheduled.mapper","com.hongte.alms.base"})
@EnableTransactionManagement 
@ComponentScan(basePackages= {"com.hongte.alms.scheduled","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean","com.hongte.alms.core.*"})
public class ScheduledServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate restTemplateNoLoadBalanced() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ScheduledServiceApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    scheduled-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}

}