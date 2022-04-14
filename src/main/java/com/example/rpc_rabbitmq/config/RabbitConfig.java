package com.example.rpc_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author     ：zhaolengquan.
 * @ Date       ：Created in 20:13 2022/4/14
 * @ Description：
 */
@Configuration
public class RabbitConfig {
	public static final String MSG_QUEUE = "MSG_QUEUE";
	public static final String RPC_REPLY = "RPC_REPLY";
	public static final String RPC_EXCHANGE = "RPC_EXCHANGE";

	@Bean
	Queue MSGQUEUE() {
		return new Queue(MSG_QUEUE);
	}
	@Bean
	Queue RPC_REPLY() {
		return new Queue(RPC_REPLY);
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(RPC_EXCHANGE);
	}

	@Bean
	Binding bind() {
		return BindingBuilder.bind(MSGQUEUE()).to(topicExchange()).with(MSG_QUEUE);
	}

	@Bean
	Binding bind2() {
		return BindingBuilder.bind(RPC_REPLY()).to(topicExchange()).with(RPC_EXCHANGE);
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setReplyAddress(RPC_REPLY);
		template.setReplyTimeout(6000);
		return template;
	}
	@Bean
	public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(RabbitConfig.RPC_REPLY);
		container.setMessageListener(rabbitTemplate(connectionFactory));
		return container;
	}


}
