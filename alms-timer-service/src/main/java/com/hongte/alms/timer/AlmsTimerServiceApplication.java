package com.hongte.alms.timer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//1.禁用spring boot自带的DataSourceAutoConfiguration，因为它会读取application.properties文件的spring.datasource.*的属性并自动配置单数据源
//2.此处需要实现多数据源，所以禁用掉

//@SpringCloudApplication
//@EnableScheduling
////@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
//@SpringBootApplication
//@MapperScan(basePackages = {"com.hongte.alms.timer.mapper","com.hongte.alms.base"})
//@EnableTransactionManagement
//@ComponentScan(basePackages= {"com.hongte.alms.timer.*","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean"})

@EnableFeignClients(basePackages = {"com.ht.ussp.client"})
@SpringCloudApplication
@EnableScheduling
@MapperScan(basePackages = {"com.hongte.alms.core.mapper","com.hongte.alms.timer.mapper","com.hongte.alms.base"})
@EnableTransactionManagement
@ComponentScan(basePackages= {"com.hongte.alms.timer","com.hongte.alms.common","com.hongte.alms.base.*","com.ht.ussp.bean"})
public class AlmsTimerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlmsTimerServiceApplication.class, args);
		System.err.println("ヾ(◍°∇°◍)ﾉﾞ    timer-service启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
	}
}
