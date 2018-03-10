package com.hongte.alms.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WebUIApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebUIApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    WebUI项目启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}


}
