package com.microservices.pagamento.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

	@Value("${crud.rabbitmq.exchange}")
	public String exchangeName;
	
	@Value("${crud.rabbitmq.queue}")
	public String queueName;
	
	@Value("${crud.rabbitmq.routingkey}")
	public String routingKeyName;
	
	@Bean
	public Exchange declareExchange() {
		return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
	}
	
	@Bean
	public Queue declareQueue() {
		return QueueBuilder.durable(queueName).build();
	}
	
	@Bean
	public Binding declareBinding() {
		return BindingBuilder.bind(declareQueue()).to(declareExchange()).with(routingKeyName).noargs();
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
