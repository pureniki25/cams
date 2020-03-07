//package com.hongte.alms.core.config;
//
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//public class RabbitMqConfiguration {
//
//	@Bean(name = "ucConnectionFactory")
//	@Primary
//	public ConnectionFactory ucConnectionFactory(@Value("${spring.rabbitmq.hostuc.host}") String host,
//			@Value("${spring.rabbitmq.hostuc.port}") int port,
//			@Value("${spring.rabbitmq.hostuc.username}") String username,
//			@Value("${spring.rabbitmq.hostuc.password}") String password,
//			@Value("${spring.rabbitmq.hostuc.publisher-confirms}") boolean publisherConfirms,
//			@Value("${spring.rabbitmq.hostuc.virtual-host}") String virtualHost) {
//		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//		connectionFactory.setHost(host);
//		connectionFactory.setPort(port);
//		connectionFactory.setUsername(username);
//		connectionFactory.setPassword(password);
//		connectionFactory.setVirtualHost(virtualHost);
//		connectionFactory.setPublisherConfirms(publisherConfirms);
//		return connectionFactory;
//	}
//
//	@Bean(name = "ucRabbitTemplate")
//	// @Primary
//	public RabbitTemplate ucRabbitTemplate(@Qualifier("ucConnectionFactory") ConnectionFactory connectionFactory) {
//		RabbitTemplate hospSyncRabbitTemplate = new RabbitTemplate(connectionFactory);
//		// 使用外部事物
//		// ydtRabbitTemplate.setChannelTransacted(true);
//		return hospSyncRabbitTemplate;
//	}
//
//	@Bean(name = "ucContainerFactory")
//	public SimpleRabbitListenerContainerFactory ucSyncFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
//			@Qualifier("ucConnectionFactory") ConnectionFactory connectionFactory) {
//		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//		configurer.configure(factory, connectionFactory);
//		return factory;
//	}
//
//}
