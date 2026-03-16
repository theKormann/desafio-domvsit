package com.desafio.domvs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_conta_cartao")
public class ContaCartao {
    
    @Id
    private UUID id;
    private UUID propostaId;
    private String numeroCartaoVirtual;
    private BigDecimal limiteDisponivel;
    private LocalDateTime dataCriacao;

    public ContaCartao() {
        this.id = UUID.randomUUID();
        this.dataCriacao = LocalDateTime.now();
        this.numeroCartaoVirtual = "5502 " + (int)(Math.random() * 9000 + 1000) + " XXXX XXXX"; 
    }
}