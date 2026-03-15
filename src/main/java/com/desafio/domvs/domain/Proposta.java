package com.desafio.domvs.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class Proposta {
    
    private UUID id;
    private String cpf;
    private String nome;
    private BigDecimal renda;
    private BigDecimal investimentos;
    private int anosDeContaCorrente;
    private OfertaTipo ofertaSelecionada;
    private List<BeneficioTipo> beneficioSelecionado;
    private StatusProposta status;


    public Proposta(){
        this.id = UUID.randomUUID();
        this.status = StatusProposta.RECEBIDO;
    }
}