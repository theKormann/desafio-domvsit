package com.desafio.domvs.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_propostas")
public class Proposta {
    
    @Id
    private UUID id;
    
    @Convert(converter = com.desafio.domvs.infrastructure.security.CpfEncryptor.class)
    private String cpf;
    
    private String nome;
    private BigDecimal renda;
    private BigDecimal investimentos;
    private int tempoContaCorrenteAnos;
    
    @Enumerated(EnumType.STRING)
    private OfertaTipo ofertaSelecionada;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tb_proposta_beneficios", joinColumns = @JoinColumn(name = "proposta_id"))
    @Column(name = "beneficio")
    private List<BeneficioTipo> beneficiosSelecionados;
    
    @Enumerated(EnumType.STRING)
    private StatusProposta status;

    public Proposta() {
        this.id = UUID.randomUUID();
        this.status = StatusProposta.RECEBIDA;
    }
}