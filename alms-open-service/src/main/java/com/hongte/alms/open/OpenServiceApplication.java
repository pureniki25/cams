package com.hongte.alms.open;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 第三方对外服务
 */
@EnableFeignClients
@SpringCloudApplication
@MapperScan(basePackages = {"com.hongte.alms.open.mapper","com.hongte.alms.base"})
@EnableTransactionManagement
@ComponentScan(basePackages= {"com.hongte.alms.open","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean"})
public class OpenServiceApplication {

	public static void main(String[] args) {  
		SpringApplication.run(OpenServiceApplication.class, args);

		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    open-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}
}
