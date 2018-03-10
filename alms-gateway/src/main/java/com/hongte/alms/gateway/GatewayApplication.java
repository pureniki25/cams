/*
 * 文件名 GatewayApplication.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月17日 下午3:17:16 
 */
package com.hongte.alms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 服务网关<br>
 * 
 * @author 谭荣巧
 * @date 2017年11月17日 下午3:17:16
 * @since gateway 1.0-SNAPSHOT
 */
@EnableZuulProxy
@SpringCloudApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
