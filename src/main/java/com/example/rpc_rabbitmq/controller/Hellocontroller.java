package com.example.rpc_rabbitmq.controller;

import com.example.rpc_rabbitmq.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author     ：zhaolengquan.
 * @ Date       ：Created in 20:23 2022/4/14
 * @ Description：
 */
@RestController
public class Hellocontroller {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@GetMapping("/send")
	public String hello(String message) {
		Message msg = MessageBuilder.withBody(message.getBytes()).build();
		//发送消息方法返回值
		Message result = rabbitTemplate.sendAndReceive(RabbitConfig.RPC_EXCHANGE, RabbitConfig.MSG_QUEUE, msg);
		if (result!=null) {
			//发送消息的correlationId
			String correlationId = msg.getMessageProperties().getCorrelationId();
			String s = (String) result.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
			if (correlationId.equals(s)) {
				System.out.println("我收到服务器的响应"+new String(result.getBody()));
			}
		}
		return new String(result.getBody());

	}
}
