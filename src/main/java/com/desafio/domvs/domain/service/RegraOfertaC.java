package com.desafio.domvs.domain.service;

import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class RegraOfertaC {

    @Override
    public OfertaTipo getOfertaSuportada() {
        return OfertaTipo.OFERTA_C;
    }

    @Override
    public boolean isElegivel(Proposta proposta) {
        if (proposta.getRenda() == null) return false;
        
        boolean rendaElegivel = proposta.getRenda().compareTo(new BigDecimal("50000.00")) > 0;
        boolean tempoContaElegivel = proposta.getTempoContaCorrenteAnos() > 2;
        
        return rendaElegivel && tempoContaElegivel;
    }
}