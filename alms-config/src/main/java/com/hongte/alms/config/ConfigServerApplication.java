/*
 * 文件名 almsConfigApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月20日 下午2:34:59 
 */
package com.hongte.alms.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 服务配置中心启动类<br>
 * 
 * @author 谭荣巧
 * @date 2017年11月20日 下午2:34:59
 * @since alms-config 1.0-SNAPSHOT
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication()
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
