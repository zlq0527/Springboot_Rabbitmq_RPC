package com.example.rpc_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author     ：zhaolengquan.
 * @ Date       ：Created in 20:13 2022/4/14
 * @ Description：
 */
@Configuration
public class RabbitConfig {
	public static final String QUEUE = "MSG_QUEUE";
	public static final String EXCHANGE = "RPC_EXCHANGE";

	@Bean
	Queue MSGQUEUE() {
		return new Queue(QUEUE);
	}
	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(EXCHANGE);
	}

	@Bean
	Binding bind() {
		return BindingBuilder.bind(MSGQUEUE()).to(topicExchange()).with(QUEUE);
	}



}
