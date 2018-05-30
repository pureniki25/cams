/*
 * 文件名 almsServiceApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月17日 下午3:01:38 
 */
package com.hongte.alms.platRepay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * 服务提供者<br>
 * 
 * @author 曾坤
 * @date 2017年4月17日
 * @since alms-service 1.0-SNAPSHOT
 */
@EnableFeignClients(basePackages = {"com.ht.ussp.client", "com.hongte.alms.base.feignClient"})
@SpringCloudApplication
@MapperScan(basePackages = {"com.hongte.alms.finance.mapper","com.hongte.alms.base"})
@EnableTransactionManagement 
@ComponentScan(basePackages= {"com.hongte.alms.platRepay","com.hongte.alms.common", "com.hongte.alms.base.*","com.ht.ussp.bean"})
public class PlatRepayServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(PlatRepayServiceApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    platRepay-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}




}
