package com.desafio.domvs.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_PROPOSTAS_APROVADAS = "propostas.aprovadas.queue";
    public static final String EXCHANGE_PROPOSTAS = "propostas.exchange";
    public static final String ROUTING_KEY_APROVADAS = "propostas.aprovadas.routing.key";

    @Bean
    public Queue filaPropostasAprovadas() {
        return new Queue(FILA_PROPOSTAS_APROVADAS, true); 
    }

    @Bean
    public DirectExchange exchangePropostas() {
        return new DirectExchange(EXCHANGE_PROPOSTAS);
    }

    @Bean
    public Binding bindingPropostasAprovadas(Queue filaPropostasAprovadas, DirectExchange exchangePropostas) {
        return BindingBuilder.bind(filaPropostasAprovadas).to(exchangePropostas).with(ROUTING_KEY_APROVADAS);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}