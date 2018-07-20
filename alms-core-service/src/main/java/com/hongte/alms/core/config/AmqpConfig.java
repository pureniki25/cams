package com.hongte.alms.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class AmqpConfig {

    public final static String ALMS_DAIHOU = "alms.daihou_synch";
    public final static String ALMS_JIANMIAN = "alms.jianmian_synch";
    public final static String ALMS_CAIWU = "alms.caiwu_synch";
    public final static String ALMS_ROUTING_DAIHOU = "alms.daihou_synch";
    public final static String ALMS_ROUTING_JIANMIAN = "alms.jianmian_synch";
    public final static String ALMS_ROUTING_CAIWU = "alms.caiwu_synch";
    public final static String ALMS_EXCHANGE = "myExchange_alms";

    //创建贷后同步任务队列
    @Bean
    public Queue queueAlmsDaihou() {
    	Map<String, Object> arguments = new HashMap<>();
        return new Queue(AmqpConfig.ALMS_DAIHOU,true, false, false, arguments);
    }

    //创建减免同步任务队列
    @Bean
    public Queue queueAlmsJianmian() {
    	Map<String, Object> arguments = new HashMap<>();
        return new Queue(AmqpConfig.ALMS_JIANMIAN,true, false, false, arguments);
    }
    
    //创建财务同步任务队列
    @Bean
    public Queue queueAlmsCaiwu() {
    	Map<String, Object> arguments = new HashMap<>();
        return new Queue(AmqpConfig.ALMS_JIANMIAN,true, false, false, arguments);
    }


    //创建交换器
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(AmqpConfig.ALMS_EXCHANGE);
    }
    
    //贷后对列绑定并关联到ALMS_ROUTING_DAIHOU
    @Bean
    Binding bindingExchangeDaihou(Queue queueAlmsDaihou, TopicExchange exchange) {
        return BindingBuilder.bind(queueAlmsDaihou).to(exchange).with(AmqpConfig.ALMS_ROUTING_DAIHOU);
    }

    //减免对列绑定并关联到ALMS_JIANMIAN
    @Bean
    Binding bindingExchangeJianmian(Queue queueAlmsJianmian, TopicExchange exchange) {
        return BindingBuilder.bind(queueAlmsJianmian).to(exchange).with(AmqpConfig.ALMS_ROUTING_JIANMIAN);//*表示一个词,#表示零个或多个词
    }

    //财务对列绑定并关联到ALMS_CAIWU
    @Bean
    Binding bindingExchangeCaiwu(Queue queueAlmsCaiwu, TopicExchange exchange) {
        return BindingBuilder.bind(queueAlmsCaiwu).to(exchange).with(AmqpConfig.ALMS_ROUTING_JIANMIAN);//*表示一个词,#表示零个或多个词
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        return new MappingJackson2MessageConverter();
    }

    /**
     * 生产者用
     * @return
     */
    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(jackson2Converter());
        rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
        return rabbitMessagingTemplate;
    }
    
    
    @Bean
    public ConnectionFactory connectionFactory() {
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("/uc");
        return factory;
    }

}