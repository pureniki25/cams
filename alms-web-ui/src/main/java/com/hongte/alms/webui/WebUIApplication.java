package com.hongte.alms.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@SpringCloudApplication
//@MapperScan(basePackages = {"com.ht.ussp.bean","com.hongte.alms.webui.controller"})
@EnableFeignClients(basePackages = {"com.ht.ussp.client"})
@ComponentScan(basePackages = {"com.ht.ussp.bean", "com.hongte.alms.webui.controller"})


public class WebUIApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebUIApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    WebUI项目启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");


	}


}
