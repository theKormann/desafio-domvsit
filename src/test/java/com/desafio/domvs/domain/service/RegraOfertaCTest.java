package com.desafio.domvs.domain.service;

import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RegraOfertaCTest {

    private RegraOfertaC regraOfertaC;

    @BeforeEach
    void setUp() {
        regraOfertaC = new RegraOfertaC();
    }

    @Test
    void deveRetornarOfertaCComoSuportada() {
        assertEquals(OfertaTipo.OFERTA_C, regraOfertaC.getOfertaSuportada());
    }

    @Test
    void deveSerElegivelQuandoRendaETempoDeContaForemSuficientes() {
        Proposta proposta = new Proposta();
        proposta.setRenda(new BigDecimal("55000.00"));
        proposta.setTempoContaCorrenteAnos(3);

        boolean elegivel = regraOfertaC.isElegivel(proposta);

        assertTrue(elegivel, "A proposta deveria ser elegível para a Oferta C");
    }

    @Test
    void naoDeveSerElegivelQuandoRendaForInsuficiente() {
        Proposta proposta = new Proposta();
        proposta.setRenda(new BigDecimal("40000.00")); 
        proposta.setTempoContaCorrenteAnos(5);

        assertFalse(regraOfertaC.isElegivel(proposta), "Não deveria ser elegível devido à renda baixa");
    }

    @Test
    void naoDeveSerElegivelQuandoTempoDeContaForInsuficiente() {
        Proposta proposta = new Proposta();
        proposta.setRenda(new BigDecimal("60000.00"));
        proposta.setTempoContaCorrenteAnos(1); 

        assertFalse(regraOfertaC.isElegivel(proposta), "Não deveria ser elegível devido ao tempo de conta curto");
    }
}