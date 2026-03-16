package com.desafio.domvs.infrastructure.messaging;

import com.desafio.domvs.domain.Proposta;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PropostaPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PropostaPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarPropostaAprovada(Proposta proposta) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_PROPOSTAS,
                RabbitMQConfig.ROUTING_KEY_APROVADAS,
                proposta
        );
        System.out.println("🚀 [RabbitMQ] Proposta " + proposta.getId() + " enviada para a fila com sucesso!");
    }
}