package com.desafio.domvs.infrastructure.messaging;

import com.desafio.domvs.domain.ContaCartao;
import com.desafio.domvs.domain.Proposta;
import com.desafio.domvs.domain.repository.ContaCartaoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ContaCartaoListener {

    private final ContaCartaoRepository contaCartaoRepository;

    public ContaCartaoListener(ContaCartaoRepository contaCartaoRepository) {
        this.contaCartaoRepository = contaCartaoRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_PROPOSTAS_APROVADAS)
    public void processarPropostaAprovada(Proposta proposta) {
        System.out.println("📥 [RabbitMQ] Consumindo proposta aprovada: " + proposta.getId());

        ContaCartao conta = new ContaCartao();
        conta.setPropostaId(proposta.getId());
        
        conta.setLimiteDisponivel(proposta.getRenda().multiply(new BigDecimal("0.30")));

        contaCartaoRepository.save(conta);
        System.out.println("✅ Conta Cartão criada com sucesso para a proposta: " + proposta.getId());
    }
}